package com.gift3dconc3pts.batch.processing.core.services;


import com.gift3dconc3pts.batch.processing.core.model.entities.ProcessingError;
import com.gift3dconc3pts.batch.processing.core.repositories.ProcessingErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProcessingErrorService {

    private final ProcessingErrorRepository processingErrorRepository;

    public ProcessingErrorService(ProcessingErrorRepository processingErrorRepository) {
        this.processingErrorRepository = processingErrorRepository;
    }

    public ProcessingError saveProcessingError(ProcessingError processingError){

        return processingErrorRepository.save(processingError);
    }
}
