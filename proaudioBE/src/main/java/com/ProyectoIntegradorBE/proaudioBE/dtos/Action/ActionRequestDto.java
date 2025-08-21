package com.ProyectoIntegradorBE.proaudioBE.dtos.Action;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActionRequestDto {

    private String actionKey;

    private String description;

}
