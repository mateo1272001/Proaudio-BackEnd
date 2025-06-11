package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.SortByEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductRepositoryCustomImpl {

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    public ProductListResponseDto findAllWithFilters(
//            List<Long> tagIds,
//            SortByEnum sortBy,
//            DirectionEnum direction,
//            LocalDate startDate, //TODO INCLUDE DATE FILTERS WHEN ARTICLES AND PROJECTS ARE ADDED
//            LocalDate endDate, //TODO INCLUDE DATE FILTERS WHEN ARTICLES AND PROJECTS ARE ADDED
//            Integer page,
//            Integer size
//    ) {
//        //TODO THINK OF ADDING RELATION TAGS FILTERS TO QUERY
//
//        Boolean hasTags = Objects.nonNull(tagIds) && !tagIds.isEmpty();
//        tagIds = hasTags ? tagIds : List.of(-1L);
//        page = Objects.nonNull(page) ? page - 1 : 0;
//        size = Objects.nonNull(size) ? size : 10;
//        sortBy = Objects.nonNull(sortBy) ? sortBy : SortByEnum.ID;
//        direction = Objects.nonNull(direction) ? direction : DirectionEnum.DESC;
//
//        String sql = """
//                WITH RECURSIVE tag_hierarchy AS (
//                    SELECT tag_id
//                    FROM tag
//                    WHERE (:hasTags = TRUE AND tag_id IN (:tagIds))
//
//                    UNION ALL
//
//                    SELECT t.tag_id
//                    FROM tag t
//                    INNER JOIN tag_hierarchy th ON t.father_id = th.tag_id
//                )
//
//                SELECT
//                    p.product_id AS ID,
//                    marca_tag.name AS Marca,
//                    p.model AS Modelo,
//                    p.comments AS Comentarios,
//                    p.status AS Estado
//                FROM product p
//                INNER JOIN product_tag marca_pt
//                    ON p.product_id = marca_pt.product_id
//                    AND marca_pt.type = 'DESCRIPTIVE'
//                INNER JOIN tag marca_tag
//                    ON marca_tag.tag_id = marca_pt.tag_id
//                    AND marca_tag.father_id = 1
//                WHERE p.status = 'ACTIVE'
//                  AND p.product_id IN (
//                      SELECT pt.product_id
//                      FROM product_tag pt
//                      WHERE pt.type = 'DESCRIPTIVE'
//                        AND pt.tag_id IN (SELECT tag_id FROM tag_hierarchy)
//                      GROUP BY pt.product_id
//                      HAVING COUNT(DISTINCT pt.tag_id) >= :tagIdCount
//                  )
//                ORDER BY :sortBy :direction
//                LIMIT :size OFFSET :offset;
//        """;
//
//        Query query = entityManager.createNativeQuery(sql, "ProductCustomDtoMapping");
//
//        query.setParameter("hasTags", hasTags);
//        query.setParameter("tagIds", tagIds);
//        query.setParameter("startDate", startDate);
//        query.setParameter("endDate", endDate);
//        query.setParameter("tagIdCount", tagIds.size());
//        query.setParameter("limit", size);
//        query.setParameter("offset", page * size);
//        query.setParameter("sortBy", sortBy);
//        query.setParameter("direction", direction);
//
//        return query.getResultList();
//    }

}
