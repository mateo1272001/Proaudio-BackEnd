package com.ProyectoIntegradorBE.proaudioBE.provider;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.TypeDataDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.TypeDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectTypeDataProvider implements TypeDataProvider {

    private final ProjectRepository projectRepository;

    @Override
    public NotificationTypeEnum getType() {
        return NotificationTypeEnum.PROJECT;
    }

    @Override
    public List<TypeDataDto> buildTypeData(NotificationEntity notification) {

        var project = projectRepository.findById(notification.getEntityId())
                .orElseThrow(() -> new BadRequestException("Proyecto no encontrado: " + notification.getEntityId()));

        List<TypeDataDto> data = new ArrayList<>();
        data.add(new TypeDataDto("Nombre", project.getName()));
        data.add(new TypeDataDto("Descripión", project.getDescription()));
        data.add(new TypeDataDto("Fecha de inicio", project.getStartDate().toString()));
        data.add(new TypeDataDto("Fecha de fin", project.getEndDate().toString()));
        data.add(new TypeDataDto("Estado", project.getStatus().name()));
        data.add(new TypeDataDto("Estado de pago", project.getPaymentStatus().name()));

        return data;
    }
}

