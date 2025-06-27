package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.EventEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.EventMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.EventRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    @Override
    public EventResponseDto CreateEvent(EventRequestDto eventRequestDto) {

        EventEntity eventEntity = new EventEntity();
        if (Objects.isNull(eventRequestDto.getName()) || Objects.isNull(eventRequestDto.getDistance())) {
            throw new BadRequestException("¡El evento debe tener por lo menos nombre y distancia!");
        }
        eventEntity.setName(eventRequestDto.getName());
        eventEntity.setAddress(Objects.nonNull(eventRequestDto.getAddress()) ? eventRequestDto.getAddress() : null);
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

        if (Objects.nonNull(eventRequestDto.getName())) {
            eventEntity.setName(eventRequestDto.getName());
        }
        if (Objects.nonNull(eventRequestDto.getDistance())) {
            eventEntity.setDistance(eventRequestDto.getDistance());
        }
        eventEntity.setAddress(Objects.nonNull(eventRequestDto.getAddress()) ? eventRequestDto.getAddress() : null);
        eventEntity.setDescription(
                Objects.nonNull(eventRequestDto.getDescription()) ? eventRequestDto.getDescription() : null);

        eventEntity = eventRepository.save(eventEntity);

        return eventMapper.toDto(eventEntity);
    }

    @Override
    public EventResponseDto DeleteEvent(Long id) {

        EventEntity eventEntity =
                eventRepository.findById(id).orElseThrow(() -> new BadRequestException("¡Evento no encontrado!"));

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

}
