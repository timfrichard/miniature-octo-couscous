package com.gift3dconc3pts.batch.processing.web.controller;

import com.gift3dconc3pts.batch.processing.batch.StatusEnum;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import com.gift3dconc3pts.batch.processing.core.repositories.FileUploadJobHeaderRepository;
import com.gift3dconc3pts.batch.processing.core.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("startFileUpload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadJobHeaderRepository fileUploadJobHeaderRepository;
    private final StorageService storageService;

    @PostMapping
    public FileUploadJobHeader uploadProcessingFile(@RequestParam("tasBetcFile") MultipartFile tasBetcFile) {

        FileUploadJobHeader fileUploadJobHeader = FileUploadJobHeader.builder()
                .fileName(tasBetcFile.getOriginalFilename())
                .fileUploadDateTime(LocalDateTime.now())
                .status(StatusEnum.FILEUPLOADED.name()).build();

        try {
            storageService.store(tasBetcFile);
        } finally {
            fileUploadJobHeader = fileUploadJobHeaderRepository.save(fileUploadJobHeader);
        }

        return fileUploadJobHeader;
    }
}
