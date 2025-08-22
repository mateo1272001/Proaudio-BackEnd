package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationResponseDto {

    private Long notificationId;

    private String title;

    private String description;

    private Boolean isSolved;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private NotificationTypeEnum type;

}
