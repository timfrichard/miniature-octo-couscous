package com.gift3dconc3pts.batch.processing.batch.scheduler;

import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import com.gift3dconc3pts.batch.processing.core.services.FileUploadJobHeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileProcessorScheduler {

    private final FileUploadJobHeaderService fileUploadJobHeaderService;

    private final Job tasBetcTransactionProcessorBatchJob;

    private final JobLauncher jobLauncher;

    @Scheduled(cron="${batch.tasbetc.process.service.cronSchedule}")
    public void launchFileUploadJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
            JobParametersInvalidException, JobRestartException {
        log.info("Start - launchFileUploadJob()");

        FileUploadJobHeader fileUploadJobHeader = fileUploadJobHeaderService.availableForStaging();
        if (fileUploadJobHeader != null) {
            final JobParameters params = new JobParametersBuilder()
                    .addString(Constants.PARAMETERS_JOB_START_VALUE, String.valueOf(System.currentTimeMillis()))
                    .addLong(Constants.PARAMETERS_JOB_HEADER_ID, fileUploadJobHeader.getJobHeaderId())
                    .addString(Constants.PARAMETERS_TAS_BETC_FILE_NAME, fileUploadJobHeader.getFileName())
                    .toJobParameters();

            log.info("Starting the batch job");
            final JobExecution jobExecution = jobLauncher.run(tasBetcTransactionProcessorBatchJob, params);
        } else {
            log.info("There are no files waiting to be processed.");
        }
    }
}
