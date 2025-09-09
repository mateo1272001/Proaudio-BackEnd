package com.ProyectoIntegradorBE.proaudioBE.repositories.specifications;

import com.ProyectoIntegradorBE.proaudioBE.entities.EventEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class EventSpecification {

    public static Specification<EventEntity> filterBy(BasicEnumStatus status, String term) {

        if (Objects.isNull(term)) {
            return Specification.where(statusEquals(status));
        } else {
            return Specification.where(statusEquals(status)).and(searchByNameAddressOrDistance(term));
        }

    }

    public static Specification<EventEntity> searchByNameAddressOrDistance(String term) {
        return (root, query, cb) -> {

            if (term == null || term.isBlank()) {
                return null;
            }

            String pattern = "%" + term.trim().toLowerCase() + "%";

            Predicate nameLike = cb.like(cb.lower(root.get("name")), pattern);
            Predicate addressLike = cb.like(cb.lower(root.get("address")), pattern);

            return cb.or(nameLike, addressLike);
        };
    }

    private static Specification<EventEntity> statusEquals(BasicEnumStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

}
