package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.RelationGroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RelationGroupMapper {

    RelationGroupDto toDto(RelationGroupEntity relationGroupEntity);

}
