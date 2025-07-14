package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProductProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductProjectRepository extends CrudRepository<ProductProjectEntity, Long> {
    List<ProductProjectEntity> findByProjectIdAndStatus(Long projectId, BasicEnumStatus status);
}
