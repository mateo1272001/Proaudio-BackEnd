package com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductProjectResponseDto {

    private Long productProjectId;

    private Long productId;

    private Long projectId;

    private Integer amount;

    private BasicEnumStatus status;

}
