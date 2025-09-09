package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectProductRequestDto {

    private Long productId;

    private Long priceId;

    private Integer amount;

}
