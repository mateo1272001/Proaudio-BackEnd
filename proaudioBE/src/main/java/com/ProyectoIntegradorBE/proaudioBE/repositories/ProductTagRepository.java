package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductTagRepository extends CrudRepository<ProductTagEntity, Long> {
    List<ProductTagEntity> findByProductId(Long productId);
}
