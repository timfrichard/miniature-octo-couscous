package com.gift3dconc3pts.batch.processing.batch.listeners;

import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import com.gift3dconc3pts.batch.processing.core.services.FileUploadJobHeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Slf4j
public class JobCompletionListener implements JobExecutionListener {

    private static final String QRY_GET_STATUS_EXIT_MSG = "SELECT READ_COUNT, EXIT_CODE, STATUS FROM " +
            "BATCH_STEP_EXECUTION WHERE JOB_EXECUTION_ID="; //BATCH_STEP_EXECUTION
    private static final String UPDATE_FILE_UPLOAD_HEADER = "update FILE_UPLOAD_JOB_HEADER set FK_JOB_EXECUTION_ID = ?, " +
            "READ_COUNT = ?, EXIT_CODE  = ?, STATUS = ? where FILE_UPLOAD_JOB_HEADER_ID = ?";

    private final String postgresqlSchemaPrefix;

    private final FileUploadJobHeaderService fileUploadJobHeaderService;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {

        log.info("JOB FINISHED Sync Data of the Header Table");
        /* Job Execution ID */
        Long executionJobId = jobExecution.getJobId();
        FileUploadJobHeader fileUploadJobHeader = loadFileUploadJobHeader(
                jobExecution.getJobParameters().getLong(Constants.PARAMETERS_JOB_HEADER_ID), executionJobId);

        if (fileUploadJobHeader != null) {
            /* Find the status of the Spring Batch Job */
            jdbcTemplate.query(QRY_GET_STATUS_EXIT_MSG
                            + executionJobId,
                    (rs, row) -> FileUploadJobHeader.builder().readCount(rs.getInt(1))
                            .exitCode(rs.getString(2)).status(rs.getString(3)).build()
            ).forEach(transferFileUploadJobHeader -> { /* There should only be one */
                Integer readCount = transferFileUploadJobHeader.getReadCount();
                log.info("ReadCount: " + readCount);
                fileUploadJobHeader.setReadCount(readCount);

                String exitCode = transferFileUploadJobHeader.getExitCode();
                log.info("ExitCode: " + exitCode);
                fileUploadJobHeader.setExitCode(exitCode);

                String status = transferFileUploadJobHeader.getStatus();
                log.info("Status: " + status);
                fileUploadJobHeader.setStatus(status);
            });

            jdbcTemplate.update(UPDATE_FILE_UPLOAD_HEADER, executionJobId, fileUploadJobHeader.getReadCount(),
                    fileUploadJobHeader.getExitCode(), fileUploadJobHeader.getStatus(), fileUploadJobHeader.getJobHeaderId());
//            fileUploadJobHeaderService.saveFileUploadJobHeader(fileUploadJobHeader);
        }
    }

    private FileUploadJobHeader loadFileUploadJobHeader(Long fileUploadJobHeaderId, Long jobExecutionId) {

        FileUploadJobHeader fileUploadJobHeader = null;
        if (fileUploadJobHeaderId != null) {
            fileUploadJobHeader = fileUploadJobHeaderService
                    .findById(fileUploadJobHeaderId);
            fileUploadJobHeader.setJobExecutionId(jobExecutionId);
        }

        return fileUploadJobHeader;
    }
}
