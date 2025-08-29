package com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AmountRentedResponseDto {

    private RangeDto range;

    private List<RentedProductsAmountDto> products;

}
