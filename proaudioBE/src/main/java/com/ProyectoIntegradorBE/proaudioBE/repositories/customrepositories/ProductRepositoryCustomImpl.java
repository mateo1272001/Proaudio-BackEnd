package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductRowDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductSortByEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public ProductListResponseDto findAllWithFilters(
            List<Long> tagIds, ProductSortByEnum sortBy,
            DirectionEnum direction,
            LocalDate startDate,
            LocalDate endDate,
            Integer page,
            Integer size
    ) {
        boolean hasTags = tagIds != null && !tagIds.isEmpty();

        int pageNumber = page != null ? page : 1;
        int pageSize = size != null ? size : 10;
        int offset = pageNumber * pageSize;

        String sortColumn = switch (sortBy) {
            case BRAND -> "brand";
            case MODEL -> "model";
            case STATUS -> "status";
            case ID -> "id";
        };

        String sortDir = direction == DirectionEnum.ASC ? "ASC" : "DESC";

        int tagCount;
        String tagIdCsv;
        if(hasTags) {
            tagCount = tagIds.size();
            tagIdCsv = tagIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        } else {
            tagCount = 0;
            tagIdCsv = "";
        }

        List<ProductRowDto> products = getProductRowDtos(hasTags, tagIdCsv, sortColumn, sortDir, tagCount, pageSize, offset);

        int totalElements = getTotalElements(hasTags, tagIdCsv, tagCount);

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean hasNext = pageNumber + 1 < totalPages;
        boolean hasPrevious = pageNumber > 0;

        PageableDto pageable = PageableDto.builder().pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();

        ProductListResponseDto response = new ProductListResponseDto();
        response.setProducts(products);
        response.setPageable(pageable);
        return response;
    }

    private List<ProductRowDto> getProductRowDtos(boolean hasTags, String tagIdCsv, String sortColumn, String sortDir,
                                                  int tagCount, int pageSize, int offset) {
        String sql;

        if(hasTags) {
            sql = String.format("""
                WITH RECURSIVE tag_hierarchy AS (
                	SELECT tag_id,tag_id as root FROM tag WHERE tag_id IN (%s)
                	UNION ALL
                	SELECT t.tag_id,th.root FROM tag t
                	INNER JOIN tag_hierarchy th ON t.father_id = th.tag_id
                )
                SELECT filtered.*, brands.name as brand
                FROM (
                	SELECT
                		p.product_id AS id,
                		p.model AS model,
                		p.comments AS comments,
                		p.status AS status
                	FROM product p
                	INNER JOIN product_tag pt ON (p.product_id = pt.product_id)
                	INNER JOIN tag_hierarchy th ON (pt.tag_id = th.tag_id)
                                    	WHERE p.status = "ACTIVE" AND pt.type = "DESCRIPTIVE" AND pt.status = "ENABLED"
                	GROUP BY th.root, pt.product_id
                ) AS filtered
                LEFT JOIN
                	(SELECT t_brand.name,pt_brand.product_id
                	FROM product_tag pt_brand
                	INNER JOIN tag t_brand ON (pt_brand.tag_id = t_brand.tag_id AND t_brand.father_id = 1)
                	WHERE pt_brand.status = 'ENABLED')
                AS brands ON brands.product_id = filtered.id
                GROUP BY filtered.id,brands.name
                HAVING COUNT(id) = ?
                ORDER BY %s %s
                LIMIT ?
                OFFSET ?
                
                """, tagIdCsv, sortColumn, sortDir);

            return jdbcTemplate.query(
                    sql,
                    ps -> {
                        ps.setInt(1, tagCount);
                        ps.setInt(2, pageSize);
                        ps.setInt(3, offset);
                    },
                    new BeanPropertyRowMapper<>(ProductRowDto.class)
            );

        } else {
            sql = String.format("""
                    SELECT p.product_id AS id, p.model AS model, p.comments AS comments, p.status AS status,
                        brands.name as brand
                    FROM product p
                    LEFT JOIN
                    	(SELECT t_brand.name,pt_brand.product_id
                    	FROM product_tag pt_brand
                    	INNER JOIN tag t_brand ON (pt_brand.tag_id = t_brand.tag_id AND t_brand.father_id = 1)
                    	WHERE pt_brand.status = 'ENABLED') AS brands
                    ON brands.product_id = p.product_id
                    WHERE p.status = "ACTIVE"
                    ORDER BY %s %s
                    LIMIT ?
                    OFFSET ?
                    
                    """, sortColumn, sortDir);

            return jdbcTemplate.query(
                    sql,
                    ps -> {
                        ps.setInt(1, pageSize);
                        ps.setInt(2, offset);
                    },
                    new BeanPropertyRowMapper<>(ProductRowDto.class)
            );
        }

    }

    private int getTotalElements(boolean hasTags, String tagIdCsv, int tagCount) {

        String countSql;

        if(hasTags) {

            countSql = String.format("""
            WITH RECURSIVE tag_hierarchy AS (
                SELECT tag_id,tag_id as root FROM tag WHERE tag_id IN (%S)
                UNION ALL
                SELECT t.tag_id,th.root FROM tag t
                INNER JOIN tag_hierarchy th ON t.father_id = th.tag_id
            )
            select COUNT(*)
            FROM (
                SELECT filtered.id
                FROM (
                    SELECT
                        p.product_id AS id,
                        p.model AS model,
                        p.comments AS comments,
                        p.status AS status
                    FROM product p
                    INNER JOIN product_tag pt ON (p.product_id = pt.product_id)
                    INNER JOIN tag_hierarchy th ON (pt.tag_id = th.tag_id)
                                                    WHERE p.status = "ACTIVE" AND pt.type = "DESCRIPTIVE" AND pt.status = "ENABLED"
                    GROUP BY th.root, pt.product_id
                ) AS filtered
                GROUP BY filtered.id
                HAVING COUNT(id) = ?
            ) as a
            """, tagIdCsv);


            return jdbcTemplate.query(
                    countSql,
                    ps -> ps.setInt(1, tagCount),
                    rs -> rs.next() ? rs.getInt(1) : 0
            );

        } else {

            countSql = "SELECT COUNT(*) FROM product p WHERE p.status = \"ACTIVE\"";

            return jdbcTemplate.queryForObject(countSql, Integer.class);

        }

    }

}
