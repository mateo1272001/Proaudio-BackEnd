package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRequestListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    private final ProductService productService;

    @PostMapping
    private ItemResponseListDto CreateItem(@RequestBody ItemRequestListDto items) throws Exception {

        return productService.ValidateProductsAndCreateItem(items);

    }


}
