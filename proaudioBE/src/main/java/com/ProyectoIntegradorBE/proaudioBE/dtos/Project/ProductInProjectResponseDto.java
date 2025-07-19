package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import java.math.BigDecimal;

public interface ProductInProjectResponseDto {
    Long getId();

    String getModel();

    String getComments();

    Integer getAmount();

    BigDecimal getRentPrice();
}
