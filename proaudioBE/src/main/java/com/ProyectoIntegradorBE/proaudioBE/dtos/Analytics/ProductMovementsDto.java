package com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductMovementsDto {

    private Long itemId;

    private BigDecimal amount;

    private LocalDate date;

    private String action;

}
