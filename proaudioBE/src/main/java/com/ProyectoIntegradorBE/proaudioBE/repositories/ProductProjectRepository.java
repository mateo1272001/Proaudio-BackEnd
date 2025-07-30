package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
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


    @Query(value = """
            SELECT 
                p.product_id AS id,
                p.model,
                p.comments,
                pp.amount,
                rp.value AS rent_price,
                pp.product_project_id
            FROM product_project pp
            JOIN product p ON pp.product_id = p.product_id
            LEFT JOIN rent_price rp ON pp.rent_price_id = rp.rent_price_id
            WHERE pp.project_id = :projectId
              AND pp.status = 'ENABLED'
            """, nativeQuery = true)
    List<ProductInProjectResponseDto> findProductProjectDetail(Long projectId);

    Optional<ProductProjectEntity> findByProductIdAndProjectIdAndStatus(Long productId, Long projectId,
                                                                        BasicEnumStatus basicEnumStatus);

}
