package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;

public interface EventService {


    EventResponseDto CreateEvent(EventRequestDto eventRequestDto);

    EventResponseDto UpdateEvent(EventRequestDto eventRequestDto, Long id);

    EventResponseDto DeleteEvent(Long id);

    EventResponseDto GetEvent(Long id);
}
