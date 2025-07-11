package com.ProyectoIntegradorBE.proaudioBE.dtos.Event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventRequestDto {

    private Long eventId;

    private String name;

    private String address;

    private Double distance;

    private String description;

    private String status;

}
