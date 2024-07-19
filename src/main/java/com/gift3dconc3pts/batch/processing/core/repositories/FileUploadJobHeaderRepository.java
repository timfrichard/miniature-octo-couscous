package com.gift3dconc3pts.batch.processing.core.repositories;


import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FileUploadJobHeaderRepository extends
        CrudRepository<FileUploadJobHeader, Long> {

    @Query("FROM FileUploadJobHeader FUJH WHERE FUJH.jobExecutionId IS NULL AND FUJH.status = 'FILEUPLOADED' AND fileUploadDateTime = " +
            "(SELECT MIN(fileUploadDateTime) FROM FileUploadJobHeader WHERE jobExecutionId IS NULL)")
    public FileUploadJobHeader availableForStaging();
}
