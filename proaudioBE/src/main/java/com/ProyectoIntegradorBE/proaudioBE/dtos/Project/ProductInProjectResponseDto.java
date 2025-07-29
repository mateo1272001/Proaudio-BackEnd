package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ProductInProjectResponseDto {
    Long getId();

    String getModel();

    String getComments();

    Integer getAmount();

    BigDecimal getRentPrice();

    Long getProductProjectId();
}
