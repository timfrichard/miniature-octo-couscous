package com.gift3dconc3pts.batch.processing.core.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClamAVScanResultDTO {

    private String fileName;
    private String virusName;
    private String timeStamp;
    private boolean infected;
}
