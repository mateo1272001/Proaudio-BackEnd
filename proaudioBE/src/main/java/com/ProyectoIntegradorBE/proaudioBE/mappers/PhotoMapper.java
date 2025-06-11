package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.PhotoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoResponseDto toDto(PhotoEntity rentPrice);

    PhotoEntity toEntity(PhotoRequestDto dto);

    List<PhotoEntity> toEntityList(List<PhotoRequestDto> dtos);

    List<PhotoResponseDto> toDtoList(List<PhotoEntity> photoEntities);
}
