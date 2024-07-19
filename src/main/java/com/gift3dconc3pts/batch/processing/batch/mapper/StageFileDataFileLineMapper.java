package com.gift3dconc3pts.batch.processing.batch.mapper;

import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileUploadJobHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;

@RequiredArgsConstructor
@Slf4j
public class StageFileDataFileLineMapper implements LineMapper<FileStaging> {

    private final int linesToSkip;
    private final Long jobHeaderId;

    @Override
    public FileStaging mapLine(final String line, final int lineNumber) throws Exception {

        FileStaging fileStaging = null;
        if(lineNumber > linesToSkip){
            fileStaging = FileStaging.builder().fileLineNum(lineNumber).lineData(line)
                    .fileUploadJobHeader(FileUploadJobHeader.builder().jobHeaderId(jobHeaderId).build()).build();
        }

        return fileStaging;
    }
}
