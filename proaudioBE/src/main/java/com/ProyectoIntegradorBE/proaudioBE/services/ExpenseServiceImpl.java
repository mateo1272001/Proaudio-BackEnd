package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseTypeListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ExpenseEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ExpenseTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ExpenseMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ExpenseRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseResponseDto CreateExpense(ExpenseRequestDto expenseRequestDto) {

        //todo [PROJECT] validate project id

        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setProjectId(expenseRequestDto.getProjectId());
        expenseEntity.setType(Objects.nonNull(expenseRequestDto.getType()) ? expenseRequestDto.getType() :
                ExpenseTypeEnum.EXTRA_COST);
        expenseEntity.setValue(expenseRequestDto.getValue());
        if (Objects.nonNull(expenseRequestDto.getDescription())) {
            expenseEntity.setDescription(expenseRequestDto.getDescription());
        }
        expenseEntity.setStatus(BasicEnumStatus.ENABLED);

        expenseEntity = expenseRepository.save(expenseEntity);

        return expenseMapper.toDto(expenseEntity);
    }

    @Override
    public ExpenseResponseDto UpdateExpense(Long id, ExpenseRequestDto expenseRequestDto) {

        ExpenseEntity expenseEntity = expenseRepository.findByExpenseId(id)
                .orElseThrow(() -> new BadRequestException("¡No existe costo con ese id!"));

        expenseEntity.setType(expenseRequestDto.getType());
        expenseEntity.setValue(expenseRequestDto.getValue());
        expenseEntity.setDescription(expenseRequestDto.getDescription());

        expenseEntity = expenseRepository.save(expenseEntity);

        return expenseMapper.toDto(expenseEntity);
    }

    @Override
    public ExpenseResponseDto DeleteExpense(Long id) {

        ExpenseEntity expenseEntity = expenseRepository.findByExpenseIdAndStatus(id, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new BadRequestException("¡No existe costo con ese id!"));

        expenseEntity.setStatus(BasicEnumStatus.DISABLED);

        expenseRepository.save(expenseEntity);

        return expenseMapper.toDto(expenseEntity);
    }

    @Override
    public ExpenseResponseDto GetExpense(Long id) {

        ExpenseEntity expenseEntity = expenseRepository.findByExpenseIdAndStatus(id, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new BadRequestException("¡No existe costo con ese id!"));

        return expenseMapper.toDto(expenseEntity);
    }

    @Override
    public ExpenseResponseListDto GetExpensesByProject(Long id) {

        List<ExpenseEntity> expenseEntities = expenseRepository.findByProjectIdAndStatus(id, BasicEnumStatus.ENABLED);

        return new ExpenseResponseListDto(expenseMapper.toDtoList(expenseEntities));
    }

    @Override
    public ExpenseTypeListResponseDto GetExpenseTypes() {

        return new ExpenseTypeListResponseDto(Arrays.stream(ExpenseTypeEnum.values()).toList());
    }


}
