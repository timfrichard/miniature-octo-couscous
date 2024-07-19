package com.gift3dconc3pts.batch.processing.config;

import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.batch.listeners.error.StageFileDataReaderErrorListener;
import com.gift3dconc3pts.batch.processing.batch.mapper.StageFileDataFileLineMapper;
import com.gift3dconc3pts.batch.processing.batch.properties.BatchProperties;
import com.gift3dconc3pts.batch.processing.batch.reader.StageFileDataFlatFileReader;
import com.gift3dconc3pts.batch.processing.batch.writers.StageFileDataItemWriter;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.services.ProcessingErrorService;
import com.gift3dconc3pts.batch.processing.core.services.StorageService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class StageFileDataStepConfiguration {

    private final BatchProperties batchProperties;
    private final StorageService storageService;

    @Bean
    @StepScope
    public StageFileDataFileLineMapper stageFileDataFileLineMapper(
            final @Value("#{jobParameters['" + Constants.PARAMETERS_JOB_HEADER_ID + "']}") Long fileUploadJobHeaderId){

        return new StageFileDataFileLineMapper(batchProperties.getBeginningLinesToSkip(), fileUploadJobHeaderId);
    }

    @Bean
    @StepScope
    public StageFileDataReaderErrorListener stageFileDataReaderErrorListener(
            final @Value("#{jobParameters['" + Constants.PARAMETERS_JOB_HEADER_ID + "']}") Long fileUploadJobHeaderId,
            final ProcessingErrorService processingErrorService) {
        return new StageFileDataReaderErrorListener(fileUploadJobHeaderId, processingErrorService);
    }

    @Bean
    @StepScope
    public StageFileDataFlatFileReader stageFileDataFlatFileReader(
            final @Value("#{jobParameters['" + Constants.PARAMETERS_TAS_BETC_FILE_NAME + "']}") String fileName,
            final StageFileDataFileLineMapper stageFileDataFileLineMapper){

        StageFileDataFlatFileReader stageFileDataFlatFileReader = new StageFileDataFlatFileReader(batchProperties.getBeginningLinesToSkip());
        stageFileDataFlatFileReader.setEncoding(batchProperties.getEncoding());
        /* Setting the file name which should have been set during launch */
        stageFileDataFlatFileReader.setResource(storageService.loadAsResource(fileName));
        stageFileDataFlatFileReader.setLineMapper(stageFileDataFileLineMapper);

        return stageFileDataFlatFileReader;
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<FileStaging> synchronizedStageFileDataItemStreamReader(
            final StageFileDataFlatFileReader stageFileDataFlatFileReader){
        return new SynchronizedItemStreamReaderBuilder<FileStaging>().delegate(stageFileDataFlatFileReader).build();
    }

    /**
     * Persist file contents in a staging table for processing in the following step.
     */
    @Bean
    public Step fileDataStagingStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager,
                      final SynchronizedItemStreamReader<FileStaging> synchronizedStageFileDataItemStreamReader,
                      final StageFileDataItemWriter stageFileDataItemWriter,
                      final StageFileDataReaderErrorListener stageFileDataReaderErrorListener) {

        return new StepBuilder(Constants.STEP_STAGE_FILE_DATA, jobRepository)
                .<FileStaging, FileStaging>chunk(batchProperties.getChunkSize(), transactionManager)
                /* Synchronized Async FlatFileItemReader */
                .reader(synchronizedStageFileDataItemStreamReader)
                .listener(stageFileDataReaderErrorListener)
                /* setting the spring data repo as the writer */
                .writer(stageFileDataItemWriter)
                /* Adding fault tolerance in order to configure skipping */
                .faultTolerant()
                /* Skip Limit setting */
                .skipLimit(batchProperties.getSkipLimit())
                /* The specific exceptions we are skipping */
                .skip(FlatFileFormatException.class)
                .skip(ParseException.class)
                .skip(ConstraintViolationException.class)
                .build();
    }
}
