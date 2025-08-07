package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductsInProjectResponseDto;

import java.util.List;

public interface ProductProjectService {
    ProductProjectResponseDto createProductProject(ProductProjectRequestDto productProjectRequestDto,
                                                   List<ItemResponseDto> itemsOfProduct);

    ProductProjectResponseDto deleteProductProject(Long id);

    ProductsInProjectResponseDto getProductsInProject(Long id);
}
