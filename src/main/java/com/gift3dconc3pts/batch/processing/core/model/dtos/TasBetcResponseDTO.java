package com.gift3dconc3pts.batch.processing.core.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasBetcResponseDTO {

    private List<TasBetcDTO> tasBetcDTOS;
}
