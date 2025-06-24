package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.Utils.CollectionUtils;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductDetailResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemSortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ItemMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ItemRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.specifications.ItemSpecification;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final QrService qrService;

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    @Override
    @Transactional
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
            itemEntity.setUpdatedAt(LocalDateTime.now());

            itemEntities.add(itemEntity);
        }

        return itemEntities;

    }

    @Override
    public ItemResponseDto UpdateItem(UpdateItemRequestDto item, Long id) {

        ItemEntity itemEntity =
                itemRepository.findById(id).orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        itemEntity.setStatus(Objects.nonNull(item.getStatus()) ? item.getStatus() : itemEntity.getStatus());
        itemEntity.setDescription(
                Objects.nonNull(item.getDescription()) ? item.getDescription() : itemEntity.getDescription());
        itemEntity.setUpdatedAt(LocalDateTime.now());

        itemEntity = itemRepository.save(itemEntity);

        return itemMapper.toDto(itemEntity);

    }

    @Override
    public ItemResponseDto DeleteItem(Long itemId) {

        ItemEntity itemEntity =
                itemRepository.findById(itemId).orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        //todo (PROJECTS) add project participation validation

        itemEntity.setStatus(ItemStatusEnum.DELETED);
        itemEntity.setUpdatedAt(LocalDateTime.now());

        itemEntity = itemRepository.save(itemEntity);

        return itemMapper.toDto(itemEntity);
    }

    @Override
    public ItemResponseDto GetItem(Long itemId) {

        ItemEntity itemEntity =
                itemRepository.findById(itemId).orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        return itemMapper.toDto(itemEntity);
    }

    @Override
    public ItemSectionResonseDto GetItemList(Long productId, String status, String sortBy, String direction,
                                             Integer page, Integer size) {

        page = (page != null ? page : 1);
        DirectionEnum dir = Objects.isNull(direction) ? DirectionEnum.DESC : DirectionEnum.valueOf(direction);
        ItemSortByEnum sortByEnum =
                Objects.isNull(sortBy) ? ItemSortByEnum.ID : ItemSortByEnum.valueOf(sortBy.toUpperCase());

        String sortColumn = switch (sortByEnum) {
            case LOCATION -> "location";
            case BOUGHT_AT -> "boughtAt";
            case ID -> "itemId";
        };

        Sort sort = Sort.by(Sort.Direction.fromString(dir.name()), sortColumn);
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<ItemEntity> spec = Objects.isNull(status) ? ItemSpecification.filterBy(productId) :
                ItemSpecification.filterBy(productId, ItemStatusEnum.valueOf(status));

        Page<ItemEntity> pages = itemRepository.findAll(spec, pageable);

        List<ItemRowDto> dtoList = pages.getContent().stream().map(itemMapper::toRowDto).toList();

        PageableDto pagination = buildPageableDto(pages);

        return new ItemSectionResonseDto(dtoList, pagination);

    }

    private PageableDto buildPageableDto(Page<?> page) {
        return PageableDto.builder().pageNumber(page.getNumber()).pageSize(page.getSize())
                .totalPages(page.getTotalPages()).totalElements(page.getTotalElements()).hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious()).build();
    }

    private static ItemDetailsResponseDto FillItemDetails(ItemResponseDto item,
                                                          ItemProductResponseDto itemProductResponseDto) {
        ItemDetailsResponseDto itemDetailsResponseDto = new ItemDetailsResponseDto();
        itemDetailsResponseDto.setProduct(itemProductResponseDto);
        itemDetailsResponseDto.setDescription(Objects.nonNull(item.getDescription()) ? item.getDescription() : null);
        itemDetailsResponseDto.setStatus(item.getStatus());
        itemDetailsResponseDto.setLocation(item.getLocation());
        itemDetailsResponseDto.setPriceBought(Objects.nonNull(item.getPriceBought()) ? item.getPriceBought() : null);
        itemDetailsResponseDto.setBoughtAt(item.getBoughtAt());
        //        itemDetailsResponseDto.setActivities()
        return itemDetailsResponseDto;
    }

    @Override
    public ItemDetailsResponseDto GetItemDetails(ItemResponseDto item, ProductDetailResponseDto productDetail) {

        ItemProductResponseDto itemProductResponseDto = new ItemProductResponseDto();
        itemProductResponseDto.setProductId(productDetail.getProductId());
        itemProductResponseDto.setBrand(productDetail.getBrand());
        itemProductResponseDto.setModel(productDetail.getModel());
        itemProductResponseDto.setPhotos(productDetail.getPhotos());

        return FillItemDetails(item, itemProductResponseDto);
    }

    @Override
    public ItemActionsDto GetItemActions(String qrId) {
        return null;
    }

    @Override
    public ItemStatusResponseDto GetItemStatuses() {
        return new ItemStatusResponseDto(Arrays.stream(ItemStatusEnum.values()).toList());
    }
}
