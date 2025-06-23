package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductTagMapper {

    List<ProductTagResponseDto> toDtoList(List<ProductTagEntity> productTagEntities);

    ProductTagResponseDto toDto(ProductTagEntity productTagEntity);

    ProductTagEntity toEntity(ProductTagRequestDto productTagRequestDto);
}
