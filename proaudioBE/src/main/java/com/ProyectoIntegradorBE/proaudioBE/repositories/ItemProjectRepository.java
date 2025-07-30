package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ItemProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemProjectRepository extends CrudRepository<ItemProjectEntity, Long> {

    @Query(value = """
                SELECT ip.* 
                FROM item_project ip
                INNER JOIN item i on (ip.item_id = i.item_id)
                WHERE ip.project_id = :projectId
                AND i.product_id = :productId
            """, nativeQuery = true)
    List<ItemProjectEntity> findItemsOfProductInProject(Long productId, Long projectId);

    Optional<ItemProjectEntity> findByItemIdAndProjectIdAndStatusIn(Long itemId, Long projectId,
                                                                    List<ItemProjectStatus> itemProjectStatus);
}
