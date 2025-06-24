package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;

public interface ItemService {

    ItemResponseListDto CreateItem(ItemRequestListDto items) throws Exception;

    ItemResponseDto UpdateItem(UpdateItemRequestDto item, Long id);

    ItemResponseDto DeleteItem(Long itemId);

    ItemResponseDto GetItem(Long itemId);

    ItemSectionResonseDto GetItemList(Long productId, String status, String sortBy, String direction, Integer page,
                                      Integer size);

    ItemDetailsResponseDto GetItemDetails(Long itemId);

    ItemActionsDto GetItemActions(String qrId);

    ItemStatusResponseDto GetItemStatuses();
}
