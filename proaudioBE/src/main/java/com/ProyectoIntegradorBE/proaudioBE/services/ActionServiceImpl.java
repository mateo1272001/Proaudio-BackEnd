package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ActionEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ActionMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ActionRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper;

    @Override
    public ActionResponseDto createAction(ActionRequestDto actionRequestDto) {

        ActionEntity actionEntity = new ActionEntity();
        actionEntity.setActionKey(actionRequestDto.getActionKey());
        actionEntity.setDescription(actionRequestDto.getDescription());
        actionEntity.setStatus(BasicEnumStatus.ENABLED);

        actionEntity = actionRepository.save(actionEntity);

        return actionMapper.toDto(actionEntity);
    }

    @Override
    public ActionResponseDto getAction(Long id) {

        ActionEntity actionEntity = actionRepository.findByActionId(id)
                .orElseThrow(() -> new BadRequestException("No hay una acción con este id"));

        return actionMapper.toDto(actionEntity);
    }

    @Override
    public ActionResponseDto getAction(String key) {

        ActionEntity actionEntity = actionRepository.findByActionKey(key)
                .orElseThrow(() -> new BadRequestException("No hay una acción con este id"));

        return actionMapper.toDto(actionEntity);
    }


    @Override
    public ActionResponseDto deleteAction(Long id) {

        ActionEntity actionEntity = actionRepository.findByActionId(id)
                .orElseThrow(() -> new BadRequestException("No hay una acción con este id"));

        actionEntity.setStatus(BasicEnumStatus.DISABLED);

        actionEntity = actionRepository.save(actionEntity);

        return actionMapper.toDto(actionEntity);
    }


}
