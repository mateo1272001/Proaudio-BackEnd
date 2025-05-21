package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.exceptions.ClientNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import lombok.RequiredArgsConstructor;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ClientMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ClientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDto getClientById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return clientMapper.toDto(clientEntity);
    }
}
