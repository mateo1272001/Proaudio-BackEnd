package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ClientMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ClientRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ClientService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDto getClientById(Long id) {

        ClientEntity clientEntity = clientRepository.findByClientIdAndStatus(id, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new BadRequestException("¡No existe un cliente activo con ese id!"));

        return clientMapper.toDto(clientEntity);

    }

    @Override
    public ClientResponseDto createClient(ClientRequestDto clientRequestDto) {

        if (Objects.isNull(clientRequestDto.getName())) {
            throw new BadRequestException("¡El cliente debe tener, por lo menos, un nombre!");
        }

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(clientRequestDto.getName());
        clientEntity.setPhoneNumber(
                StringUtils.isBlank(clientRequestDto.getPhoneNumber()) ? null : clientRequestDto.getPhoneNumber());
        clientEntity.setEmail(StringUtils.isBlank(clientRequestDto.getEmail()) ? null : clientRequestDto.getEmail());
        clientEntity.setAddress(
                StringUtils.isBlank(clientRequestDto.getAddress()) ? null : clientRequestDto.getAddress());
        clientEntity.setDetails(
                StringUtils.isBlank(clientRequestDto.getDetails()) ? null : clientRequestDto.getDetails());
        clientEntity.setStatus(BasicEnumStatus.ENABLED);

        clientEntity = clientRepository.save(clientEntity);

        return clientMapper.toDto(clientEntity);
    }

    @Override
    public ClientResponseDto updateClient(Long id, ClientRequestDto clientRequestDto) {

        ClientEntity clientEntity = clientRepository.findByClientIdAndStatus(id, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new BadRequestException("¡No se encontró un cliente activo con ese id!"));

        if (Objects.isNull(clientRequestDto.getName())) {
            throw new BadRequestException("¡El cliente debe tener, por lo menos, un nombre!");
        }

        clientEntity.setName(clientRequestDto.getName());
        clientEntity.setPhoneNumber(
                StringUtils.isBlank(clientRequestDto.getPhoneNumber()) ? null : clientRequestDto.getPhoneNumber());
        clientEntity.setEmail(StringUtils.isBlank(clientRequestDto.getEmail()) ? null : clientRequestDto.getEmail());
        clientEntity.setAddress(
                StringUtils.isBlank(clientRequestDto.getAddress()) ? null : clientRequestDto.getAddress());
        clientEntity.setDetails(
                StringUtils.isBlank(clientRequestDto.getDetails()) ? null : clientRequestDto.getDetails());
        clientEntity.setStatus(BasicEnumStatus.ENABLED);

        clientEntity = clientRepository.save(clientEntity);

        return clientMapper.toDto(clientEntity);
    }

    @Override
    public ClientResponseDto deleteClient(Long id) {

        ClientEntity clientEntity = clientRepository.findByClientIdAndStatus(id, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new BadRequestException("¡No se encontró un cliente activo con ese id!"));

        //todo add validation of project presence

        clientEntity.setStatus(BasicEnumStatus.DISABLED);

        clientRepository.save(clientEntity);

        return clientMapper.toDto(clientEntity);

    }

}
