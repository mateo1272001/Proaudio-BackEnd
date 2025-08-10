package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ClientSortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ClientMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ClientRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.specifications.ClientSpecification;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ClientService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final UtilService utilService;

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

    @Override
    public ClientListResponseDto getClientList(String sortBy, String direction, Integer page, Integer size,
                                               String status) {

        DirectionEnum dir =
                Objects.isNull(direction) ? DirectionEnum.DESC : DirectionEnum.valueOf(direction.toUpperCase());
        page = Objects.nonNull(page) ? page : 1;
        size = Objects.nonNull(size) ? size : 10;
        BasicEnumStatus statusEnum = Objects.nonNull(status) ? BasicEnumStatus.valueOf(status.toUpperCase()) : null;

        ClientSortByEnum sortByEnum =
                Objects.nonNull(sortBy) ? ClientSortByEnum.valueOf(sortBy.toUpperCase()) : ClientSortByEnum.ID;
        String sortColumn = switch (sortByEnum) {
            case NAME -> "name";
            case PHONE -> "phoneNumber";
            case ID -> "clientId";
        };

        Sort sort = Sort.by(Sort.Direction.fromString(dir.name()), sortColumn);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<ClientEntity> spec = ClientSpecification.filterBy(statusEnum);

        Page<ClientEntity> pages = clientRepository.findAll(spec, pageable);
        List<ClientResponseDto> dtoList = pages.getContent().stream().map(clientMapper::toDto).toList();

        PageableDto pagination = utilService.buildPageableDto(pages);

        return new ClientListResponseDto(dtoList, pagination);
    }

    @Override
    public ClientDetailsResponseDto getClientDetails(ClientResponseDto clientResponseDto,
                                                     List<ProjectParticipatedResponseDto> projectParticipatedResponseDtos) {

        ClientDetailsResponseDto clientDetailsResponseDto = new ClientDetailsResponseDto();
        clientDetailsResponseDto.setClientId(clientResponseDto.getClientId());
        clientDetailsResponseDto.setName(clientResponseDto.getName());
        clientDetailsResponseDto.setPhoneNumber(clientResponseDto.getPhoneNumber());
        clientDetailsResponseDto.setEmail(clientResponseDto.getEmail());
        clientDetailsResponseDto.setAddress(clientResponseDto.getAddress());
        clientDetailsResponseDto.setDetails(clientResponseDto.getDetails());
        clientDetailsResponseDto.setStatus(clientResponseDto.getStatus());
        clientDetailsResponseDto.setProjectsParticipated(projectParticipatedResponseDtos);

        return clientDetailsResponseDto;
    }


}
