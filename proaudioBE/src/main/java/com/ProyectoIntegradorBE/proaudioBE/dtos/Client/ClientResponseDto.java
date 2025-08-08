package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import lombok.Data;

@Data
public class ClientResponseDto {

    private Long clientId;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

    private BasicEnumStatus status;

}
