package com.ProyectoIntegradorBE.proaudioBE.repositories.specifications;

import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    public static Specification<ItemEntity> filterBy(Long productId, ItemStatusEnum status) {
        return Specification.where(productIdEquals(productId)).and(statusEquals(status));
    }

    private static Specification<ItemEntity> productIdEquals(Long productId) {
        return (root, query, cb) -> productId == null ? null : cb.equal(root.get("productId"), productId);
    }

    private static Specification<ItemEntity> statusEquals(ItemStatusEnum status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }
}
