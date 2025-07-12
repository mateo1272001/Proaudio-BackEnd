package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ProductTagService {


    List<ProductTagResponseDto> CreateProductTags(List<TagResponseDto> tags, List<ProductTagRequestDto> productTags,
                                                  Long productId) throws BadRequestException;

    ProductTagResponseDto createProductTag(ProductTagRequestDto productTagRequestDto, TagEntity tagEntity);

    ProductTagResponseDto deleteProductTag(Long tagId, Long productId, String type);
}
