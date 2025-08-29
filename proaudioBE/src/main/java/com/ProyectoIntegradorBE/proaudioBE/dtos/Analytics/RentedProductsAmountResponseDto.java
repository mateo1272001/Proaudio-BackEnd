package com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RentedProductsAmountResponseDto {

    private Long productId;

    private String model;

    private Long amount;

    private String brand;

}
