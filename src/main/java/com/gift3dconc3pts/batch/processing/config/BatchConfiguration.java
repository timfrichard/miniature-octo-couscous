package com.gift3dconc3pts.batch.processing.config;

import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.batch.listeners.JobCompletionListener;
import com.gift3dconc3pts.batch.processing.core.services.FileUploadJobHeaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableBatchProcessing
@EnableScheduling
@RequiredArgsConstructor
public class BatchConfiguration {

    /* 1 - Pick up the file from the location provided and persist the header table stating file location name & etc. */
    /* 2 - Call Antivirus Service */
    /* 3 - Stage files in the database. */
    /* 4 - Call Transaction service. */

    @Bean
    @JobScope
    public JobCompletionListener jobCompletionListener(
            @Value("${spring.batch.table-prefix:BATCH_}") final String postgresqlSchemaPrefix,
            final FileUploadJobHeaderService fileUploadJobHeaderService,
            final JdbcTemplate jdbcTemplate,
            final @Value("#{jobParameters['" + Constants.PARAMETERS_JOB_HEADER_ID + "']}") Long fileUploadJobHeaderId) {
        return new JobCompletionListener(postgresqlSchemaPrefix, fileUploadJobHeaderService, jdbcTemplate);
    }

    @Bean
    public Job tasBetcTransactionProcessorBatchJob(final JobRepository jobRepository,
                                                   final JobCompletionListener jobCompletionListener,
                                                   final Step clamAVStep, final Step fileDataStagingStep,
                                                   final Step tasBetcAPIStep) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .start(clamAVStep)
                .next(fileDataStagingStep)
                .next(tasBetcAPIStep)
                .build();
    }
}
