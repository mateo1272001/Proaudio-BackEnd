package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/action")
public class ActionController {

    private final ActionService actionService;

    @PostMapping
    private ActionResponseDto createAction(@RequestBody ActionRequestDto actionRequestDto) {
        return actionService.createAction(actionRequestDto);
    }

    @GetMapping("{id}")
    private ActionResponseDto getAction(@PathVariable Long id) {
        return actionService.getAction(id);
    }

    @DeleteMapping("{id}")
    private ActionResponseDto deleteAction(@PathVariable Long id) {
        return actionService.deleteAction(id);
    }


}
