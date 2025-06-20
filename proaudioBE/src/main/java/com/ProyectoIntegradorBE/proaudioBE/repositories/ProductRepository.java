package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories.ProductRepositoryCustom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long>, ProductRepositoryCustom {

    List<ProductEntity> findByProductIdInAndStatus(List<Long> productIds, ProductStatus status);
}
