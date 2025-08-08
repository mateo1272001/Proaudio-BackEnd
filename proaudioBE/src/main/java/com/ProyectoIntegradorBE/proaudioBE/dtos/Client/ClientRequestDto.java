package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientRequestDto {

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

}
