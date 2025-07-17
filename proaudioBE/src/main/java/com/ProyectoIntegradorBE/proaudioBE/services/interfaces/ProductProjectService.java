package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;

import java.util.List;

public interface ProductProjectService {
    ProductProjectResponseDto createProductProject(ProductProjectRequestDto productProjectRequestDto,
                                                   List<ItemResponseDto> itemsOfProduct);

    ProductProjectResponseDto deleteProductProject(Long id);
}
