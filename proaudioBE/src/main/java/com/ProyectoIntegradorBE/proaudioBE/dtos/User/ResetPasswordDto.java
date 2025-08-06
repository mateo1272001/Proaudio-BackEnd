package com.ProyectoIntegradorBE.proaudioBE.dtos.User;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResetPasswordDto {

    private String token;

    private String newPassword;

    private String newPasswordRepeat;

}