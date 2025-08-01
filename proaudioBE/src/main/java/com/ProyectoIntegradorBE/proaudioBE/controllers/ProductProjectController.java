package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDtoImpl;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/project")
public class ProductProjectController {

    private final ProductProjectService productProjectService;

    private final ProjectService projectService;

    private final ProductService productService;

    private final RentPriceService rentPriceService;

    private final ItemService itemService;

    @PostMapping
    private ProductInProjectResponseDtoImpl createProductProject(
            @RequestBody ProductProjectRequestDto productProjectRequestDto) {

        projectService.getProject(productProjectRequestDto.getProjectId());
        productService.GetProduct(productProjectRequestDto.getProductId());
        PriceReponseDto priceReponseDto = rentPriceService.getPrice(productProjectRequestDto.getRentPriceId());

        if (!productProjectRequestDto.getProductId().equals(priceReponseDto.getProductId())) {
            throw new BadRequestException("¡Este precio no corresponde al producto!");
        }

        List<ItemResponseDto> items = itemService.getByProductId(productProjectRequestDto.getProductId());

        return productProjectService.createProductProject(productProjectRequestDto, items);

    }

    @DeleteMapping("{id}")
    private ProductInProjectResponseDtoImpl deleteProductProject(@PathVariable Long id) {

        return productProjectService.deleteProductProject(id);

    }


}
