package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.TagRelationGroupWithTagInfoDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagRelationGroupEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRelationGroupRepository extends CrudRepository<TagRelationGroupEntity, Long> {
    Optional<TagRelationGroupEntity> findByTagIdAndRelationGroupId(Long tagId, Long relationGroupId);

    @Query(value = """
            SELECT trg.tag_relation_group_id, t.tag_id, t.name, t.father_id
            FROM tag_relation_group trg
            INNER JOIN tag t ON (t.tag_id = trg.tag_id)
            WHERE trg.relation_group_id = :relationGroupId AND trg.status = 'ENABLED' AND t.status = 'ENABLED'
            """, nativeQuery = true)
    List<TagRelationGroupWithTagInfoDto> findByRelationGroupIdWithTagInfo(Long relationGroupId);

    List<TagRelationGroupEntity> findByRelationGroupIdAndStatus(Long relationGroupId, BasicEnumStatus status);

}
