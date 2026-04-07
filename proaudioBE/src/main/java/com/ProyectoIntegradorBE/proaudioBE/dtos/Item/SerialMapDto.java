package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SerialMapDto {

    private String serialNumber;

    private String assignedId;

}
