package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientResponseDto {

    private Long clientId;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

    private BasicEnumStatus status;

}
