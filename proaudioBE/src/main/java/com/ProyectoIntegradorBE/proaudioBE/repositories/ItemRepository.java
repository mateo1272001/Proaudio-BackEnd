package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long>, JpaRepository<ItemEntity, Long>,
        JpaSpecificationExecutor<ItemEntity> {

    List<ItemEntity> findByProductIdInAndStatusIn(List<Long> productIds, List<ItemStatusEnum> itemStatusEnums);

    List<ItemEntity> findByProductIdAndStatusIn(Long productId, List<ItemStatusEnum> itemStatusEnums);

    Optional<ItemEntity> findByItemId(Long id);

    @Query(value = """
            SELECT i.*
            FROM item i
            INNER JOIN item_project ip ON (i.item_id = ip.item_id)
            WHERE ip.project_id = :projectId AND ip.status = :itemProjectStatus AND i.status IN (:itemStatuses)
            """, nativeQuery = true)
    List<ItemEntity> findByProjectIdAndStatus(Long projectId, String itemProjectStatus, List<String> itemStatuses);

    @Query(value = """
            SELECT *
            FROM item i
            WHERE i.product_id = :productId AND i.status in (:statuses) AND i.serial_number in (:serialNumbers)
            """, nativeQuery = true)
    List<ItemEntity> findRepeatedSerialNumbers(@NotNull Long productId, List<ItemStatusEnum> statuses,
                                               List<String> serialNumbers);
}
