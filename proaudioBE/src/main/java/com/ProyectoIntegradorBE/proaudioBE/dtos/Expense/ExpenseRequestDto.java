package com.ProyectoIntegradorBE.proaudioBE.dtos.Expense;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ExpenseTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpenseRequestDto {

    @Nullable
    private Long expenseId;
    
    private Long projectId;

    @NotNull
    private ExpenseTypeEnum type;

    @NotNull
    private BigDecimal value;

    private String description;

    @Nullable
    private BasicEnumStatus status;

}
