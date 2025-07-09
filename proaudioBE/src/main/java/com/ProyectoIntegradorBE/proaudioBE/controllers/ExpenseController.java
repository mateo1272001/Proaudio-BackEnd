package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    private ExpenseResponseDto CreateExpense(@Valid @RequestBody ExpenseRequestDto expenseRequestDto) {

        return expenseService.CreateExpense(expenseRequestDto);

    }


    @PutMapping("{id}")
    private ExpenseResponseDto UpdateExpense(@PathVariable Long id, @RequestBody ExpenseRequestDto expenseRequestDto) {

        return expenseService.UpdateExpense(id, expenseRequestDto);

    }

    @DeleteMapping("{id}")
    private ExpenseResponseDto DeleteExpense(@PathVariable Long id) {

        return expenseService.DeleteExpense(id);

    }

    @GetMapping("{id}")
    private ExpenseResponseDto GetExpense(@PathVariable Long id) {

        return expenseService.GetExpense(id);

    }

    @GetMapping("/project/{id}")
    private ExpenseResponseListDto GetExpensesByProject(@PathVariable Long id) {

        return expenseService.GetExpensesByProject(id);

    }


}
