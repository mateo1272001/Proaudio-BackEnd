package com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BalanceDto {

    private BigDecimal expenses = BigDecimal.ZERO;

    private BigDecimal earnings = BigDecimal.ZERO;

    private BigDecimal balance = BigDecimal.ZERO;

}
