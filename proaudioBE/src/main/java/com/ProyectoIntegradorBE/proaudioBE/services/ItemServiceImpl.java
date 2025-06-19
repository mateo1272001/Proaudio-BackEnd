package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    @Override
    public ItemResponseListDto CreateItem(ItemRequestListDto items) {
        return null;
    }

    @Override
    public ItemResponseDto UpdateItem(ItemRequestDto item) {
        return null;
    }

    @Override
    public ItemResponseDto DeleteItem(Long itemId) {
        return null;
    }

    @Override
    public ItemResponseDto GetItem(Long itemId) {
        return null;
    }

    @Override
    public ItemSectionResonseDto GetItemList(String status, String sortBy, String direction, Integer page,
                                             Integer size) {
        return null;
    }

    @Override
    public ItemDetailsResponseDto GetItemDetails(Long itemId) {
        return null;
    }

    @Override
    public ItemActionsDto GetItemActions(String qrId) {
        return null;
    }
}
