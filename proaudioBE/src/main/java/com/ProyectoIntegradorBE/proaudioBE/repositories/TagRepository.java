package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<TagEntity, Long> {

    List<TagEntity> findByFatherIdIsNullAndStatus(BasicEnumStatus status);

    List<TagEntity> findByFatherIdAndStatus(Long tagId, BasicEnumStatus status);

    List<TagEntity> findAllByStatus(BasicEnumStatus status);

    List<TagEntity> findByTagIdIn(List<Long> tagIds);

    Optional<TagEntity> findByNameAndStatus(String brandTagKey, BasicEnumStatus basicEnumStatus);
}
