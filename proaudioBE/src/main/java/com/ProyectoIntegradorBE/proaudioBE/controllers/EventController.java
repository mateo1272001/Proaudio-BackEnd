package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    private EventResponseDto CreateEvent(@RequestBody EventRequestDto eventRequestDto) {

        return eventService.CreateEvent(eventRequestDto);

    }

    @PutMapping("{id}")
    private EventResponseDto UpdateEvent(@RequestBody EventRequestDto eventRequestDto, @PathVariable Long id) {

        return eventService.UpdateEvent(eventRequestDto, id);

    }

    @DeleteMapping("{id}")
    private EventResponseDto DeleteEvent(@PathVariable Long id) {

        return eventService.DeleteEvent(id);

    }

    @GetMapping("{id}")
    private EventResponseDto GetEvent(@PathVariable Long id) {

        return eventService.GetEvent(id);

    }

}
