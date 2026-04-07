package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRequestListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.ProductTagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupResponseDtoList;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductSortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductProjectService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.RelationGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final RentPriceServiceImpl rentPriceService;

    private final PhotoServiceImpl photoService;

    private final ProductTagServiceImpl productTagService;

    private final RelationGroupService relationGroupService;

    private final RelationGroupServiceImpl relationGroupServiceImpl;

    private final TagServiceImpl tagService;

    private final ItemService itemService;

    private final ProductProjectService productProjectService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, MultipartFile[] files)
            throws BadRequestException {

        ProductEntity product = new ProductEntity();
        product.setModel(productRequestDto.getModel());
        product.setComments(Objects.nonNull(productRequestDto.getComments()) ? productRequestDto.getComments() : null);
        product.setReplacementValue(Objects.nonNull(productRequestDto.getReplacementValue())
                ? productRequestDto.getReplacementValue() : null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        product = productRepository.save(product);
        ProductResponseDto productResponseDto = productMapper.toDto(product);

        List<PriceReponseDto> prices =
                rentPriceService.createPrices(productRequestDto.getPrices(), productResponseDto.getProductId());

        //List<ProductTagResponseDto> tags = CreateProductTags(productRequestDto, product);
        relationGroupServiceImpl.createMultipleRelationGroups(productRequestDto.getTags(), productResponseDto);

        if (files != null && files.length > 0) {
            List<MultipartFile> photoList = Stream.of(files).toList();

            List<PhotoResponseDto> photos =
                    photoService.createPhotos(productResponseDto.getProductId(), photoList);
            productResponseDto.setPhotos(photos);
        }

        productResponseDto.setPrices(prices);
        //        productResponseDto.setTags(relationGroupResponseDtos);

        return productResponseDto;

    }

    @Deprecated
    private List<ProductTagResponseDto> CreateProductTags(ProductRequestDto productRequestDto, ProductEntity product) {

        List<Long> tagIds = new ArrayList<>();

        Long brandTagId = tagService.findBrandRoot().getTagId();
        boolean hasBrandSelected = false;

        for (ProductTagRequestDto productTagRequestDto : productRequestDto.getTags()) {

            Long productTagId = productTagRequestDto.getTagId();

            tagIds.add(productTagId);

            if (tagService.checkIfTagIsChildOfSelected(productTagId, brandTagId) &&
                    productTagRequestDto.getType().equals(TagTypeEnum.DESCRIPTIVE)) {
                hasBrandSelected = true;
            }
        }

        if (!hasBrandSelected) {
            throw new BadRequestException("¡El producto debe tener una marca!");
        }


        List<TagResponseDto> tagsFromRequest = tagService.findByTagIdIn(tagIds);

        return productTagService.CreateProductTags(tagsFromRequest, productRequestDto.getTags(), product.getProductId(),
                brandTagId);
    }

    @Override
    @Transactional
    public ProductResponseDto UpdateProduct(ProductRequestDto productRequestDto, Long productId)
            throws BadRequestException {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product ID no encontrado: " + productId));

        productEntity.setModel(productRequestDto.getModel());
        productEntity.setComments(
                Objects.nonNull(productRequestDto.getComments()) ? productRequestDto.getComments() : null);
        productEntity.setReplacementValue(Objects.nonNull(productRequestDto.getReplacementValue())
                ? productRequestDto.getReplacementValue() : null);
        productEntity.setUpdatedAt(LocalDateTime.now());

        productEntity = productRepository.save(productEntity);

        ProductResponseDto productResponseDto = productMapper.toDto(productEntity);
        productResponseDto.setPrices(rentPriceService.findRentPriceByProductId(productId));
        productResponseDto.setTags(findTagsByProductId(productId));

        return productResponseDto;
    }

    private List<ProductTagResponseDto> findTagsByProductId(Long productId) {

        List<ProductTagEntity> productTagEntities = productTagService.findTagsByProductId(productId);
        List<TagResponseDto> tagResponseDtos =
                tagService.findByTagIdIn(productTagEntities.stream().map(ProductTagEntity::getTagId).toList());

        return productTagService.validateTagsInProductTags(productTagEntities, tagResponseDtos);
    }

    @Override
    public ProductResponseDto DeleteProduct(Long id) throws BadRequestException {

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product ID no encontrado: " + id));

        if (!itemService.getByProductId(id).isEmpty()) {
            throw new BadRequestException("No se puede borrar un producto con artículos asociados");
        }

        if (!productProjectService.getNextProjectsForProduct(productEntity.getProductId()).isEmpty()) {
            throw new BadRequestException(
                    "¡No se puede borrar este producto porque está asignado a proyectos activos!");
        }

        productEntity.setStatus(ProductStatus.ELIMINATED);
        productRepository.save(productEntity);

        return productMapper.toDto(productEntity);
    }

    @Override
    public ProductResponseDto GetProduct(Long productId) {

        Optional<ProductEntity> productEntityOptional = productRepository.findById(productId);

        ProductEntity productEntity =
                productEntityOptional.orElseThrow(() -> new BadRequestException("¡El producto no existe!"));

        return productMapper.toDto(productEntity);
    }


    @Override
    public ProductListResponseDto getFilteredProducts(List<Long> tags, String sortBy, String direction,
                                                      LocalDate startDate, LocalDate endDate, Integer page,
                                                      Integer size, String title) throws BadRequestException {

        ProductSortByEnum productSortByEnum;
        DirectionEnum directionEnum;

        try {
            productSortByEnum =
                    Objects.nonNull(sortBy) ? ProductSortByEnum.valueOf(sortBy.toUpperCase()) : ProductSortByEnum.ID;
            directionEnum = Objects.nonNull(direction)
                    ? DirectionEnum.valueOf(direction.toUpperCase()) :
                    productSortByEnum.equals(ProductSortByEnum.ID) ? DirectionEnum.DESC : DirectionEnum.ASC;
        } catch (Exception ex) {
            throw new BadRequestException("¡Valor de sort o direction incorrecto!");
        }

        Long brandId = tagService.findBrandRoot().getTagId();

        return productRepository.findAllWithFilters(tags, productSortByEnum, directionEnum, startDate, endDate, page,
                size, title, brandId);

    }

    @Override
    public ProductDetailResponseDto GetProductDetails(Long id) {

        ProductDetailResponseDto response = new ProductDetailResponseDto();

        ProductResponseDto product = GetProduct(id);

        response.setProductId(product.getProductId());
        response.setModel(product.getModel());
        response.setComments(product.getComments());
        response.setReplacementValue(product.getReplacementValue());
        response.setStatus(product.getStatus());

        try {
            response.setPhotos(photoService.findPhotosByProductId(id));
        } catch (Exception ex) {
            response.setPhotos(new ArrayList<>());
        }

        response.setPrices(rentPriceService.findRentPriceByProductId(id));
        RelationGroupResponseDtoList relationGroupResponseDto = relationGroupService.getRelationGroupsByProduct(id);

        if (Objects.nonNull(relationGroupResponseDto.getDescriptive().getTags())) {

            List<TagResponseDto> existingTags = tagService.findByTagIdIn(
                    relationGroupResponseDto.getDescriptive().getTags().stream().map(TagResponseDto::getTagId)
                            .toList());

            if (!existingTags.isEmpty()) {
                response.setBrand(
                        obtainBrandOfProduct(relationGroupResponseDto.getDescriptive(), existingTags, response));
            }
        }


        return response;
    }

    private String obtainBrandOfProduct(RelationGroupResponseDto descriptiveTags, List<TagResponseDto> existingTags,
                                        ProductDetailResponseDto response) {
        try {
            TagEntity brandRoot = tagService.findBrandRoot();

            List<Long> descriptiveTagsIds = descriptiveTags.getTags().stream().map(TagResponseDto::getTagId).toList();

            Optional<TagResponseDto> brand = existingTags.stream()
                    .filter(t -> Objects.nonNull(t.getFatherId()) && t.getFatherId().equals(brandRoot.getTagId()) &&
                            descriptiveTagsIds.contains(t.getTagId())).findFirst();

            brand.ifPresent(tagResponseDto -> response.setBrand(tagResponseDto.getName()));

            if (brand.isPresent()) {
                return brand.get().getName();
            }

        } catch (Exception ex) {
            return null;
        }

        return null;
    }

    @Override
    public ProductStatusListDto GetProductStatuses() {

        List<ProductStatus> statusList = Arrays.stream(ProductStatus.values()).toList();

        return new ProductStatusListDto(statusList);
    }

    @Override
    public ItemResponseListDto ValidateProductsAndCreateItem(ItemRequestListDto items) throws Exception {

        List<Long> productRequestIds = items.getItems().stream().map(ItemRequestDto::getProductId).toList();

        List<ProductEntity> productEntities =
                productRepository.findByProductIdInAndStatus(productRequestIds, ProductStatus.ACTIVE);

        List<Long> productIds = productEntities.stream().map(ProductEntity::getProductId).toList();

        items.getItems().forEach(i -> {

            if (productIds.stream().noneMatch(pId -> pId.equals(i.getProductId()))) {
                throw new BadRequestException("¡No hay productos con ese ID!");
            }

        });

        return itemService.createItemsAndGenerateQr(items);
    }

    //TAGS

    @Deprecated
    public ProductTagResponseDto createProductTag (ProductTagRequestDto productTagRequestDto)
            throws BadRequestException {

        if(Objects.isNull(productTagRequestDto.getProductId())) {
            throw new BadRequestException("¡Debe tener un producto asociado!");
        }

        GetProduct(productTagRequestDto.getProductId());

        Optional<TagEntity> tagEntityOpt = tagService.findByTagId(productTagRequestDto.getTagId());

        if (tagEntityOpt.isEmpty()) {
            throw new BadRequestException("¡La etiqueta no existe!");
        }

        TagEntity brandTag = tagService.findBrandRoot();

        return productTagService.createProductTag(productTagRequestDto, tagEntityOpt.get(), brandTag);

    }

    @Deprecated
    public ProductTagResponseDto DeleteProductTag(Long tagId, Long productId, String type) {
        return productTagService.deleteProductTag(tagId, productId, type);
    }

    //PHOTOS

    public List<PhotoResponseDto> CreatePhotos(MultipartFile[] files, Long productId) {
        GetProduct(productId);

        List<MultipartFile> photoList = Stream.of(files).toList();

        return photoService.createPhotos(productId, photoList);
    }

    public PhotoResponseDto DeletePhoto(Long id) {
        return photoService.deletePhoto(id);
    }

    //PRICES

    public PriceReponseDto CreatePrice(PriceRequestDto priceRequestDto) throws BadRequestException {

        if (Objects.isNull(priceRequestDto.getProductId())) {
            throw new BadRequestException("¡Debe tener un producto asociado!");
        }
        GetProduct(priceRequestDto.getProductId());

        return rentPriceService.createPrice(priceRequestDto);

    }

    public PriceReponseDto DeletePrice(Long id) throws BadRequestException {
        return rentPriceService.DeletePrice(id);
    }

    @Override
    public PhotoResponseListDto GetProductPhotos(Long id) {
        GetProduct(id);
        return new PhotoResponseListDto(photoService.findPhotosByProductId(id));
    }

}
