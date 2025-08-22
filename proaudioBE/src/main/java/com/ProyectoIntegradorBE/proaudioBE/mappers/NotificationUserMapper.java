package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.NotificationUserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationUserMapper {

    NotificationUserResponseDto toDto(NotificationUserEntity notificationUserEntity);

}
