package com.gift3dconc3pts.batch.processing.batch.reader;

import com.gift3dconc3pts.batch.processing.batch.Constants;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;

@Slf4j
public class StageFileDataFlatFileReader extends FlatFileItemReader<FileStaging> {

    public StageFileDataFlatFileReader(int linesToSkip){
        setLinesToSkip(linesToSkip);
        setName(Constants.FILE_UPLOAD_READER_ITEM);
    }
}
