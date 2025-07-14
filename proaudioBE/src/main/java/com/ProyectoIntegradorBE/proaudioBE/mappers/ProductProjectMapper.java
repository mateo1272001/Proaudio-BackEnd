package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductProjectEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductProjectMapper {

    ProductProjectResponseDto toDto(ProductProjectEntity productProjectEntity);

}
