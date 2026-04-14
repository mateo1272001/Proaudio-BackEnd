package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductDetailResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface ItemService {

    ItemResponseListDto createItemsAndGenerateQr(ItemRequestListDto items) throws Exception;

    ItemResponseDto updateItem(UpdateItemRequestDto item, Long id);

    ItemResponseDto deleteItem(Long itemId);

    ItemResponseDto getItem(Long itemId);

    ItemSectionResponseDto getItemList(Long productId, String status, String sortBy, String direction, Integer page,
                                       Integer size);

    ItemDetailsResponseDto getItemDetails(ItemResponseDto item, ProductDetailResponseDto product);

    ItemActionsDto GetItemActions(String qrId);

    ItemStatusResponseDto getItemStatuses();

    List<ItemResponseDto> getByProductIds(List<Long> productIds);

    List<ItemResponseDto> getByProductId(@NotNull Long productId);

    ItemResponseDto regenerateItemQr(Long id) throws Exception;

    ItemResponseDto updateLocation(LocationEnum locationEnum, ItemResponseDto itemResponseDto);

    List<ItemEntity> getByItemProject(Long projectId);

    ItemStatusResponseDto getPossibleItemStatuses(Long id);
}
