package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import lombok.Data;

@Data
public class ClientResponseDto {

    private Long id;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

}
