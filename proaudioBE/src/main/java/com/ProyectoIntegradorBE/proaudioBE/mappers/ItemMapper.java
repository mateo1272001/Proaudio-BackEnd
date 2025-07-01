package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRowDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemResponseDto toDto(ItemEntity item);

    List<ItemResponseDto> toDtoList(List<ItemEntity> items);

    ItemEntity toEntity(ItemRequestDto dto);

    ItemRowDto toRowDto(ItemEntity item);

}
