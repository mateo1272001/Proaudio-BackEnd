package com.ProyectoIntegradorBE.proaudioBE.dtos.User;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class LoginResponseDto {

    private Long userId;

    private String email;

    private String name;

    private String token;

}
