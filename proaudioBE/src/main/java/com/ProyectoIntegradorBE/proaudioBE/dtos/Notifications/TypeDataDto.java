package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TypeDataDto {

    private String title;

    private String value;

}
