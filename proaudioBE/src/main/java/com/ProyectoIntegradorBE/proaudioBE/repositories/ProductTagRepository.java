package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTagRepository extends CrudRepository<ProductTagEntity, Long> {

    List<ProductTagEntity> findByProductId(Long productId);

    Optional<ProductTagEntity> findByProductIdAndTagId(Long productId, Long tagId);

    Optional<ProductTagEntity> findByProductTagIdAndStatus(Long id, BasicEnumStatus basicEnumStatus);

    Optional<ProductTagEntity> findByTagIdAndProductIdAndStatus(Long tagId, Long productId,
                                                                BasicEnumStatus basicEnumStatus);
}
