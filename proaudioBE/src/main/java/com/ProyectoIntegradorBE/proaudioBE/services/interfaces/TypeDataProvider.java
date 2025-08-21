package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.TypeDataDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;

import java.util.List;

public interface TypeDataProvider {

    NotificationTypeEnum getType(); // para registrar el provider

    List<TypeDataDto> buildTypeData(NotificationEntity notification);
}
