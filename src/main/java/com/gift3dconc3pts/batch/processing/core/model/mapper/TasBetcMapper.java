package com.gift3dconc3pts.batch.processing.core.model.mapper;


import com.gift3dconc3pts.batch.processing.core.model.dtos.TasBetcDTO;
import com.gift3dconc3pts.batch.processing.core.model.entities.TasBetc;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TasBetcMapper {

    TasBetcMapper tasBetcMapper = Mappers.getMapper(TasBetcMapper.class);

    TasBetc dtoTasBetcToTasBetc(TasBetcDTO tasBetcDTO);
}
