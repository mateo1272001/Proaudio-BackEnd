package com.ProyectoIntegradorBE.proaudioBE.dtos.User;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRegisterRequestDto {

    private String email;

    private String name;

    private String password;

    private String phoneNumber;
}