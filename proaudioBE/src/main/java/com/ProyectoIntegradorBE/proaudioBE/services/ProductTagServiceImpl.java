package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductTagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductTagMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductTagRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductTagService;
import lombok.RequiredArgsConstructor;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTagServiceImpl implements ProductTagService {

    private final ProductTagRepository productTagRepository;

    private final ProductTagMapper productTagMapper;

    private final TagServiceImpl tagService;

    public List<ProductTagResponseDto> CreateProductTags(List<ProductTagRequestDto> productTags, Long productId)
            throws BadRequestException {

        List<Long> tagIds =  productTags.stream().map(ProductTagRequestDto::getTagId).toList();

        List<TagResponseDto> tags = tagService.findByTagIdIn(tagIds);

        List<ProductTagResponseDto> tagResponseDtos = productTags.stream().map(tagRequest -> {
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

        return tagResponseDtos;
    }

    public List<ProductTagEntity> convertToEntities(List<ProductTagRequestDto> dtoList, Long productId,
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


    public List<ProductTagResponseDto> findTagsByProductId(Long productId) {

        List<ProductTagEntity> productTagEntities = productTagRepository.findByProductId(productId);
        List<TagResponseDto> tagResponseDtos =
                tagService.findByTagIdIn(productTagEntities.stream().map(ProductTagEntity::getTagId).toList());

        List<ProductTagResponseDto> response = new ArrayList<>();

        for(ProductTagEntity productTagEntity : productTagEntities) {
            TagResponseDto tagResponseDto = tagResponseDtos
                            .stream()
                            .filter(t -> t.getTagId().equals(productTagEntity.getTagId()))
                            .findFirst()
                            .orElse(null);

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
}
