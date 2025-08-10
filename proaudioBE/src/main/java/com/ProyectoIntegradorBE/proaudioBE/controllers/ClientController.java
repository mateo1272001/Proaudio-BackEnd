package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.*;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ClientService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    private final ProjectService projectService;

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
    public ClientResponseDto getClientById(@PathVariable Long id) {

        return clientService.getClientById(id);

    }

    @GetMapping("/list")
    public ClientListResponseDto getClientList(@RequestParam(required = false, defaultValue = "id") String sortBy,
                                               @RequestParam(required = false) String direction,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String status) {

        return clientService.getClientList(sortBy, direction, page, size, status);

    }

    @GetMapping("/{id}/details")
    public ClientDetailsResponseDto getClientDetails(@PathVariable Long id) {

        ClientResponseDto clientResponseDto = clientService.getClientById(id);

        List<ProjectParticipatedResponseDto> projectParticipatedResponseDtos = projectService.getProjectsByClient(id);

        return clientService.getClientDetails(clientResponseDto, projectParticipatedResponseDtos);

    }

}
