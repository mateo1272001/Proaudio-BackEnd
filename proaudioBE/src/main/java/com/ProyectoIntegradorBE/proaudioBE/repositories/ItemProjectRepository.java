package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseIntDto;
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
                AND i.product_id = :productId AND ip.status = "ENABLED"
            """, nativeQuery = true)
    List<ItemProjectEntity> findItemsOfProductInProject(Long productId, Long projectId);

    Optional<ItemProjectEntity> findByItemIdAndProjectIdAndStatusIn(Long itemId, Long projectId,
                                                                    List<ItemProjectStatus> itemProjectStatus);

    @Query(value = """
            SELECT
            	ip.item_project_id,
            	i.item_id,
            	ip.project_id,
            	ip.status,
            	ip.created_at,
            	i.bought_at AS item_bought_at,
            	i.price_bought AS item_price_bought,
            	i.description AS item_description,
            	i.status AS item_status,
            	i.serial_number AS item_serial_number,
            	i.item_range,
            	i.location AS item_location,
            	p.product_id,
            	p.model AS product_model
            FROM item_project ip
            INNER JOIN item i ON (ip.item_id = i.item_id)
            INNER JOIN product p ON (p.product_id = i.product_id)
            WHERE ip.project_id = :projectId AND ip.status = :status;
            """, nativeQuery = true)
    List<ItemProjectResponseIntDto> findAllItemsInProject(Long projectId, String status);

    ItemProjectEntity findByItemIdAndProjectId(Long itemId, Long projectId);
}
