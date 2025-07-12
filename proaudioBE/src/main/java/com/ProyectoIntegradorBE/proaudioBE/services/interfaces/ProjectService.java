package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectStatusesDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectTypesResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto request);

    ProjectTypesResponseDto getProjectTypes();

    ProjectStatusesDto getPossibleStatusByProjectId(Long id);

    ProjectStatusesDto getPossibleStatusForStartingProject();
}
