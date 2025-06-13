package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagResponseDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ProductTagService {

    List<ProductTagResponseDto> CreateProductTags(List<ProductTagRequestDto> tags, Long productId) throws BadRequestException;

    ProductTagResponseDto createProductTag(ProductTagRequestDto productTagRequestDto);

    ProductTagResponseDto deleteProductTag(Long id);
}
