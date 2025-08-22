package com.ProyectoIntegradorBE.proaudioBE.dtos.Action;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActionResponseDto {

    private Long actionId;

    private String actionKey;

    private String description;

}
