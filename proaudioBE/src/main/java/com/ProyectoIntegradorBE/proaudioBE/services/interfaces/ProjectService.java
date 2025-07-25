package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto request);

    ProjectSimpleReponseDto updateProject(Long id, ProjectRequestDto request);

    ProjectSimpleReponseDto getProject(Long id);

    ProjectTypesResponseDto getProjectTypes();

    ProjectStatusesDto getPossibleStatusByProjectId(Long id);

    ProjectStatusesDto getPossibleStatusForStartingProject();

    void updateProjectStatusAutomatically();

    ProjectDetailsResponseDto getProjectDetails(Long id);

    ProjectPaymentStatusesDto getPossiblePaymentStatusByProjectId(Long id);

    ProjectListResponseDto getProjectList(Integer page, Integer size, String sortBy, String direction,
                                          List<String> filterStatus, String filterPaymentStatus, String name);
}
