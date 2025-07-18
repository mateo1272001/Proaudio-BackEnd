package com.ProyectoIntegradorBE.proaudioBE.mappers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ExpenseEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseResponseDto toDto(ExpenseEntity expenseEntity);

    List<ExpenseResponseDto> toDtoList(List<ExpenseEntity> expenseEntities);
}
