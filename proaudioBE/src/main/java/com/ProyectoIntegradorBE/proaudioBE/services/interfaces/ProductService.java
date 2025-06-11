package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductListResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import org.apache.coyote.BadRequestException;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) throws BadRequestException;

    ProductResponseDto UpdateProduct(ProductRequestDto productRequestDto, Long productId) throws BadRequestException;

    ProductResponseDto DeleteProduct(Long id) throws BadRequestException;

    ProductListResponseDto getFilteredProducts(List<Long> tags, String sortBy, String direction,
                                               LocalDate startDate, LocalDate endDate, Integer page, Integer size);
}
