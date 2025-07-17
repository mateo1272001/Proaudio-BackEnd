package com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductProjectRequestDto {

    @Nullable
    private Long productProjectId;

    @NotNull
    private Long productId;

    @NotNull
    private Long projectId;

    @NotNull
    private Long rentPriceId;

    @NotNull
    private Integer amount;

    private BasicEnumStatus status = BasicEnumStatus.ENABLED;

}
