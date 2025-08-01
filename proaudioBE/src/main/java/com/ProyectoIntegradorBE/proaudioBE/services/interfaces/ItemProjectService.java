package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectWithModelResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;

import java.util.List;

public interface ItemProjectService {
    ItemProjectResponseDto createItemProject(ItemResponseDto idItem, Long projectId,
                                             List<ProductProjectWithModelResponseDto> productProjectWithModelResponseDtos);


    ItemProjectResponseDto changeStatusItemProject(ItemResponseDto itemResponseDto,
                                                   ProjectSimpleReponseDto projectSimpleReponseDto,
                                                   ItemProjectStatus itemProjectStatus,
                                                   ItemProjectResponseDto itemProjectResponseDto);

    ItemProjectResponseListDto getItemsInProject(Long id);

    ItemProjectResponseDto getItemProjectByProjectIdAndItemId(Long idProject, Long idItem);

    void checkIfItemLimitIsSurpassed(ItemResponseDto itemResponseDto, Long projectId,
                                     List<ProductProjectWithModelResponseDto> productProjectWithModelResponseDtos,
                                     ItemProjectEntity itemProjectEntity);

}
