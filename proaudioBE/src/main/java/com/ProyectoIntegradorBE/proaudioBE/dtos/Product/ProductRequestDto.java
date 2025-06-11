package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRequestDto {

    @NotNull
    private String model;

    private String comments;

    private BigDecimal replacementValue;

    @NotNull
    private List<PriceRequestDto> prices;

    private List<PhotoRequestDto> photos;

    @NotNull
    private List<ProductTagRequestDto> tags;

    @Nullable
    private ProductStatus status;

}
