package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductSortByEnum;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepositoryCustom {

    ProductListResponseDto findAllWithFilters(
            List<Long> tagIds, ProductSortByEnum sortBy,
            DirectionEnum direction,
            LocalDate startDate,
            LocalDate endDate, Integer page, Integer size, String title, Long brandId
    );

}
