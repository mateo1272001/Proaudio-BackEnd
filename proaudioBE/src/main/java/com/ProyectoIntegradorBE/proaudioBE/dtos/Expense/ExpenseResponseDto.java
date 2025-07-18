package com.ProyectoIntegradorBE.proaudioBE.dtos.Expense;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ExpenseTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpenseResponseDto {

    private Long expenseId;

    private Long projectId;

    private ExpenseTypeEnum type;

    private BigDecimal value;

    private String description;

    private BasicEnumStatus status;

}
