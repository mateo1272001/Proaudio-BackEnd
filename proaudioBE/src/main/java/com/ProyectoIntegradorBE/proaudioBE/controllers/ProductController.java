package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.services.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductServiceImpl productService;


    //
//    @GetMapping("/all")
//    String GetProducts() {
//        return "hola hola";
//    }

    @PostMapping
    private ProductResponseDto CreateProductComplete(@RequestBody ProductRequestDto productRequestDto)
            throws BadRequestException {

        //TODO ADD MANDATORY BRAND TAG
        return productService.createProduct(productRequestDto);

    }

    @PutMapping("{id}")
    private ProductResponseDto UpdateProduct(@RequestBody ProductRequestDto productRequestDto,
                                             @PathVariable Long id) throws BadRequestException {

        return productService.UpdateProduct(productRequestDto, id);

    }

    @DeleteMapping("{id}")
    private ProductResponseDto DeleteProduct(@PathVariable Long id) throws BadRequestException {

        return productService.DeleteProduct(id);

    }

    @GetMapping("/all")
    private ProductListResponseDto ListProducts(
            @RequestParam(required = false) List<Long> tags,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) throws BadRequestException {

        return productService.getFilteredProducts(tags, sortBy, direction, startDate, endDate, page, size);

    }

    @PostMapping("/tag")
    ProductTagResponseDto createProductTag(@RequestBody  ProductTagRequestDto productTagRequestDto) throws BadRequestException {
        return productService.createProductTag(productTagRequestDto);
    }

    @DeleteMapping("/tag/{id}")
    ProductTagResponseDto DeleteProductTag(@RequestParam Long id) throws BadRequestException {
        return productService.deleteProductTag(id);
    }

}
