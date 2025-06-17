package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.SortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ImagesNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.TagNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.TagRepository;
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

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final TagRepository tagRepository;

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

        List<ProductTagResponseDto> tags =
                productTagService.CreateProductTags(productRequestDto.getTags(), product.getProductId());

        List<MultipartFile> photoList = Stream.of(files).toList();

        List<PhotoResponseDto> photos =
                photoService.createPhotos(productRequestDto, productResponseDto.getProductId(), photoList);

        productResponseDto.setPrices(prices);
        productResponseDto.setTags(tags);
        productResponseDto.setPhotos(photos);

        return productResponseDto;

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
        productResponseDto.setTags(productTagService.findTagsByProductId(productId));

        return productResponseDto;
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

//        if(Objects.isNull(tags) || tags.isEmpty()) {
//            List<TagEntity> tagEntities= tagRepository.findByFatherIdAndStatus(null, BasicEnumStatus.ENABLED);
//            tags = tagEntities.stream().map(TagEntity::getTagId).toList();
//        }

        SortByEnum sortByEnum;
        DirectionEnum directionEnum;

        try {
            sortByEnum = Objects.nonNull(sortBy) ? SortByEnum.valueOf(sortBy.toUpperCase()) : SortByEnum.ID;
            directionEnum = Objects.nonNull(direction)
                    ? DirectionEnum.valueOf(direction.toUpperCase())
                    : sortByEnum.equals(SortByEnum.ID) ? DirectionEnum.DESC : DirectionEnum.ASC;
        } catch (Exception ex) {
            throw new BadRequestException("¡Valor de sort o direction incorrecto!");
        }

        ProductListResponseDto productListResponseDto =
                productRepository.findAllWithFilters(tags, sortByEnum, directionEnum, startDate, endDate, page, size);

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
        List<ProductTagResponseDto> productTagResponseDtos = productTagService.findTagsByProductId(id);

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

    //TAGS

    public ProductTagResponseDto createProductTag (ProductTagRequestDto productTagRequestDto)
            throws BadRequestException {

        if(Objects.isNull(productTagRequestDto.getProductId())) {
            throw new BadRequestException("¡Debe tener un producto asociado!");
        }

        GetProduct(productTagRequestDto.getProductId());

        return productTagService.createProductTag(productTagRequestDto);

    }

    @Override
    public ProductTagResponseDto DeleteProductTag(Long id) {
        return productTagService.deleteProductTag(id);
    }

    @Override
    public PhotoResponseListDto CreatePhoto(PhotoRequestListDto photoRequestListDto) throws BadRequestException {

        return new PhotoResponseListDto();
//
//        List<PhotoRequestDto> photos = photoRequestListDto.getPhotos();
//        Long productId = photoRequestListDto.getProductId();
//
//        GetProduct(productId);
//
//        List<PhotoResponseDto> photoResponseDtos = photoService.createPhotos(photos, productId, null);
//        return new PhotoResponseListDto(photoResponseDtos);
    }


    //PHOTOS

    public PhotoResponseDto UploadPhoto(MultipartFile file, Long productId, String name)
            throws BadRequestException {
        GetProduct(productId);
        return photoService.UploadPhoto(file, productId, name);
    }

    public List<PhotoResponseDto> UploadMultiplePhotos(MultipartFile[] files, Long productId) {
        GetProduct(productId);
        return photoService.UploadMultiplePhotos(files, productId);
    }

    @Override
    public PhotoResponseDto DeletePhoto(Long id) {
        return photoService.deletePhoto(id);
    }

    //PRICES

    @Override
    public PriceReponseDto CreatePrice(PriceRequestDto priceRequestDto) throws BadRequestException {

        if (Objects.isNull(priceRequestDto.getProductId())) {
            throw new BadRequestException("¡Debe tener un producto asociado!");
        }
        GetProduct(priceRequestDto.getProductId());

        return rentPriceService.createPrice(priceRequestDto);

    }

    @Override
    public PriceReponseDto DeletePrice(Long id) throws BadRequestException {
        return rentPriceService.DeletePrice(id);
    }

    public PhotoResponseListDto GetProductPhotos(Long id) {
        GetProduct(id);
        return new PhotoResponseListDto(photoService.findPhotosByProductId(id));
    }

}
