package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponseDto {

    private Long productId;

    private String model;

    private String comments;

    private ProductStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BigDecimal replacementValue;

    private List<PriceReponseDto> prices;

    private List<PhotoResponseDto> photos;

    private List<ProductTagResponseDto> tags;


}
