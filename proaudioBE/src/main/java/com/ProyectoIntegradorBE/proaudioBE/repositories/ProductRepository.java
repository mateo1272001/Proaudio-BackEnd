package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.RentedProductsAmountResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long>, ProductRepositoryCustom {

    List<ProductEntity> findByProductIdInAndStatus(List<Long> productIds, ProductStatus status);

    @Query(value = """
            SELECT products.product_id, products.model, products.amount, t.name as brand
            FROM
            (
            	SELECT pd.product_id, pd.model, count(pd.product_id) as amount
            	FROM project p
            	INNER JOIN product_project pp ON (p.project_id = pp.project_id AND pp.status = 'ENABLED')
            	INNER JOIN product pd ON (pp.product_id = pd.product_id)
            	WHERE p.status NOT IN ('DISCARDED', 'PLANNED') AND
                	((:start is NULL OR p.start_date >= :start) AND ((:end is NULL OR p.start_date >= :end) OR p.start_date <= :end))
            	    OR ((:start is NULL OR p.end_date >= :start) AND (:end is NULL OR p.end_date <= :end))
            	GROUP BY pd.product_id, pd.model
            ) AS products
            LEFT JOIN product_tag pt ON (products.product_id = pt.product_id)
            INNER JOIN tag t ON (t.tag_id = pt.tag_id)
            WHERE t.father_id = 1
            ORDER BY products.amount
            LIMIT :limit
            """, nativeQuery = true)
    List<RentedProductsAmountResponseDto> findMostUsedProducts(LocalDate start, LocalDate end, Integer limit);
}
