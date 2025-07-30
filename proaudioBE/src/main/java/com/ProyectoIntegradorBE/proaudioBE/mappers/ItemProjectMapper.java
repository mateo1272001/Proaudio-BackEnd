package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemProjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemProjectMapper {

    ItemProjectResponseDto toDto(ItemProjectEntity itemProjectEntity);

}
