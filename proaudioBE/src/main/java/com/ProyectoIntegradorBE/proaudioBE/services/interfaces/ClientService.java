package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;

public interface ClientService {

    public ClientResponseDto getClientById(Long id);

}
