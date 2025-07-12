package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTagRepository extends CrudRepository<ProductTagEntity, Long> {

    List<ProductTagEntity> findByProductId(Long productId);

    Optional<ProductTagEntity> findByProductIdAndTagId(Long productId, Long tagId);

    Optional<ProductTagEntity> findByTagIdAndProductIdAndStatus(Long tagId, Long productId,
                                                                BasicEnumStatus basicEnumStatus);

    List<ProductTagEntity> findByProductIdAndStatus(Long productId, BasicEnumStatus basicEnumStatus);

    List<ProductTagEntity> findByTagIdAndStatus(Long tagId, BasicEnumStatus status);

    Optional<ProductTagEntity> findByProductIdAndTagIdAndType(Long productId, Long tagId, TagTypeEnum type);

    Optional<ProductTagEntity> findByTagIdAndProductIdAndTypeAndStatus(Long tagId, Long productId, TagTypeEnum typeEnum,
                                                                       BasicEnumStatus basicEnumStatus);
}
