package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProductProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductProjectRepository extends CrudRepository<ProductProjectEntity, Long> {
    List<ProductProjectEntity> findByProjectIdAndStatus(Long projectId, BasicEnumStatus status);

    Optional<ProductProjectEntity> findByProductProjectIdAndStatus(Long id, BasicEnumStatus basicEnumStatus);

    Optional<ProductProjectEntity> findByProjectIdAndProductIdAndRentPriceId(@NotNull Long projectId,
                                                                             @NotNull Long productId,
                                                                             @NotNull Long rentPriceId);

    List<ProductProjectEntity> findByProjectIdAndProductIdAndStatus(Long productProjectId, @NotNull Long productId,
                                                                    BasicEnumStatus basicEnumStatus);
}
