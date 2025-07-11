package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    ProjectResponseDto CreateProject(ProjectRequestDto request);
}
