package com.gift3dconc3pts.batch.processing.core.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FILE_UPLOAD_JOB_HEADER")
public class FileUploadJobHeader {

    @Column(name = "EXIT_CODE")
    private String exitCode;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_UPLOAD_DATETIME")
    private LocalDateTime fileUploadDateTime;

    @Column(name = "FK_JOB_EXECUTION_ID")
    private Long jobExecutionId;

    @Column(name = "FILE_UPLOAD_JOB_HEADER_ID", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_header_sequence_gen")
    @SequenceGenerator(allocationSize = 10, name = "job_header_sequence_gen", sequenceName = "FILE_UPLOAD_JOB_HEADER_SEQ")
    private Long jobHeaderId;

    @Column(name = "READ_COUNT")
    private Integer readCount;

    @Column(name = "STATUS")
    private String status;

}
