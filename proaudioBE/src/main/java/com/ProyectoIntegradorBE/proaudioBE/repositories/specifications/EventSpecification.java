package com.ProyectoIntegradorBE.proaudioBE.repositories.specifications;

import com.ProyectoIntegradorBE.proaudioBE.entities.EventEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {

    public static Specification<EventEntity> filterBy(BasicEnumStatus status) {
        return Specification.where(statusEquals(status));
    }

    private static Specification<EventEntity> statusEquals(BasicEnumStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

}
