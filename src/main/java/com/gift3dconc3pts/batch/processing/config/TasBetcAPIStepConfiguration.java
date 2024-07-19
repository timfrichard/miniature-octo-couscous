package com.gift3dconc3pts.batch.processing.config;

import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.batch.processors.FileStagingToTasBetcProcessor;
import com.gift3dconc3pts.batch.processing.batch.properties.BatchProperties;
import com.gift3dconc3pts.batch.processing.batch.writers.TasBetcAPIItemWriter;
import com.gift3dconc3pts.batch.processing.client.TasBetcClient;
import com.gift3dconc3pts.batch.processing.core.model.dtos.TasBetcDTO;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.repositories.FileStagingPagingRepository;
import com.gift3dconc3pts.batch.processing.core.repositories.FileUploadJobHeaderRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class TasBetcAPIStepConfiguration {

    private final BatchProperties batchProperties;
    private final FileStagingPagingRepository fileStagingPagingRepository;
    private final FileUploadJobHeaderRepository fileUploadJobHeaderRepository;
    private final TasBetcClient tasBetcClient;

    /**
     * Send data saved in the staging tables to the transaction service for submission.
     */
    @Bean
    public Step tasBetcAPIStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager,
                               final ItemReader<FileStaging> fileStagingPagingReader,
                               final TasBetcAPIItemWriter tasBetcAPIItemWriter,
                               final FileStagingToTasBetcProcessor fileStagingToTasBetcProcessor,
                               final TaskExecutor tasBetcAPITaskExecutor) {

        return new StepBuilder(Constants.STEP_TAS_BETC_API_CALL, jobRepository)
                .<FileStaging, TasBetcDTO>chunk(100, transactionManager)
                .reader(fileStagingPagingReader)
//                .listener(stageFileDataReaderErrorListener)
                .processor(fileStagingToTasBetcProcessor)
                /* setting the spring data repo as the writer */
                .writer(tasBetcAPIItemWriter)
                .taskExecutor(tasBetcAPITaskExecutor)
                /* Adding fault tolerance in order to configure skipping */
                .faultTolerant()
                /* Skip Limit setting */
                .skipLimit(2)
                /* The specific exceptions we are skipping */
                .skip(FlatFileFormatException.class)
                .skip(ParseException.class)
                .skip(ConstraintViolationException.class)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<FileStaging> fileStagingPagingReader(
            final @Value("#{jobParameters['" + Constants.PARAMETERS_JOB_HEADER_ID + "']}") Long fileUploadJobHeaderId) {

        RepositoryItemReader<FileStaging> reader = new RepositoryItemReader<>();
        reader.setRepository(fileStagingPagingRepository);
        reader.setMethodName("findByFileUploadJobHeaderOrderByFileLineNumAsc");
        reader.setArguments(Collections.singletonList(fileUploadJobHeaderRepository.findById(fileUploadJobHeaderId).get()));
        reader.setPageSize(100);

        HashMap<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);

        return reader;
    }

    /**
     * This Bean instantiation is for the Item Processor just in case you need to do some massaging of data in
     * the staged tables.
     *
     * @return FileStagingToTasBetcProcessor
     */
    @Bean
    @StepScope
    public FileStagingToTasBetcProcessor fileStagingToTasBetcProcessor(
            final @Value("#{jobParameters['" + Constants.PARAMETERS_JOB_HEADER_ID + "']}") Long fileUploadJobHeaderId) {
        return new FileStagingToTasBetcProcessor(batchProperties);
    }

    @Bean
    public TaskExecutor tasBetcAPITaskExecutor() {
        return new SimpleAsyncTaskExecutor("tasbetc-api");
    }
}
