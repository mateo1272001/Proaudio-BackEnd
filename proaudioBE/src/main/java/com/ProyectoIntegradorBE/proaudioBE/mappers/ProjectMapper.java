package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectSimpleReponseDto toDto(ProjectEntity projectEntity);

    List<ProjectSimpleReponseDto> toDtoList(List<ProjectEntity> projectEntity);

}
