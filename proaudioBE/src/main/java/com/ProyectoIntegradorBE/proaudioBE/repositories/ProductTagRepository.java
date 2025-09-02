package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTagRepository extends CrudRepository<ProductTagEntity, Long> {

    List<ProductTagEntity> findByProductId(Long productId);

    //    @Query(value = """
    //            SELECT t.*
    //            FROM product_tag pt
    //            INNER JOIN tag t ON (pt.tag_id = t.tag_id AND t.status = 'ENABLED' AND pt.status = 'ENABLED')
    //            """)
    //    List<TagEntity> findTagByProductId(Long productId);

    @Query(value = """
            SELECT t.* 
            FROM proaudio_channels.tag t 
            INNER JOIN proaudio_channels.product_tag pt ON (t.tag_id = pt.tag_id) 
            WHERE pt.product_id = :productId AND t.father_id = :fatherId AND pt.type = 'DESCRIPTIVE'
            """, nativeQuery = true)
    List<TagEntity> findByProductIdAndFatherId(Long productId, Long fatherId);

    Optional<ProductTagEntity> findByProductIdAndTagId(Long productId, Long tagId);

    Optional<ProductTagEntity> findByTagIdAndProductIdAndStatus(Long tagId, Long productId,
                                                                BasicEnumStatus basicEnumStatus);

    List<ProductTagEntity> findByProductIdAndStatus(Long productId, BasicEnumStatus basicEnumStatus);

    List<ProductTagEntity> findByProductIdInAndStatus(List<Long> productId, BasicEnumStatus basicEnumStatus);

    List<ProductTagEntity> findByTagIdAndStatus(Long tagId, BasicEnumStatus status);

    Optional<ProductTagEntity> findByProductIdAndTagIdAndType(Long productId, Long tagId, TagTypeEnum type);

    Optional<ProductTagEntity> findByTagIdAndProductIdAndTypeAndStatus(Long tagId, Long productId, TagTypeEnum typeEnum,
                                                                       BasicEnumStatus basicEnumStatus);
}
