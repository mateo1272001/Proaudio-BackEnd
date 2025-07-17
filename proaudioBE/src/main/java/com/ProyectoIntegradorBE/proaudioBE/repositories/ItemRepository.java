package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long>, JpaRepository<ItemEntity, Long>,
        JpaSpecificationExecutor<ItemEntity> {

    List<ItemEntity> findByProductIdInAndStatusIn(List<Long> productIds, List<ItemStatusEnum> itemStatusEnums);

    List<ItemEntity> findByProductIdAndStatusIn(Long productId, List<ItemStatusEnum> itemStatusEnums);
}
