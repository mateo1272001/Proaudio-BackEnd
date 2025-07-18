package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilService {

    public PageableDto buildPageableDto(Page<?> page) {
        return PageableDto.builder().pageNumber(page.getNumber()).pageSize(page.getSize())
                .totalPages(page.getTotalPages()).totalElements(page.getTotalElements()).hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious()).build();
    }

}
