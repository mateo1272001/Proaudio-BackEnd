package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto request);

    ProjectSimpleReponseDto updateProject(Long id, ProjectRequestDto request);

    ProjectSimpleReponseDto getProject(Long id);

    ProjectTypesResponseDto getProjectTypes();

    ProjectStatusesDto getPossibleStatusByProjectId(Long id);

    ProjectStatusesDto getPossibleStatusForStartingProject();

    void updateProjectStatusAutomatically();
}
