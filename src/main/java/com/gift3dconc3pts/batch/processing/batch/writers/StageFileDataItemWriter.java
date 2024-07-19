package com.gift3dconc3pts.batch.processing.batch.writers;

import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.repositories.FileUploadJobHeaderRepository;
import com.gift3dconc3pts.batch.processing.core.services.FileStagingService;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StageFileDataItemWriter extends JpaItemWriter<FileStaging> {

    private final EntityManagerFactory entityManagerFactory;
    private final FileStagingService fileStagingService;
    private final FileUploadJobHeaderRepository fileUploadJobHeaderRepository;
    private StepExecution stepExecution;

    public StageFileDataItemWriter(EntityManagerFactory entityManagerFactory, FileStagingService fileStagingService, FileUploadJobHeaderRepository fileUploadJobHeaderRepository) {
        setEntityManagerFactory(entityManagerFactory);
        this.entityManagerFactory = entityManagerFactory;
        this.fileStagingService = fileStagingService;
        this.fileUploadJobHeaderRepository = fileUploadJobHeaderRepository;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(final Chunk<? extends FileStaging> fileStagingChunk) {

        fileStagingChunk.forEach(fileStaging -> {
            final Long jobHeaderId = this.stepExecution.getJobExecution().getJobParameters().getLong("jobHeaderId");
            log.info("JobHeader from Spring Batch: {}", jobHeaderId);
            log.info("JobHeader from Java Object: {}", fileStaging.getFileUploadJobHeader().getJobHeaderId());
        });

        fileStagingService.saveAll(fileStagingChunk.getItems());
    }
}
