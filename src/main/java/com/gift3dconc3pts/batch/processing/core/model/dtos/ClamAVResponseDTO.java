package com.gift3dconc3pts.batch.processing.core.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClamAVResponseDTO {

    private ClamAVScanResultDTO scanResult;
}
