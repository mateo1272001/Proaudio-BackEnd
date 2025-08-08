package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;

public interface ClientService {

    public ClientResponseDto getClientById(Long id);

    ClientResponseDto createClient(ClientRequestDto clientRequestDto);

    ClientResponseDto updateClient(Long id, ClientRequestDto clientRequestDto);

    ClientResponseDto deleteClient(Long id);

    ClientListResponseDto getClientList(String sortBy, String direction, Integer page, Integer size, String status);
}
