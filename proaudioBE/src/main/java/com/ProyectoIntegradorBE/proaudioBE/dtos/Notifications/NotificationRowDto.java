package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationRowDto {

    private Long notificationId;

    private String title;

    private String description;

    private Boolean isSolved;

    private Boolean isSeen;

    private LocalDateTime createdAt;

}
