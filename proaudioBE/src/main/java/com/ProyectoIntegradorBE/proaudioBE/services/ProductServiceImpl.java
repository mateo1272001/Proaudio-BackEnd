package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemRequestListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductSortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ImagesNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.TagNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
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

    private final TagServiceImpl tagService;

    private final ItemService itemService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private static final Long BRAND_TAG_FATHER = 1L;

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

        List<ProductTagResponseDto> tags = CreateProductTags(productRequestDto, product);

        if (files != null && files.length > 0) {
            List<MultipartFile> photoList = Stream.of(files).toList();

            List<PhotoResponseDto> photos =
                    photoService.createPhotos(productResponseDto.getProductId(), photoList);
            productResponseDto.setPhotos(photos);
        }

        productResponseDto.setPrices(prices);
        productResponseDto.setTags(tags);

        return productResponseDto;

    }

    private List<ProductTagResponseDto> CreateProductTags(ProductRequestDto productRequestDto, ProductEntity product) {

        List<Long> tagIds = productRequestDto.getTags().stream().map(ProductTagRequestDto::getTagId).toList();

        List<TagResponseDto> tagsFromRequest = tagService.findByTagIdIn(tagIds);

        return productTagService.CreateProductTags(tagsFromRequest, productRequestDto.getTags(),
                product.getProductId());
    }

    @Override
    @Transactional
    public ProductResponseDto UpdateProduct(ProductRequestDto productRequestDto, Long productId)
            throws BadRequestException, BadRequestException {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Product ID no encontrado: " + productId));

        ProductEntity product = new ProductEntity();
        product.setProductId(productEntity.getProductId());
        product.setModel(productRequestDto.getModel());
        product.setComments(Objects.nonNull(productRequestDto.getComments()) ? productRequestDto.getComments() : null);
        product.setReplacementValue(Objects.nonNull(productRequestDto.getReplacementValue())
                ? productRequestDto.getReplacementValue() : null);
        product.setStatus(productRequestDto.getStatus());
        product.setCreatedAt(productEntity.getCreatedAt());
        product.setUpdatedAt(LocalDateTime.now());
        product = productRepository.save(product);

        ProductResponseDto productResponseDto = productMapper.toDto(product);
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

        //TODO ADD ARTICLES VALIDATION

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
                                                      LocalDate startDate, LocalDate endDate,
                                                      Integer page, Integer size) throws BadRequestException {

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

        ProductListResponseDto productListResponseDto =
                productRepository.findAllWithFilters(tags, productSortByEnum, directionEnum, startDate, endDate, page,
                        size);

        return productListResponseDto;

    }

    @Override
    public ProductDetailResponseDto GetProductDetails(Long id) {

        ProductDetailResponseDto response = new ProductDetailResponseDto();

        ProductResponseDto product = GetProduct(id);

        response.setModel(product.getModel());
        response.setComments(product.getComments());
        response.setReplacementValue(product.getReplacementValue());
        response.setStatus(product.getStatus());
        try {
            response.setBrand(tagService.findByProductIdAndFatherId(id, BRAND_TAG_FATHER).getName());
        } catch (TagNotFoundException ex) {
            response.setBrand("");
        }
        try{
            response.setPhotos(photoService.findPhotosByProductId(id));
        } catch (ImagesNotFoundException ex) {
            response.setPhotos(new ArrayList<>());
        }
        response.setPrices(rentPriceService.findRentPriceByProductId(id));
//        response.setActivities();  //todo add activities when developing this functionalities
//        response.setProductBalance(); //todo add balance when projects are added
        List<ProductTagResponseDto> productTagResponseDtos = findTagsByProductId(id);

        List<TagResponseDto> tags =  tagService.findByTagIdIn(productTagResponseDtos
                .stream()
                .map(ProductTagResponseDto::getTagId)
                .toList());

        response.setDescriptionTags(new ArrayList<>());
        response.setDependencyTags(new ArrayList<>());
        response.setRelationTags(new ArrayList<>());

        for(TagResponseDto tag : tags) {

            ProductTagResponseDto productTagResponseDto =
                    productTagResponseDtos.stream().filter(pt -> pt.getTagId()
                            .equals(tag.getTagId()))
                            .findFirst().orElseThrow(() -> new BadRequestException(""));

            switch (productTagResponseDto.getType()) {
                case DESCRIPTIVE -> response.getDescriptionTags().add(tag);
                case RELATION -> response.getRelationTags().add(tag);
                case DEPENDENCY -> response.getDependencyTags().add(tag);
            }

        }

        return response;
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

        return itemService.CreateItem(items);
    }

    //TAGS

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

        return productTagService.createProductTag(productTagRequestDto);

    }

    public ProductTagResponseDto DeleteProductTag(Long tagId, Long productId) {
        return productTagService.deleteProductTag(tagId, productId);
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

    public PhotoResponseListDto GetProductPhotos(Long id) {
        GetProduct(id);
        return new PhotoResponseListDto(photoService.findPhotosByProductId(id));
    }

}
