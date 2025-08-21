package com.ProyectoIntegradorBE.proaudioBE.repositories.specifications;

import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
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

    private static Specification<ClientEntity> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }


}
