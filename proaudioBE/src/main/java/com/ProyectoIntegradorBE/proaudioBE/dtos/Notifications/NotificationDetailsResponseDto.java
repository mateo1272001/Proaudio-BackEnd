package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationDetailsResponseDto {

    private Long notificationId;

    private String title;

    private String description;

    private Boolean isSolved;

    private Boolean isSeen;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private NotificationTypeEnum type;

    private Long entityId;

    private ActionResponseDto action;

    private List<TypeDataDto> typeData;

}
