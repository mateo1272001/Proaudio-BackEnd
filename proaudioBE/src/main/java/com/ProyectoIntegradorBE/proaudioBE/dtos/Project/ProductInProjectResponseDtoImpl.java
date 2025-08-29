package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductInProjectResponseDtoImpl {

    private Long productProjectId;

    private Long id;

    private String model;

    private String comments;

    private Integer amount;

    private Long rentPriceId;

    private BigDecimal rentPriceValue;

}
