package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseIntDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.NextProjectInfoDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
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

    @Query(value = """
            SELECT *
            FROM item_project ip
            WHERE ip.item_id = :idItem
            ORDER BY 1 DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<ItemProjectEntity> findLastProjectOfItem(Long idItem);

    @Query(value = """
            SELECT
                  ip.item_id AS itemId,
                  p.project_id AS projectId,
                  p.name AS projectName,
                  p.start_date AS projectStartDate
            FROM item_project ip
            JOIN project p ON ip.project_id = p.project_id
            JOIN (
                SELECT ip2.item_id, MIN(p2.start_date) AS min_start
                FROM item_project ip2
                JOIN project p2 ON ip2.project_id = p2.project_id
                WHERE ip2.item_id IN (:itemIds)
                AND p2.end_date > NOW()
                GROUP BY ip2.item_id
            ) AS min_projects ON ip.item_id = min_projects.item_id AND p.start_date = min_projects.min_start
            """, nativeQuery = true)
    List<NextProjectInfoDto> findNextProjectInfoFromItems(List<Long> itemIds);


    @Query(value = """
            SELECT p.*
            FROM item_project ip
            INNER JOIN project p ON (p.project_id = ip.project_id AND p.status in ('PLANNED', 'CONFIRMED'))
            WHERE ip.item_id = :itemId
            """, nativeQuery = true)
    List<ProjectEntity> findNextPlannedProjectsForItem(Long itemId);
}
