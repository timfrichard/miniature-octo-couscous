package com.gift3dconc3pts.batch.processing.batch.writers;

import com.gift3dconc3pts.batch.processing.client.TasBetcClient;
import com.gift3dconc3pts.batch.processing.core.model.dtos.TasBetcDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TasBetcAPIItemWriter implements ItemWriter<TasBetcDTO> {

    private final TasBetcClient tasBetcClient;
    private StepExecution stepExecution;

    public TasBetcAPIItemWriter(final TasBetcClient tasBetcClient) {
        this.tasBetcClient = tasBetcClient;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(Chunk<? extends TasBetcDTO> tasBetcDtos) throws Exception {

        System.out.println("API calls for "+ tasBetcDtos.size()+ " elements");
        List<TasBetcDTO> saveTasBetcDtos = Lists.newArrayList();
//        tasBetcDtos.forEach(tasBetcDTOS -> tasBetcDTOS.forEach(tasBetcDTO -> saveTasBetcDtos.add(tasBetcDTO)));
        tasBetcDtos.forEach(saveTasBetcDtos::add);
        tasBetcClient.saveTasBetcs(saveTasBetcDtos);
    }
}
