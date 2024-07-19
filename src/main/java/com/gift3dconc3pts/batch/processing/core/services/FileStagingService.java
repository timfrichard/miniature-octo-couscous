package com.gift3dconc3pts.batch.processing.core.services;

import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.repositories.FileStagingRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileStagingService {

    private final FileStagingRepository fileStagingRepository;

    public FileStaging save(final FileStaging fileStaging) {
        return fileStagingRepository.save(fileStaging);
    }

    public List<FileStaging> saveAll(final List<? extends FileStaging> fileStagings) {
        return Lists.newArrayList(fileStagingRepository.saveAll(fileStagings));
    }
}
