package com.ProyectoIntegradorBE.proaudioBE.dtos.Event;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventResponseDto {

    private Long eventId;

    private String name;

    private String address;

    private Double distance;

    private String description;

    private BasicEnumStatus status;

}
