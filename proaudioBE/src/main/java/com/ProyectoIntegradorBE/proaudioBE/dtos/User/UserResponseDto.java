package com.ProyectoIntegradorBE.proaudioBE.dtos.User;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDto {

    private Long userId;

    private String email;

    private String name;

    private String password;

    private String phone_number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BasicEnumStatus status;

}
