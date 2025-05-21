package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;

public interface ClientService {

    public ClientResponseDto getClientById(Long id);

}
