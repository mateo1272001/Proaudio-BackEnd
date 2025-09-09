package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientRequestDto {

    @Nullable
    private Long clientId;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

}
