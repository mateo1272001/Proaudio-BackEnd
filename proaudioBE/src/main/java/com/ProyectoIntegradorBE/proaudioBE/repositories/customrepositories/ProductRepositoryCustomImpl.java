package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductRowDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.SortByEnum;
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
            List<Long> tagIds,
            SortByEnum sortBy,
            DirectionEnum direction,
            LocalDate startDate,
            LocalDate endDate,
            Integer page,
            Integer size
    ) {
        boolean hasTags = tagIds != null && !tagIds.isEmpty();
        tagIds = hasTags ? tagIds : List.of(-1L);

        int pageNumber = (page != null ? page : 1) - 1;
        int pageSize = size != null ? size : 10;
        int offset = pageNumber * pageSize;
        int tagCount = tagIds.size();

        String sortColumn = switch (sortBy) {
            case BRAND -> "brand";
            case MODEL -> "model";
            case STATUS -> "status";
            case ID -> "id";
        };

        String sortDir = direction == DirectionEnum.ASC ? "ASC" : "DESC";

        String tagIdCsv = tagIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        String sql = String.format("""
                WITH RECURSIVE tag_hierarchy AS (
                    SELECT tag_id FROM tag WHERE tag_id IN (%s)
                    UNION ALL
                    SELECT t.tag_id FROM tag t
                    INNER JOIN tag_hierarchy th ON t.father_id = th.tag_id
                )
                SELECT
                    p.product_id AS id,
                    marca_tag.name AS brand,
                    p.model AS model,
                    p.comments AS comments,
                    p.status AS status
                FROM product p
                INNER JOIN product_tag marca_pt
                    ON p.product_id = marca_pt.product_id
                    AND marca_pt.type = 'DESCRIPTIVE'
                INNER JOIN tag marca_tag
                    ON marca_tag.tag_id = marca_pt.tag_id
                    AND marca_tag.father_id = 1
                WHERE p.status = 'ACTIVE'
                  AND p.product_id IN (
                      SELECT pt.product_id
                      FROM product_tag pt
                      WHERE pt.type = 'DESCRIPTIVE'
                        AND pt.tag_id IN (SELECT tag_id FROM tag_hierarchy)
                      GROUP BY pt.product_id
                      HAVING COUNT(DISTINCT pt.tag_id) >= ?
                  )
                ORDER BY %s %s
                LIMIT ? OFFSET ?;
                """, tagIdCsv, sortColumn, sortDir);

        List<ProductRowDto> products = jdbcTemplate.query(
                sql,
                ps -> {
                    ps.setInt(1, tagCount);
                    ps.setInt(2, pageSize);
                    ps.setInt(3, offset);
                },
                new BeanPropertyRowMapper<>(ProductRowDto.class)
        );

        // Total count
        String countSql = String.format("""
            WITH RECURSIVE tag_hierarchy AS (
                SELECT tag_id FROM tag WHERE tag_id IN (%s)
                UNION ALL
                SELECT t.tag_id FROM tag t
                INNER JOIN tag_hierarchy th ON t.father_id = th.tag_id
            )
            SELECT COUNT(*) FROM (
                SELECT pt.product_id
                FROM product_tag pt
                INNER JOIN product p ON pt.product_id = p.product_id
                WHERE pt.type = 'DESCRIPTIVE'
                  AND p.status = 'ACTIVE'
                  AND pt.tag_id IN (SELECT tag_id FROM tag_hierarchy)
                GROUP BY pt.product_id
                HAVING COUNT(DISTINCT pt.tag_id) >= ?
            ) AS filtered
            """, tagIdCsv);

        int totalElements = jdbcTemplate.query(
                countSql,
                ps -> ps.setInt(1, tagCount),
                rs -> rs.next() ? rs.getInt(1) : 0
        );

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        boolean hasNext = pageNumber + 1 < totalPages;
        boolean hasPrevious = pageNumber > 0;

        PageableDto pageable = PageableDto.builder()
                .pageNumber(pageNumber + 1)
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
}
