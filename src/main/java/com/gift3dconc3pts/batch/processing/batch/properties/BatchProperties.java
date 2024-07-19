package com.gift3dconc3pts.batch.processing.batch.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "batch.tasbetc.process.service")
public class BatchProperties {

    private Integer beginningLinesToSkip;

    private Integer chunkSize;

    private String cronSchedule;

    private String[] csvFileHeaders;

    private String encoding;

    private Path fileUploadRootDirectory;

    private Integer intervalInSeconds;

    private Integer skipLimit;
}
