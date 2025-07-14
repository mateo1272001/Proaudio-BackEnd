package com.ProyectoIntegradorBE.proaudioBE.dtos.Expense;

import com.ProyectoIntegradorBE.proaudioBE.enums.ExpenseTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ExpenseTypeListResponseDto {

    private List<ExpenseTypeEnum> types;

}
