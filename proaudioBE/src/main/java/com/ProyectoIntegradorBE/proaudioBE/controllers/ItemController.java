package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("{id}")
    private ItemResponseDto UpdateItem(@RequestBody UpdateItemRequestDto itemRequestDto, @PathVariable Long id) {

        return itemService.UpdateItem(itemRequestDto, id);

    }

    @DeleteMapping("{id}")
    private ItemResponseDto DeleteItem(@PathVariable Long id) {

        return itemService.DeleteItem(id);

    }

    @GetMapping("{id}")
    private ItemResponseDto GetItem(@PathVariable Long id) {

        return itemService.GetItem(id);

    }

    @GetMapping("/status")
    private ItemStatusResponseDto getItemStatuses() {

        return itemService.GetItemStatuses();

    }


}
