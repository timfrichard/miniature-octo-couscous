package com.gift3dconc3pts.batch.processing.core.services;


import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import com.gift3dconc3pts.batch.processing.core.repositories.FileUploadJobHeaderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileUploadJobHeaderService {

    private final FileUploadJobHeaderRepository fileUploadJobHeaderRepository;

    public FileUploadJobHeader availableForStaging() {
        return fileUploadJobHeaderRepository.availableForStaging();
    }

    @Transactional
    public FileUploadJobHeader saveFileUploadJobHeader(final FileUploadJobHeader fileUploadJobHeader){
        log.info("Saving File Upload Header {}", fileUploadJobHeader);
        return fileUploadJobHeaderRepository.save(fileUploadJobHeader);
    }

    public FileUploadJobHeader findById(Long jobHeaderId) {
        return fileUploadJobHeaderRepository.findById(jobHeaderId).get();
    }
}
