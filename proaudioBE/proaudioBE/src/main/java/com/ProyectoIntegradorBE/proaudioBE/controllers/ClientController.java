package com.ProyectoIntegradorBE.proaudioBE.controllers;

import dtos.Client.ClientResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ProyectoIntegradorBE.proaudioBE.services.ClientServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientServiceImpl clientServiceImpl;

    @GetMapping("/{id}")
    public ClientResponseDto GetClientById(@PathVariable Long id) throws Exception {
        return clientServiceImpl.getClientById(id);
    }

}
