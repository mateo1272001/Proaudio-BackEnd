package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "SELECT t.* " +
            "FROM proaudio_channels.tag t " +
            "INNER JOIN proaudio_channels.product_tag pt ON (t.tag_id = pt.tag_id) " +
            "WHERE pt.product_id = :productId " +
            "AND t.father_id = :fatherId " +
            "ORDER BY 1 " +
            "DESC " +
            "LIMIT 1;",
            nativeQuery = true)
    List<TagEntity> findByProductIdAndFatherId(Long productId, Long fatherId);

    Optional<TagEntity> findByNameAndStatus(String brandTagKey, BasicEnumStatus basicEnumStatus);
}
