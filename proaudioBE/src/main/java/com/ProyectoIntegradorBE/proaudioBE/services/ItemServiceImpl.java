package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.Utils.CollectionUtils;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductDetailResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.*;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ItemMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ItemRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.specifications.ItemSpecification;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemProjectService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.QrService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ProyectoIntegradorBE.proaudioBE.Utils.AppConstants.ITEM_AVAILABLE_STATUS;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final QrService qrService;

    private final UtilService utilService;

    private final ItemProjectService itemProjectService;

    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;


    public static final List<ProjectStatusEnum> PROJECT_STARTED_STATUS =
            List.of(ProjectStatusEnum.ON_COURSE, ProjectStatusEnum.EXPIRED, ProjectStatusEnum.COMPLETED);


    @Override
    @Transactional
    public ItemResponseListDto createItem(ItemRequestListDto items) throws Exception {

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

        if (Objects.isNull(item.getAmountBought()) || item.getAmountBought() <= 0) {
            throw new BadRequestException("Se debe comprar por lo menos un artículo");
        }

        if (item.getAmountBought() != item.getSerialNumbers().size()) {
            throw new BadRequestException("La cantidad de numeros de serie debe ser igual a los productos comprados");
        }

        if (Objects.isNull(item.getPriceBought()) || item.getPriceBought().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("¡Se debe asignar un valor de compra positivo!");
        }

        if (Objects.isNull(item.getBoughtAt()) || item.getBoughtAt().plusDays(1).isAfter(LocalDate.now())) {
            throw new BadRequestException("Debe tener una fecha de compra no futura");
        }

        if (!itemRepository.findRepeatedSerialNumbers(item.getProductId(),
                List.of(ItemStatusEnum.CREATED, ItemStatusEnum.GOOD, ItemStatusEnum.WITH_DETAILS),
                item.getSerialNumbers()).isEmpty()) {
            throw new BadRequestException("Hay números de serie ya reguistrados");
        }

        List<ItemEntity> itemEntities = new ArrayList<>();

        for (int i = 0; i < item.getAmountBought(); i++) {

            int itemPosition = i;
            if (itemEntities.stream()
                    .anyMatch(ie -> ie.getSerialNumber().equals(item.getSerialNumbers().get(itemPosition)))) {
                throw new BadRequestException("¡Hay números de serie repetidos en la petición!");
            }

            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setProductId(item.getProductId());
            itemEntity.setDescription(Objects.nonNull(item.getDescription()) ? item.getDescription() : null);
            itemEntity.setPriceBought(item.getPriceBought());
            itemEntity.setBoughtAt(item.getBoughtAt());
            itemEntity.setLocation(LocationEnum.IN_DEPOSIT);
            itemEntity.setStatus(ItemStatusEnum.CREATED);
            itemEntity.setUpdatedAt(LocalDateTime.now());
            itemEntity.setItemRange(item.getItemRange());

            itemEntity.setSerialNumber(item.getSerialNumbers().get(i));

            itemEntities.add(itemEntity);
        }

        return itemEntities;

    }

    @Override
    public ItemResponseDto updateItem(UpdateItemRequestDto item, Long id) {

        ItemEntity itemEntity =
                itemRepository.findById(id).orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        itemEntity.setStatus(Objects.nonNull(item.getStatus()) ? item.getStatus() : itemEntity.getStatus());
        itemEntity.setDescription(
                Objects.nonNull(item.getDescription()) ? item.getDescription() : itemEntity.getDescription());
        itemEntity.setItemRange(Objects.nonNull(item.getItemRange()) ? item.getItemRange() : itemEntity.getItemRange());
        itemEntity.setUpdatedAt(LocalDateTime.now());

        itemEntity = itemRepository.save(itemEntity);

        return itemMapper.toDto(itemEntity);

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
        itemDetailsResponseDto.setRange(item.getItemRange());
        itemDetailsResponseDto.setSerialNumber(item.getSerialNumber());

        return itemDetailsResponseDto;
    }

    @Override
    public ItemResponseDto getItem(Long itemId) {

        ItemEntity itemEntity =
                itemRepository.findById(itemId).orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        return itemMapper.toDto(itemEntity);
    }

    @Override
    public ItemSectionResponseDto getItemList(Long productId, String status, String sortBy, String direction,
                                              Integer page, Integer size) {

        page = (page != null ? page : 0);
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

        PageableDto pagination = utilService.buildPageableDto(pages);

        return new ItemSectionResponseDto(dtoList, pagination);

    }

    @Override
    public ItemResponseDto deleteItem(Long itemId) {

        ItemEntity itemEntity =
                itemRepository.findById(itemId).orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        if (itemEntity.getStatus().equals(ItemStatusEnum.DELETED) ||
                itemEntity.getStatus().equals(ItemStatusEnum.OUT_OF_USAGE)) {
            throw new BadRequestException("¡El artículo ya está eliminado!");
        }

        if (!itemProjectService.checkNextProjectForItem(itemId).isEmpty()) {
            throw new BadRequestException(
                    "¡Para eliminarlo o retirar su uso, primero sacalo de sus próximos proyectos!");
        }

        if (itemEntity.getStatus().equals(ItemStatusEnum.CREATED)) {
            itemEntity.setStatus(ItemStatusEnum.DELETED);
        } else {
            itemEntity.setStatus(ItemStatusEnum.OUT_OF_USAGE);
        }
        itemEntity.setUpdatedAt(LocalDateTime.now());

        itemEntity = itemRepository.save(itemEntity);

        return itemMapper.toDto(itemEntity);
    }

    @Override
    public ItemDetailsResponseDto getItemDetails(ItemResponseDto item, ProductDetailResponseDto productDetail) {

        ItemProductResponseDto itemProductResponseDto = new ItemProductResponseDto();
        itemProductResponseDto.setProductId(productDetail.getProductId());
        itemProductResponseDto.setBrand(productDetail.getBrand());
        itemProductResponseDto.setModel(productDetail.getModel());

        return FillItemDetails(item, itemProductResponseDto);
    }

    @Override
    public ItemActionsDto GetItemActions(String qrId) {
        return null;
    }

    @Override
    public ItemStatusResponseDto getItemStatuses() {
        return new ItemStatusResponseDto(Arrays.stream(ItemStatusEnum.values()).toList());
    }


    @Override
    public ItemStatusResponseDto getPossibleItemStatuses(Long id) {

        ItemResponseDto itemResponseDto = getItem(id);

        List<ItemStatusEnum> response = switch (itemResponseDto.getStatus()) {
            case CREATED -> List.of(ItemStatusEnum.DELETED);
            case DELETED -> List.of(ItemStatusEnum.CREATED);
            case GOOD -> List.of(ItemStatusEnum.WITH_DETAILS, ItemStatusEnum.OUT_OF_USAGE);
            case WITH_DETAILS -> List.of(ItemStatusEnum.GOOD, ItemStatusEnum.OUT_OF_USAGE);
            case OUT_OF_USAGE -> List.of(ItemStatusEnum.WITH_DETAILS, ItemStatusEnum.GOOD);

        };

        return new ItemStatusResponseDto(response);
    }


    @Override
    public List<ItemResponseDto> getByProductIds(List<Long> productIds) {

        List<ItemEntity> itemEntities = itemRepository.findByProductIdInAndStatusIn(productIds, GetUsableStatuses());

        return itemMapper.toDtoList(itemEntities);

    }

    @Override
    public List<ItemResponseDto> getByProductId(Long productId) {
        List<ItemEntity> itemEntities = itemRepository.findByProductIdAndStatusIn(productId, GetUsableStatuses());

        return itemMapper.toDtoList(itemEntities);
    }

    @Override
    public ItemResponseDto regenerateItemQr(Long id) throws Exception {

        ItemEntity itemEntity = itemRepository.findByItemId(id)
                .orElseThrow(() -> new BadRequestException("Artículo no encontrado con ID " + id));

        ItemResponseDto itemResponseDto = GenerateQrAndResponse(List.of(itemEntity)).stream().findFirst()
                .orElseThrow(() -> new InternalException("Error regenerating QR code"));

        return itemResponseDto;
    }

    @Override
    public ItemResponseDto updateLocation(LocationEnum locationEnum, ItemResponseDto itemResponseDto) {

        ItemEntity itemEntity = itemMapper.toEntity(itemResponseDto);

        if (locationEnum.equals(LocationEnum.USING) && itemEntity.getStatus().equals(ItemStatusEnum.CREATED)) {
            itemEntity.setStatus(ItemStatusEnum.GOOD);
            itemEntity = itemRepository.save(itemEntity);
        }

        itemEntity.setLocation(locationEnum);

        itemEntity = itemRepository.save(itemEntity);

        return itemMapper.toDto(itemEntity);
    }

    @Override
    public List<ItemEntity> getByItemProject(Long projectId) {

        return itemRepository.findByProjectIdAndStatus(projectId, BasicEnumStatus.ENABLED.name(),
                ITEM_AVAILABLE_STATUS.stream().map(Enum::name).toList());
    }

    private List<ItemStatusEnum> GetUsableStatuses() {
        return List.of(ItemStatusEnum.CREATED, ItemStatusEnum.GOOD, ItemStatusEnum.WITH_DETAILS);
    }
}
