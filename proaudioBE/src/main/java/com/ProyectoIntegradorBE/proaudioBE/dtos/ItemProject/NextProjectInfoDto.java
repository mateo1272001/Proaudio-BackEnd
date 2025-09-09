package com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface NextProjectInfoDto {

    Long getItemId();

    Long getProjectId();

    String getProjectName();

    LocalDateTime getProjectStartDate();

}
