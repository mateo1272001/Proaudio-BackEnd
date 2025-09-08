package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.ProductMovementsProjection;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Analytics.RentedProductsAmountDto;
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
            LEFT JOIN product_tag pt ON (products.product_id = pt.product_id AND pt.type = 'DESCRIPTIVE' AND pt.status = 'ENABLED')
            LEFT JOIN tag t ON (t.tag_id = pt.tag_id AND t.father_id = :brandTagId)
            ORDER BY products.amount
            LIMIT :limit
            """, nativeQuery = true)
    List<RentedProductsAmountDto> findMostUsedProducts(LocalDate start, LocalDate end, Integer limit, Long brandTagId);

    @Query(value = """
            SELECT
                movements.item_id AS itemId,
                movements.amount AS amount,
                movements.date AS date,
                movements.action AS action
            FROM
            (
            	SELECT
                    i.item_id as item_id,
                    -i.price_bought as amount,
                    DATE(i.bought_at) as date,
                    'Compra de artículos' as action
            	FROM product p
            	INNER JOIN item i ON (i.product_id = p.product_id AND i.status <> 'DELETED')
            	WHERE p.product_id = :productId
            	UNION
            	SELECT
            	    i.item_id as item_id,
                    (DATEDIFF(pry.end_date, pry.start_date) * rp.value) * (pry.cost_addition / 100) as amount,
                    DATE(pry.start_date) as date,
                    CONCAT('Renta en ', pry.name) as action
            	FROM product p
            	INNER JOIN item i ON (i.product_id = p.product_id)
            	INNER JOIN item_project ip ON (i.item_id = ip.item_id AND ip.status = 'ENABLED')
            	INNER JOIN project pry ON (ip.project_id = pry.project_id AND pry.status NOT IN ('DISCARTED', 'PLANNED'))
            	INNER JOIN product_project pp ON (pry.project_id = pp.project_id AND p.product_id = pp.product_id AND pp.status = 'ENABLED')
            	INNER JOIN rent_price rp ON (pp.rent_price_id = rp.rent_price_id)
            	WHERE i.product_id = :productId
            	GROUP BY i.item_id, rp.value, DATE(pry.start_date), pry.project_id, pry.name
            ) AS movements
            ORDER BY movements.date
            """, nativeQuery = true)
    List<ProductMovementsProjection> findProductMovements(Long productId);
}
