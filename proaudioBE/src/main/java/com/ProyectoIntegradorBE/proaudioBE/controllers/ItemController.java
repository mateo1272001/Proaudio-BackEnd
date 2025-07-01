package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductDetailResponseDto;
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

    @GetMapping("/product/{id}")
    private ItemSectionResponseDto GetItemList(@PathVariable Long id,
                                               @RequestParam(required = false, defaultValue = "id") String sortBy,
                                               @RequestParam(required = false) String direction,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) String status) {

        productService.GetProduct(id);

        return itemService.GetItemList(id, status, sortBy, direction, page, size);

    }

    @GetMapping("{id}/detail")
    private ItemDetailsResponseDto GetItemDetails(@PathVariable Long id) {

        ItemResponseDto item = itemService.GetItem(id);

        ProductDetailResponseDto productDetailResponseDto = productService.GetProductDetails(item.getProductId());

        productService.GetProductPhotos(item.getProductId());

        return itemService.GetItemDetails(item, productDetailResponseDto);

    }

}
