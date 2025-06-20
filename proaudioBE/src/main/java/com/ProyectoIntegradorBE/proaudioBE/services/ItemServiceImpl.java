package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.Utils.CollectionUtils;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ItemMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ItemRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final QrService qrService;

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    @Override
    public ItemResponseListDto CreateItem(ItemRequestListDto items) throws Exception {

        List<ItemEntity> entityList = new ArrayList<>();

        for (ItemRequestDto item : items.getItems()) {

            List<ItemEntity> itemEntities = createItemBlock(item);
            entityList.addAll(itemEntities);

        }

        entityList = CollectionUtils.toList(itemRepository.saveAll(entityList));

        List<ItemResponseDto> itemResponseList = GenerateQrAndResponse(entityList);

        return new ItemResponseListDto(itemResponseList);
    }

    private List<ItemResponseDto> GenerateQrAndResponse(List<ItemEntity> entityList) throws Exception {

        List<ItemResponseDto> itemResponseList = itemMapper.toDtoList(entityList);

        for (ItemResponseDto item : itemResponseList) {

            item.setQrImage(qrService.generateQrBase64(String.valueOf(item.getItemId())));

        }

        return itemResponseList;
    }

    private List<ItemEntity> createItemBlock(ItemRequestDto item) {

        item.setAmountBought(Objects.isNull(item.getAmountBought()) ? 1 : item.getAmountBought());

        List<ItemEntity> itemEntities = new ArrayList<>();

        for (int i = 0; i < item.getAmountBought(); i++) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setProductId(item.getProductId());
            itemEntity.setDescription(Objects.nonNull(item.getDescription()) ? item.getDescription() : null);
            itemEntity.setPriceBought(Objects.nonNull(item.getPriceBought()) ? item.getPriceBought() : null);
            itemEntity.setBoughtAt(Objects.nonNull(item.getBoughtAt()) ? item.getBoughtAt() : null);
            itemEntity.setLocation(LocationEnum.IN_DEPOSIT);
            itemEntity.setStatus(ItemStatusEnum.CREATED);

            itemEntities.add(itemEntity);
        }

        return itemEntities;

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
