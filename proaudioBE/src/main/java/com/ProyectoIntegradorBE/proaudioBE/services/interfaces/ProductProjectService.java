package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectWithModelResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDtoImpl;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductsInProjectResponseDto;

import java.util.List;

public interface ProductProjectService {

    ProductInProjectResponseDtoImpl createProductProject(ProductProjectRequestDto productProjectRequestDto,
                                                         List<ItemResponseDto> itemsOfProduct);

    ProductInProjectResponseDtoImpl deleteProductProject(Long id);

    ProductsInProjectResponseDto getProductsInProject(Long id);

    List<ProductProjectWithModelResponseDto> getByProductAndProjectId(Long productId, Long projectId);
}
