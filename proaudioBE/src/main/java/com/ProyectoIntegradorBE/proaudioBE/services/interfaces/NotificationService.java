package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationService {
    NotificationResponseDto createNotification(NotificationRequestDto notificationRequestDto);

    @Transactional
    void createNotificationAndLinkUsers(NotificationRequestDto notificationRequestDto, List<UserResponseDto> users);

    List<NotificationEntity> findNotificationByActionAndEntity(NotificationTypeEnum notificationTypeEnum,
                                                               Long idProject, String action, Boolean isSolved);

    void update(List<NotificationEntity> notifications);

    void findActiveNotificationsAndSendToUsers();

    NotificationListResponseDto findAll(String type, Boolean completed, String name, Integer page, Integer size,
                                        String direction);

    NotificationUserResponseDto readNotification(Long notificationId);

    NotificationTypesResponseDto findAllTypes();

    NotificationDetailsResponseDto getNotificationDetails(Long id);
}
