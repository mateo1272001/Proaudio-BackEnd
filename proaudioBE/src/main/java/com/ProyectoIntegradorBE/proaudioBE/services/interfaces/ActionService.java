package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionResponseDto;

public interface ActionService {
    ActionResponseDto createAction(ActionRequestDto actionRequestDto);

    ActionResponseDto getAction(Long id);

    ActionResponseDto getAction(String key);

    ActionResponseDto deleteAction(Long id);
}
