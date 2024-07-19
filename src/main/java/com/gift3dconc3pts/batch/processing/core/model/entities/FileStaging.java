package com.gift3dconc3pts.batch.processing.core.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_staging")
public class FileStaging {

    @Column(name = "FILE_STAGING_ID", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_staging_sequence_gen")
    @SequenceGenerator(allocationSize = 10, name = "file_staging_sequence_gen", sequenceName = "FILE_STAGING_SEQ")
    private Long id;

    @Column(name = "FILE_LINE_DATA")
    private String lineData;

    @Column(name = "FILE_LINE_NUM")
    private Integer fileLineNum;

    @ManyToOne(optional = false)
    @JoinColumn(name = "FK_FILE_UPLOAD_JOB_HEADER_ID", nullable = false)
    private FileUploadJobHeader fileUploadJobHeader;
}
