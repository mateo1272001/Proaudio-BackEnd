package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.RelationGroupEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationGroupRepository extends CrudRepository<RelationGroupEntity, Long> {

    Optional<RelationGroupEntity> findByRelationGroupId(Long relationGroupId);

    List<RelationGroupEntity> findByProductIdAndTypeAndStatus(Long productId, @NotNull TagTypeEnum type,
                                                              BasicEnumStatus basicEnumStatus);

    List<RelationGroupEntity> findByProductIdAndStatus(Long productId, BasicEnumStatus basicEnumStatus);
}
