package com.gift3dconc3pts.batch.processing.core.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROCESSING_ERROR")
public class ProcessingError {

    @ManyToOne(optional = false)
    @JoinColumn(name = "FK_FILE_UPLOAD_JOB_HEADER_ID", nullable = false)
    private FileUploadJobHeader fileUploadJobHeader;

    @Column(name = "ERROR_DESCRIPTION")
    private String processingErrorDescription;

    @Column(name = "PROCESSING_ERROR_ID", unique = true, nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processing_error_sequence_gen")
    @SequenceGenerator(allocationSize = 10, name = "processing_error_sequence_gen", sequenceName = "PROCESSING_ERROR_SEQ")
    private Long processingErrorId;

    @Column(name = "STEP_TYPE_ERROR", length = 30)
    @Enumerated(EnumType.STRING)
    private StepTypeError stepTypeError;

    @Column(name = "VERSION_ID")
    @Version
    private Long version;

}
