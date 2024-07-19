package com.gift3dconc3pts.batch.processing.batch.processors;

import com.gift3dconc3pts.batch.processing.batch.properties.BatchProperties;
import com.gift3dconc3pts.batch.processing.core.model.dtos.TasBetcDTO;
import com.gift3dconc3pts.batch.processing.core.model.entities.FileStaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;

@RequiredArgsConstructor
@Slf4j
public class FileStagingToTasBetcProcessor implements ItemProcessor<FileStaging, TasBetcDTO> {

    private final BatchProperties batchProperties;

    @Override
    public TasBetcDTO process(final FileStaging fileStaging) throws Exception {

        LineTokenizer lineTokenizer = createTasBetcLineTokenizer();
        var tasBetcFieldSet  = lineTokenizer.tokenize(fileStaging.getLineData());
        var tasBetcDTO = createTasBetcFieldSetMapper().mapFieldSet(tasBetcFieldSet);

        return tasBetcDTO;
    }

    /**
     * This method will set the fieldset mapper to use the TasBetc entity.
     *
     * @return FieldSetMapper
     */
    private FieldSetMapper<TasBetcDTO> createTasBetcFieldSetMapper() {

        BeanWrapperFieldSetMapper<TasBetcDTO> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TasBetcDTO.class);

        return beanWrapperFieldSetMapper;
    }

    /**
     * Creating a tokenizer for which a comma is the delimiter and the headers which are set in the yml properties
     * file that matches the private variable name in the bean entity.
     *
     * @return LineTokenizer
     */
    private LineTokenizer createTasBetcLineTokenizer() {

        DelimitedLineTokenizer fileLineTokenizer = new DelimitedLineTokenizer();
        fileLineTokenizer.setDelimiter(",");
        fileLineTokenizer.setNames(batchProperties.getCsvFileHeaders());
        return fileLineTokenizer;
    }
}
