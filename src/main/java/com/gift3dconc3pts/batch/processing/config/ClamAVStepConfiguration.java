package com.gift3dconc3pts.batch.processing.config;

import com.gift3dconc3pts.batch.processing.batch.tasklets.ClamAVTasklet;
import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.batch.properties.BatchProperties;
import com.gift3dconc3pts.batch.processing.client.ClamAVClient;
import com.gift3dconc3pts.batch.processing.core.services.FileUploadJobHeaderService;
import com.gift3dconc3pts.batch.processing.core.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ClamAVStepConfiguration {

    private final BatchProperties batchProperties;
    private final ClamAVClient clamAVClient;

    @Bean
    public Step clamAVStep(final JobRepository jobRepository,
                      final PlatformTransactionManager transactionManager, final ClamAVTasklet clamAVTasklet) {

        return new StepBuilder(Constants.STEP_CLAM_AV , jobRepository)
                .tasklet(clamAVTasklet, transactionManager).build();
    }

    @Bean
    @StepScope
    public ClamAVTasklet clamAVTasklet(final FileUploadJobHeaderService fileUploadJobHeaderService,
                                       final StorageService storageService,
            final @Value("#{jobParameters['" + Constants.PARAMETERS_JOB_HEADER_ID + "']}") Long fileUploadJobHeaderId){
        return new ClamAVTasklet(clamAVClient, fileUploadJobHeaderId, fileUploadJobHeaderService);
    }
}
