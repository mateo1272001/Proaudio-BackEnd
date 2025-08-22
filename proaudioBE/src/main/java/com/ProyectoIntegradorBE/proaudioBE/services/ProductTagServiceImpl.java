package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductTagMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductTagRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTagServiceImpl implements ProductTagService {

    private final ProductTagRepository productTagRepository;

    private final ProductTagMapper productTagMapper;

    //    private final TagServiceImpl tagService;

    public ProductTagResponseDto createProductTag(ProductTagRequestDto productTagRequestDto, TagEntity tagEntity) {

        if(Objects.isNull(productTagRequestDto.getTagId())) {
            throw new BadRequestException("¡Debe tener una etiqueta asociada!");
        }

        Optional<ProductTagEntity> productTagOpt =
                productTagRepository.findByProductIdAndTagIdAndType(productTagRequestDto.getProductId(),
                        productTagRequestDto.getTagId(), productTagRequestDto.getType());

        if(productTagOpt.isPresent()) {

            ProductTagEntity existentProductTag = productTagOpt.get();

            if (existentProductTag.getStatus().equals(BasicEnumStatus.ENABLED)) {
                throw new BadRequestException("¡La etiqueta ya fue asignada en este grupo!");
            }

            existentProductTag.setStatus(BasicEnumStatus.ENABLED);
            existentProductTag = productTagRepository.save(productTagOpt.get());
            ProductTagResponseDto existentProductTagResponseDto = productTagMapper.toDto(existentProductTag);
            existentProductTagResponseDto.setName(tagEntity.getName());

            return existentProductTagResponseDto;
        }

        ProductTagEntity productTagEntity = productTagMapper.toEntity(productTagRequestDto);
        productTagEntity.setStatus(BasicEnumStatus.ENABLED);

        productTagEntity = productTagRepository.save(productTagEntity);

        ProductTagResponseDto productTagResponseDto = productTagMapper.toDto(productTagEntity);
        productTagResponseDto.setName(tagEntity.getName());

        return productTagResponseDto;

    }

    @Override
    public ProductTagResponseDto deleteProductTag(Long tagId, Long productId, String type) {

        TagTypeEnum typeEnum = TagTypeEnum.valueOf(type.toUpperCase());

        Optional<ProductTagEntity> productTagEntityOpt =
                productTagRepository.findByTagIdAndProductIdAndTypeAndStatus(tagId, productId, typeEnum,
                        BasicEnumStatus.ENABLED);

        if (productTagEntityOpt.isEmpty()) {
            throw new BadRequestException("¡No existe vínculo actual entre este producto y esta etiqueta!");
        }

        ProductTagEntity productTagEntity = productTagEntityOpt.get();
        productTagEntity.setStatus(BasicEnumStatus.DISABLED);

        productTagRepository.save(productTagEntity);

        return productTagMapper.toDto(productTagEntity);
    }

    public List<ProductTagResponseDto> CreateProductTags(List<TagResponseDto> tags,
                                                         List<ProductTagRequestDto> productTags, Long productId)
            throws BadRequestException {

        List<ProductTagResponseDto> response = productTags.stream().map(tagRequest -> {
            TagResponseDto tag = tags.stream()
                    .filter(t -> t.getTagId().equals(tagRequest.getTagId()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Tag ID no válido: " + tagRequest.getTagId()));

            ProductTagResponseDto dto = new ProductTagResponseDto();
            dto.setTagId(tag.getTagId());
            dto.setType(tagRequest.getType());
            dto.setName(tag.getName());
            return dto;
        }).toList();

        List<ProductTagEntity> productTagEntities = convertToEntities(productTags, productId, BasicEnumStatus.ENABLED);
        productTagRepository.saveAll(productTagEntities);

        return response;
    }

    private List<ProductTagEntity> convertToEntities(List<ProductTagRequestDto> dtoList, Long productId,
                                                     BasicEnumStatus status) {
        return dtoList.stream()
                .map(dto -> {
                    ProductTagEntity entity = new ProductTagEntity();
                    entity.setProductId(productId);
                    entity.setTagId(dto.getTagId());
                    entity.setType(dto.getType());
                    entity.setStatus(status);
                    return entity;
                })
                .collect(Collectors.toList());
    }

    public List<ProductTagEntity> findTagsByProductId(Long productId) {
        return productTagRepository.findByProductIdAndStatus(productId, BasicEnumStatus.ENABLED);
    }


    public List<ProductTagResponseDto> validateTagsInProductTags(List<ProductTagEntity> productTagEntities,
                                                                 List<TagResponseDto> tagResponseDtos) {
        //        List<ProductTagEntity> productTagEntities =
        //                productTagRepository.findByProductIdAndStatus(productId, BasicEnumStatus.ENABLED);
        //        List<TagResponseDto> tagResponseDtos =
        //                tagService.findByTagIdIn(productTagEntities.stream().map(ProductTagEntity::getTagId).toList());
        List<ProductTagResponseDto> response = new ArrayList<>();

        for(ProductTagEntity productTagEntity : productTagEntities) {
            TagResponseDto tagResponseDto = tagResponseDtos
                            .stream()
                            .filter(t -> t.getTagId().equals(productTagEntity.getTagId()))
                            .findFirst()
                            .orElseThrow(() -> new BadRequestException("¡Tag no encontrado!"));

            if(Objects.nonNull(tagResponseDto)){
                ProductTagResponseDto productTagResponseDto = new ProductTagResponseDto();
                productTagResponseDto.setName(tagResponseDto.getName());
                productTagResponseDto.setType(productTagEntity.getType());
                productTagResponseDto.setTagId(tagResponseDto.getTagId());
                response.add(productTagResponseDto);
            }

        }

        return response;
    }

    public List<ProductTagResponseDto> findProductTagsByTagId(Long tagId) {

        List<ProductTagEntity> productTagEntities =
                productTagRepository.findByTagIdAndStatus(tagId, BasicEnumStatus.ENABLED);

        return productTagMapper.toDtoList(productTagEntities);
    }
}
