package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsModuleDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResponseDto toDto(TagEntity client);

    TagEntity toEntity(TagRequestDto dto);

    List<AllTagsModuleDto> toListDto(List<TagEntity> client);

}
