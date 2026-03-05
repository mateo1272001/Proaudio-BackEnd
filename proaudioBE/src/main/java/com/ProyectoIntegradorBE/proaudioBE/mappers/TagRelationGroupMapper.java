package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.TagRelationGroupDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagRelationGroupEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagRelationGroupMapper {

    TagRelationGroupDto toDto(TagRelationGroupEntity tagRelationGroupEntity);

}
