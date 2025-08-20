package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationListResponseDto {

    private List<NotificationRowDto> notifications;

    private PageableDto pageable;

}
