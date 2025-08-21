package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.NotificationResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponseDto toDto(NotificationEntity notificationEntity);

}
