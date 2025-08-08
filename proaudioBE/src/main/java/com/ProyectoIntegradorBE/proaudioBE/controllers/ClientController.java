package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ClientResponseDto createClient(@RequestBody ClientRequestDto clientRequestDto) {

        return clientService.createClient(clientRequestDto);

    }

    @PutMapping("{id}")
    public ClientResponseDto updateClient(@PathVariable Long id, @RequestBody ClientRequestDto clientRequestDto) {

        return clientService.updateClient(id, clientRequestDto);

    }

    @DeleteMapping("{id}")
    public ClientResponseDto updateClient(@PathVariable Long id) {

        return clientService.deleteClient(id);

    }

    @GetMapping("/{id}")
    public ClientResponseDto GetClientById(@PathVariable Long id) {

        return clientService.getClientById(id);

    }

}
