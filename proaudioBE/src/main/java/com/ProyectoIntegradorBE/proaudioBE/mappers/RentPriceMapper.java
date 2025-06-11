package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.RentPriceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentPriceMapper {

    PriceReponseDto toDto(RentPriceEntity rentPrice);

    RentPriceEntity toEntity(PriceRequestDto dto);

    List<RentPriceEntity> toEntityList(List<PriceRequestDto> dtos);

    List<PriceReponseDto> toDtoList(List<RentPriceEntity> rentPriceEntities);
}
