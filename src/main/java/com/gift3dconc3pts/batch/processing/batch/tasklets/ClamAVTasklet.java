package com.gift3dconc3pts.batch.processing.batch.tasklets;

import com.gift3dconc3pts.batch.processing.client.ClamAVClient;
import com.gift3dconc3pts.batch.processing.core.exceptions.ClamAVException;
import com.gift3dconc3pts.batch.processing.core.model.dtos.ClamAVResponseDTO;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import com.gift3dconc3pts.batch.processing.core.services.FileUploadJobHeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
public class ClamAVTasklet implements Tasklet {

    private final ClamAVClient clamAVClient;
    private final Long jobHeaderId;
    private final FileUploadJobHeaderService fileUploadJobHeaderService;


    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

        log.info("Executing ClamAV API call for JobHeaderID {}", jobHeaderId);
        FileUploadJobHeader fileUploadJobHeader = fileUploadJobHeaderService.findById(jobHeaderId);
        ResponseEntity<ClamAVResponseDTO>  responseEntity = clamAVClient.scanFile(fileUploadJobHeader.getFileName());
        if(!responseEntity.getBody().getClamAVScanResultDTO().isInfected()){
            return RepeatStatus.FINISHED;
        } else {
            throw new ClamAVException("File " + fileUploadJobHeader.getFileName() + " contains malware named: "
                    + responseEntity.getBody().getClamAVScanResultDTO().getVirusName());
        }

    }
}
