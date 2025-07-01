package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PriceRequestDto {

    @NotNull
    private BigDecimal value;

    @NotNull
    private String description;

    @Nullable
    private Long rentPriceId;

    @Nullable
    private BasicEnumStatus status;

    @Nullable
    private Long productId;

}
