package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ExpenseEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends CrudRepository<ExpenseEntity, Long> {
    Optional<ExpenseEntity> findByExpenseId(Long id);

    Optional<ExpenseEntity> findByExpenseIdAndStatus(Long id, BasicEnumStatus basicEnumStatus);

    List<ExpenseEntity> findByProjectIdAndStatus(Long id, BasicEnumStatus basicEnumStatus);
}
