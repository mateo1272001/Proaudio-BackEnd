package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping()
    private ProjectResponseDto createProject(@RequestBody ProjectRequestDto request) {

        return projectService.createProject(request);
    }

    @PutMapping("{id}")
    private ProjectSimpleReponseDto updateProject(@PathVariable Long id, @RequestBody ProjectRequestDto request) {

        return projectService.updateProject(id, request);
    }

    @GetMapping("{id}")
    private ProjectSimpleReponseDto getProject(@PathVariable Long id) {

        return projectService.getProject(id);
    }

    @GetMapping("/types")
    private ProjectTypesResponseDto getProjectTypes() {
        return projectService.getProjectTypes();
    }

    @GetMapping("/possible/status/{id}")
    private ProjectStatusesDto getPossibleStatusByProjectId(@PathVariable Long id) {
        return projectService.getPossibleStatusByProjectId(id);
    }

    @GetMapping("/possible/status")
    private ProjectStatusesDto getPossibleStatusForStartingProject() {
        return projectService.getPossibleStatusForStartingProject();
    }

    @GetMapping("/possible/payment/status/{id}")
    private ProjectPaymentStatusesDto getPossiblePaymentStatusByProjectId(@PathVariable Long id) {
        return projectService.getPossiblePaymentStatusByProjectId(id);
    }
    //todo [PROJECT] create possible payment endpoints

    @GetMapping("/details/{id}")
    private ProjectDetailsResponseDto getProjectDetails(@PathVariable Long id) {
        return projectService.getProjectDetails(id);
    }
}
