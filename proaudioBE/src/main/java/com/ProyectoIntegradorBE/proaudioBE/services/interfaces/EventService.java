package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseListDto;

public interface EventService {


    EventResponseDto CreateEvent(EventRequestDto eventRequestDto);

    EventResponseDto UpdateEvent(EventRequestDto eventRequestDto, Long id);

    EventResponseDto DeleteEvent(Long id);

    EventResponseDto GetEvent(Long id);

    EventResponseListDto GetAllEvents(String sortBy, String direction, Integer page, Integer size, String status,
                                      String name);
}
