package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseListDto;

public interface ExpenseService {
    ExpenseResponseDto CreateExpense(ExpenseRequestDto expenseRequestDto);

    ExpenseResponseDto UpdateExpense(Long id, ExpenseRequestDto expenseRequestDto);

    ExpenseResponseDto DeleteExpense(Long id);

    ExpenseResponseDto GetExpense(Long id);

    ExpenseResponseListDto GetExpensesByProject(Long id);
}
