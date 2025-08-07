package com.ProyectoIntegradorBE.proaudioBE.repositories.specifications;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectSpecification {

    public static Specification<ProjectEntity> filterBy(List<ProjectStatusEnum> statuses) {
        return Specification.where(statusIn(statuses));
    }

    public static Specification<ProjectEntity> filterBy(List<ProjectStatusEnum> statuses, String paymentStatus,
                                                        String name) {
        return Specification.where(statusIn(statuses)).and(paymentStatusEquals(paymentStatus)).and(nameContains(name));
    }

    public static Specification<ProjectEntity> filterByStatusAndName(List<ProjectStatusEnum> statuses, String name) {
        return Specification.where(statusIn(statuses)).and(nameContains(name));
    }

    public static Specification<ProjectEntity> filterByStatusAndPaymentStatus(List<ProjectStatusEnum> statuses,
                                                                              String paymentStatus) {
        return Specification.where(statusIn(statuses)).and(paymentStatusEquals(paymentStatus));
    }

    private static Specification<ProjectEntity> statusIn(List<ProjectStatusEnum> statuses) {
        List<String> statusesString = statuses.stream().map(Enum::name).toList();
        return (root, query, cb) -> {
            CriteriaBuilder.In<String> inClause = cb.in(root.get("status"));
            for (String status : statusesString) {
                inClause.value(status);
            }
            return inClause;
        };
    }

    private static Specification<ProjectEntity> paymentStatusEquals(String paymentStatus) {
        return (root, query, cb) -> cb.equal(root.get("paymentStatus"), paymentStatus);
    }

    private static Specification<ProjectEntity> nameContains(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

}
