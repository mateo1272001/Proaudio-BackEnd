package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class NotificationTypesResponseDto {

    private List<NotificationTypeEnum> types;

}
