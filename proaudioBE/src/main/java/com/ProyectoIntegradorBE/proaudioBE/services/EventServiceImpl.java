package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.EventEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.EventSortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.EventMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.EventRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.specifications.EventSpecification;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.EventService;
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
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final UtilService utilService;

    private static void nullValidations(EventRequestDto eventRequestDto) {
        if (Objects.isNull(eventRequestDto.getName())) {
            throw new BadRequestException("¡El evento debe tener nombre!");
        }
        if (Objects.isNull(eventRequestDto.getDistance())) {
            throw new BadRequestException("¡El evento debe tener distancia!");
        }
        if (Objects.isNull(eventRequestDto.getAddress())) {
            throw new BadRequestException("¡El evento debe tener por una dirección!");
        }
    }

    @Override
    public EventResponseDto CreateEvent(EventRequestDto eventRequestDto) {

        EventEntity eventEntity = new EventEntity();
        nullValidations(eventRequestDto);
        eventEntity.setName(eventRequestDto.getName());
        eventEntity.setAddress(eventRequestDto.getAddress());
        eventEntity.setDistance(eventRequestDto.getDistance());
        eventEntity.setDescription(
                Objects.nonNull(eventRequestDto.getDescription()) ? eventRequestDto.getDescription() : null);
        eventEntity.setStatus(BasicEnumStatus.ENABLED);

        eventEntity = eventRepository.save(eventEntity);

        return eventMapper.toDto(eventEntity);
    }

    @Override
    public EventResponseDto UpdateEvent(EventRequestDto eventRequestDto, Long id) {

        EventEntity eventEntity =
                eventRepository.findById(id).orElseThrow(() -> new BadRequestException("¡Evento no encontrado!"));

        nullValidations(eventRequestDto);

        eventEntity.setName(eventRequestDto.getName());
        eventEntity.setDistance(eventRequestDto.getDistance());
        eventEntity.setAddress(eventRequestDto.getAddress());

        if (Objects.nonNull(eventRequestDto.getDescription())) {
            eventEntity.setDescription(eventRequestDto.getDescription());
        }

        eventEntity = eventRepository.save(eventEntity);

        return eventMapper.toDto(eventEntity);
    }

    @Override
    public EventResponseDto DeleteEvent(Long id) {

        EventEntity eventEntity =
                eventRepository.findById(id).orElseThrow(() -> new BadRequestException("¡Evento no encontrado!"));

        if (eventEntity.getStatus().equals(BasicEnumStatus.DISABLED)) {
            throw new BadRequestException("¡El evento ya fue eliminado!");
        }

        eventEntity.setStatus(BasicEnumStatus.DISABLED);

        eventEntity = eventRepository.save(eventEntity);

        return eventMapper.toDto(eventEntity);
    }

    @Override
    public EventResponseDto GetEvent(Long id) {

        EventEntity eventEntity =
                eventRepository.findById(id).orElseThrow(() -> new BadRequestException("¡Evento no encontrado!"));

        return eventMapper.toDto(eventEntity);
    }

    @Override
    public EventResponseListDto GetAllEvents(String sortBy, String direction, Integer page, Integer size, String status,
                                             String name) {

        DirectionEnum dir =
                Objects.isNull(direction) ? DirectionEnum.DESC : DirectionEnum.valueOf(direction.toUpperCase());
        page = Objects.nonNull(page) ? page : 1;
        size = Objects.nonNull(size) ? size : 10;
        BasicEnumStatus statusEnum = Objects.nonNull(status) ? BasicEnumStatus.valueOf(status.toUpperCase()) : null;

        EventSortByEnum sortByEnum =
                Objects.nonNull(sortBy) ? EventSortByEnum.valueOf(sortBy.toUpperCase()) : EventSortByEnum.ID;
        String sortColumn = switch (sortByEnum) {
            case NAME -> "name";
            case DISTANCE -> "distance";
            case ID -> "eventId";
        };

        Sort sort = Sort.by(Sort.Direction.fromString(dir.name()), sortColumn);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<EventEntity> spec = EventSpecification.filterBy(statusEnum, name);

        Page<EventEntity> pages = eventRepository.findAll(spec, pageable);

        List<EventResponseDto> dtoList = pages.getContent().stream().map(eventMapper::toDto).toList();

        PageableDto pagination = utilService.buildPageableDto(pages);

        return new EventResponseListDto(dtoList, pagination);
    }

}
