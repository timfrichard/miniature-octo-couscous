package com.gift3dconc3pts.batch.processing.core.repositories;

import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FileStagingPagingRepository extends PagingAndSortingRepository<FileStaging, Long> {

    Page<FileStaging> findByFileUploadJobHeaderOrderByFileLineNumAsc(FileUploadJobHeader fileUploadJobHeader,
                                                                     Pageable pageable);
}
