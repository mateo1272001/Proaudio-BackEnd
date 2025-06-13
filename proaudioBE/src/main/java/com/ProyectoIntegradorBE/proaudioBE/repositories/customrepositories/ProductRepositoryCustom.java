package com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.SortByEnum;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepositoryCustom {

    public ProductListResponseDto findAllWithFilters(
            List<Long> tagIds,
            SortByEnum sortBy,
            DirectionEnum direction,
            LocalDate startDate,
            LocalDate endDate,
            Integer page,
            Integer size
    );

}
