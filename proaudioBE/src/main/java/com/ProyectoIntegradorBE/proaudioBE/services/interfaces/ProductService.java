package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, MultipartFile[] files)
            throws BadRequestException;

    ProductResponseDto UpdateProduct(ProductRequestDto productRequestDto, Long productId) throws BadRequestException;

    ProductResponseDto DeleteProduct(Long id) throws BadRequestException;

    ProductResponseDto GetProduct(Long productId);

    ProductListResponseDto getFilteredProducts(List<Long> tags, String sortBy, String direction,
                                               LocalDate startDate, LocalDate endDate, Integer page, Integer size) throws BadRequestException;

    ProductTagResponseDto createProductTag(ProductTagRequestDto productTagRequestDto)throws BadRequestException;

    ProductTagResponseDto DeleteProductTag(Long id);

    PhotoResponseListDto CreatePhoto(PhotoRequestListDto photoRequestListDto) throws BadRequestException;

    PhotoResponseDto DeletePhoto(Long id);

    PriceReponseDto CreatePrice(PriceRequestDto priceRequestDto) throws BadRequestException;

    PriceReponseDto DeletePrice(Long id) throws BadRequestException;

    ProductDetailResponseDto GetProductDetails(Long id);

    ProductStatusListDto GetProductStatuses();
}
