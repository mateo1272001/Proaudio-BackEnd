package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;

public interface ItemProjectService {
    ItemProjectResponseDto createItemProject(ItemResponseDto idItem, ProjectSimpleReponseDto idProject);


    ItemProjectResponseDto changeStatusItemProject(ItemResponseDto itemResponseDto,
                                                   ProjectSimpleReponseDto projectSimpleReponseDto,
                                                   ItemProjectStatus itemProjectStatus);

}
