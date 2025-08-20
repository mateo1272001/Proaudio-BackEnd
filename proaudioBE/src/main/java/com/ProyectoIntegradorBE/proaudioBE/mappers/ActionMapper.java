package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ActionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActionMapper {

    ActionResponseDto toDto(ActionEntity actionEntity);

}
