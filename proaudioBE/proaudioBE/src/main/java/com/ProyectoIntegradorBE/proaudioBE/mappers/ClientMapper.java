package com.ProyectoIntegradorBE.proaudioBE.mappers;

import dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientResponseDto toDto(ClientEntity client);

    ClientEntity toEntity(ClientResponseDto dto);

}
