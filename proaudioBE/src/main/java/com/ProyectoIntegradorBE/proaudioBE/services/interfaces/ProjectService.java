package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ProjectParticipatedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemSectionResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    ProjectStatusesDto getAllStatus();

    ProjectPaymentStatusesDto getAllPaymentStatus();

    RunningStatusResponseDto getAllRunningEnum();

    byte[] generateBudget(Long id);

    String obtainProductDependenciesAndValidate(String productName, ProjectProductRequestDto productRequest,
                                                List<ProductTagEntity> productTagsInProject, Boolean isUpdate);

    String validateDependenciesUpdate(String productName, ProductProjectRequestDto productProjectRequestDto,
                                      List<ProductTagEntity> productTagsInProject);

    ItemProjectResponseDto singleItemExit(Long idProject, Long idItem);

    @Transactional
    ItemProjectResponseDto singleItemReturnWithProjectId(Long idProject, Long idItem);

    ItemProjectResponseDto singleItemReturn(ItemProjectResponseDto itemProjectResponseDto);

    List<ProjectParticipatedResponseDto> getProjectsByClient(Long id);

    ItemSectionResponseDto getItemListFrom(Long productId, String status, String sortBy, String direction, Integer page,
                                           Integer size);

}
