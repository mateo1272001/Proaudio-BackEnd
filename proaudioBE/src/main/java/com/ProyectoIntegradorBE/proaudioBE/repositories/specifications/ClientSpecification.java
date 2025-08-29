package com.ProyectoIntegradorBE.proaudioBE.repositories.specifications;

import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {

    public static Specification<ClientEntity> filterBy(BasicEnumStatus status) {
        return Specification.where(statusEquals(status));
    }

    public static Specification<ClientEntity> filterByStatusAndName(BasicEnumStatus statuses, String name) {
        return Specification.where(statusEquals(statuses)).and(nameContains(name));
    }

    private static Specification<ClientEntity> statusEquals(BasicEnumStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    private static Specification<ClientEntity> nameContains(String term) {

        return (root, query, cb) -> {

            String pattern = "%" + term.trim().toLowerCase() + "%";

            Predicate nameLike = cb.like(cb.lower(root.get("name")), pattern);
            Predicate phoneLike = cb.like(cb.lower(root.get("phoneNumber")), pattern);
            Predicate emailLike = cb.like(cb.lower(root.get("email")), pattern);
            Predicate addressLike = cb.like(cb.lower(root.get("address")), pattern);

            return cb.or(nameLike, phoneLike, emailLike, addressLike);

        };

    }


}
