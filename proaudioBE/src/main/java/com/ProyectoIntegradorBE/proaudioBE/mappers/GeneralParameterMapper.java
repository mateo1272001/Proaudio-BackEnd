package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.GeneralParameterEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GeneralParameterMapper {

    GeneralParameterResponseDto toDto(GeneralParameterEntity generalParameterEntity);

    List<GeneralParameterResponseDto> toDtoList(List<GeneralParameterEntity> generalParameterEntity);

}
