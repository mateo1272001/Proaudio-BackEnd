package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.*;

import java.util.List;

public interface ClientService {

    public ClientResponseDto getClientById(Long id);

    ClientResponseDto createClient(ClientRequestDto clientRequestDto);

    ClientResponseDto updateClient(Long id, ClientRequestDto clientRequestDto);

    ClientResponseDto deleteClient(Long id);

    ClientListResponseDto getClientList(String sortBy, String direction, Integer page, Integer size, String status);

    ClientDetailsResponseDto getClientDetails(ClientResponseDto clientResponseDto,
                                              List<ProjectParticipatedResponseDto> projectParticipatedResponseDto);
}
