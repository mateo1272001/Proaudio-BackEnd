package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectStatusesDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectTypesResponseDto;
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

}
