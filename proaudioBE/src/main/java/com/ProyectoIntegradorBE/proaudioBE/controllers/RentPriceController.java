package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PriceResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.RentPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class RentPriceController {

    private final RentPriceService rentPriceService;

    private final ProductService productService;

    @GetMapping("/product/{id}")
    PriceResponseListDto GetProductPhotos(@PathVariable Long id) {
        productService.GetProduct(id);

        return rentPriceService.getPricesByProductId(id);
    }


}
