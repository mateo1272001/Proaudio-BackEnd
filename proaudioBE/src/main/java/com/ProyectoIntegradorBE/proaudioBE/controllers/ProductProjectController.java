package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDtoImpl;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/project")
public class ProductProjectController {

    private final ProductProjectService productProjectService;

    private final ProjectService projectService;

    private final ProductService productService;

    private final ProductTagService productTagService;

    private final RentPriceService rentPriceService;

    private final ItemService itemService;

    @PostMapping
    private ProductInProjectResponseDtoImpl createProductProject(
            @RequestBody ProductProjectRequestDto productProjectRequestDto) {

        ProjectSimpleReponseDto projectResponseDto = projectService.getProject(productProjectRequestDto.getProjectId());

        ProductResponseDto product = productService.GetProduct(productProjectRequestDto.getProductId());
        List<ProductTagEntity> productTagEntities =
                productTagService.findTagsByProductIds(List.of(product.getProductId()));

        List<ProductInProjectResponseDto> productsInProjectResponseDto =
                productProjectService.getProductsInProject(projectResponseDto.getProjectId()).getProducts();
        List<ProductTagEntity> productTagsInProject = productTagService.findTagsByProductIds(
                productsInProjectResponseDto.stream().map(ProductInProjectResponseDto::getId).toList());
        productTagsInProject.addAll(productTagEntities);


        //        String message = projectService.validateDependenciesUpdate(product.getModel(), productProjectRequestDto,
        //                productTagsInProject);
        String message = "";
        //TODO CREATE NEW VALIDATOR


        PriceReponseDto priceReponseDto = rentPriceService.getPrice(productProjectRequestDto.getRentPriceId());

        if (!productProjectRequestDto.getProductId().equals(priceReponseDto.getProductId())) {
            throw new BadRequestException("¡Este precio no corresponde al producto!");
        }

        List<ItemResponseDto> items = itemService.getByProductId(productProjectRequestDto.getProductId());

        ProductInProjectResponseDtoImpl productInProjectResponseDto =
                productProjectService.createProductProject(productProjectRequestDto, items);
        productInProjectResponseDto.setRentPriceValue(priceReponseDto.getValue());
        if (Objects.nonNull(message) && !message.isBlank()) {
            productInProjectResponseDto.setMessage(message);
        }

        return productInProjectResponseDto;
    }

    @DeleteMapping("{id}")
    private ProductInProjectResponseDtoImpl deleteProductProject(@PathVariable Long id) {

        return productProjectService.deleteProductProject(id);

    }


}
