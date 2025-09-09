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

import static com.ProyectoIntegradorBE.proaudioBE.Utils.AppConstants.CLIENT_CURRENTLY_PRESENT;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final UtilService utilService;

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;


    @Override
    public ClientResponseDto getClientById(Long id) {

        ClientEntity clientEntity = clientRepository.findByClientId(id)
                .orElseThrow(() -> new BadRequestException("¡No existe un cliente con ese id!"));

        return clientMapper.toDto(clientEntity);

    }

    private static void clientValdations(ClientRequestDto clientRequestDto) {
        if (Objects.isNull(clientRequestDto.getName())) {
            throw new BadRequestException("¡El cliente debe tener un nombre!");
        }
        if (Objects.isNull(clientRequestDto.getPhoneNumber())) {
            throw new BadRequestException("¡El cliente debe tener un número de teléfono!");
        }
        if (Objects.isNull(clientRequestDto.getEmail())) {
            throw new BadRequestException("¡El cliente debe tener un email!");
        }
        if (Objects.isNull(clientRequestDto.getAddress())) {
            throw new BadRequestException("¡El cliente debe tener una dirección!");
        }
    }

    @Override
    public ClientResponseDto createClient(ClientRequestDto clientRequestDto) {

        clientValdations(clientRequestDto);

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setName(clientRequestDto.getName());
        clientEntity.setPhoneNumber(clientRequestDto.getPhoneNumber());
        clientEntity.setEmail(clientRequestDto.getEmail());
        clientEntity.setAddress(clientRequestDto.getAddress());
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

        clientValdations(clientRequestDto);

        clientEntity.setName(clientRequestDto.getName());
        clientEntity.setPhoneNumber(clientRequestDto.getPhoneNumber());
        clientEntity.setEmail(clientRequestDto.getEmail());
        clientEntity.setAddress(clientRequestDto.getAddress());
        clientEntity.setDetails(
                StringUtils.isBlank(clientRequestDto.getDetails()) ? null : clientRequestDto.getDetails());
        clientEntity.setStatus(BasicEnumStatus.ENABLED);

        clientEntity = clientRepository.save(clientEntity);

        return clientMapper.toDto(clientEntity);
    }

    @Override
    public ClientResponseDto deleteClient(Long id, List<ProjectParticipatedResponseDto> projects) {

        ClientEntity clientEntity = clientRepository.findByClientIdAndStatus(id, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new BadRequestException("¡No se encontró un cliente activo con ese id!"));

        List<ProjectParticipatedResponseDto> impedingProjects =
                projects.stream().filter(p -> CLIENT_CURRENTLY_PRESENT.contains(p.getStatus())).toList();

        if (!impedingProjects.isEmpty()) {
            throw new BadRequestException("Este cliente está participando en un proyecto!");
        }

        clientEntity.setStatus(BasicEnumStatus.DISABLED);

        clientRepository.save(clientEntity);

        return clientMapper.toDto(clientEntity);

    }

    private static Specification<ClientEntity> getClientSpecification(String name, BasicEnumStatus statusEnum) {
        if (StringUtils.isBlank(name)) {

            return ClientSpecification.filterBy(statusEnum);

        } else {

            return ClientSpecification.filterByStatusAndName(statusEnum, name);

        }
    }

    @Override
    public ClientListResponseDto getClientList(String sortBy, String direction, Integer page, Integer size,
                                               String status, String name) {

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

        Specification<ClientEntity> spec = getClientSpecification(name, statusEnum);

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
