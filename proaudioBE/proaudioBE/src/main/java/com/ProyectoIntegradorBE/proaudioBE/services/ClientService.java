package com.ProyectoIntegradorBE.proaudioBE.services;

import dtos.Client.ClientResponseDto;

public interface ClientService {

    public ClientResponseDto getClientById(Long id);

}
