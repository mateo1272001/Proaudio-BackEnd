package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Boolean isSolved;

    private LocalDateTime expiresAt;

    @NotNull
    private NotificationTypeEnum type;

    @NotNull
    private Long entityId;

    //    private Long actionId;

    private String actionKey;
}
