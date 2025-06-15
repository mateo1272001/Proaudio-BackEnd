package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.DirectionEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProductStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.SortByEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final RentPriceServiceImpl rentPriceService;

    private final PhotoServiceImpl photoService;

    private final ProductTagServiceImpl productTagService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final TagRepository tagRepository;


    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) throws BadRequestException {

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

        List<PhotoResponseDto> photos =
                photoService.createPhotos(productRequestDto.getPhotos(), productResponseDto.getProductId());

        List<ProductTagResponseDto> tags =
                productTagService.CreateProductTags(productRequestDto.getTags(), product.getProductId());

        productResponseDto.setPrices(prices);
        productResponseDto.setPhotos(photos);
        productResponseDto.setTags(tags);

        return productResponseDto;

    }

    @Override
    @Transactional
    public ProductResponseDto UpdateProduct(ProductRequestDto productRequestDto, Long productId)
            throws BadRequestException, org.apache.coyote.BadRequestException {

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
        productResponseDto.setPhotos(photoService.findPhotosByProductId(productId));
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
    public ProductListResponseDto getFilteredProducts(List<Long> tags, String sortBy, String direction,
                                                      LocalDate startDate, LocalDate endDate,
                                                      Integer page, Integer size) throws BadRequestException {

        if(Objects.isNull(tags) || tags.isEmpty()) {
            List<TagEntity> tagEntities= tagRepository.findByFatherIdAndStatus(null, BasicEnumStatus.ENABLED);
            tags = tagEntities.stream().map(TagEntity::getTagId).toList();
        }

        SortByEnum sortByEnum;
        DirectionEnum directionEnum;

        try {
            sortByEnum = Objects.nonNull(sortBy) ? SortByEnum.valueOf(sortBy.toUpperCase()) : SortByEnum.ID;
            directionEnum = Objects.nonNull(direction) ? DirectionEnum.valueOf(direction.toUpperCase()) : DirectionEnum.ASC;
        } catch (Exception ex) {
            throw new BadRequestException("¡Valor de sort o direction incorrecto!");
        }

        ProductListResponseDto productListResponseDto =
                productRepository.findAllWithFilters(tags, sortByEnum, directionEnum, startDate, endDate, page, size);

        return productListResponseDto;

    }

    public ProductTagResponseDto createProductTag (ProductTagRequestDto productTagRequestDto)
            throws BadRequestException {

        if(Objects.isNull(productTagRequestDto.getProductId())) {
            throw new BadRequestException("¡Debe tener un producto asociado!");
        }

        if (productRepository.findById(productTagRequestDto.getProductId()).isEmpty()) {
            throw new BadRequestException("¡El producto no existe!");
        }

        return productTagService.createProductTag(productTagRequestDto);

    }

    @Override
    public ProductTagResponseDto DeleteProductTag(Long id) {
        return productTagService.deleteProductTag(id);
    }

    @Override
    public PhotoResponseListDto CreatePhoto(PhotoRequestListDto photoRequestListDto) throws BadRequestException {

        List<PhotoRequestDto> photos = photoRequestListDto.getPhotos();
        Long productId = photoRequestListDto.getProductId();

        if(productRepository.findById(productId).isEmpty()) {
            throw new BadRequestException("¡El producto no existe!");
        }

        List<PhotoResponseDto> photoResponseDtos = photoService.createPhotos(photos, productId);
        return new PhotoResponseListDto(photoResponseDtos);
    }

    public PhotoResponseDto UploadPhoto(MultipartFile file, Long productId, String name)
            throws BadRequestException {

        if(productRepository.findById(productId).isEmpty()) {
            throw new BadRequestException("¡El producto no existe!");
        }

        return photoService.UploadPhoto(file, productId, name);
    }

    public List<PhotoResponseDto> UploadMultiplePhotos(MultipartFile[] files, Long productId) {

        if(productRepository.findById(productId).isEmpty()) {
            throw new BadRequestException("¡El producto no existe!");
        }

        return photoService.UploadMultiplePhotos(files, productId);
    }

    @Override
    public PhotoResponseDto DeletePhoto(Long id) {
        return photoService.deletePhoto(id);
    }

    @Override
    public PriceReponseDto CreatePrice(PriceRequestDto priceRequestDto) throws BadRequestException {

        if (Objects.isNull(priceRequestDto.getProductId())) {
            throw new BadRequestException("¡Debe tener un producto asociado!");
        }
        if(productRepository.findById(priceRequestDto.getProductId()).isEmpty()) {
            throw new BadRequestException("¡El producto no existe!");
        }

        return rentPriceService.createPrice(priceRequestDto);

    }

    @Override
    public PriceReponseDto DeletePrice(Long id) throws BadRequestException {
        return rentPriceService.DeletePrice(id);
    }

}
