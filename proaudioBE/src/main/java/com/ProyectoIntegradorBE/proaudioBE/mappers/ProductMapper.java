package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toDto(ProductEntity productEntity);

    ProductEntity toEntity(ProductRequestDto dto);

}
