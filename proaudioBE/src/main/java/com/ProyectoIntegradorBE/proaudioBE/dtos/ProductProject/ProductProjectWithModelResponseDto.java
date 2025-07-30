package com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;

import java.math.BigDecimal;

public interface ProductProjectWithModelResponseDto {

    Long getProductProjectId();

    Long getProductId();

    Long getProjectId();

    Integer getAmount();

    BasicEnumStatus getStatus();

    BigDecimal getPrice();

    String getModel();

}
