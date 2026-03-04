package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagRelationGroupEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagMapper;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.RelationGroupService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class RelationGroupController {

    private final RelationGroupService relationGroupService;

    private final ProductService productService;

    private final TagService tagService;

    private final TagMapper tagMapper;

    @PostMapping("/tag")
    private RelationGroupResponseDto createRelationGroup(@RequestBody ProductTagRequestDto productTagRequestDto) {

        //product entity validation
        if (Objects.isNull(productTagRequestDto.getProductId())) {
            throw new com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException(
                    "¡Debe tener un producto asociado!");
        }

        ProductResponseDto productResponseDto = productService.GetProduct(productTagRequestDto.getProductId());

        //tag entity validation
        Optional<TagEntity> tagEntityOpt = tagService.findByTagId(productTagRequestDto.getTagId());

        if (tagEntityOpt.isEmpty()) {
            throw new com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException("¡La etiqueta no existe!");
        }

        //brand tag finding
        TagEntity brandRoot = tagService.findBrandRoot();

        //relation group and tag connection
        RelationGroupAndTagDto relationGroupAndTagDto =
                new RelationGroupAndTagDto(tagMapper.toDto(tagEntityOpt.get()), productTagRequestDto.getType(),
                        productResponseDto, productTagRequestDto.getRelationGroupId(), productTagRequestDto.getName());

        RelationGroupDto relationGroupDto =
                relationGroupService.createTagRelationGroup(relationGroupAndTagDto, brandRoot);

        //response creation
        List<TagRelationGroupEntity> tagRelationGroups =
                relationGroupService.getTagRelationGroupByRelationGroupAndStatus(relationGroupDto.getRelationGroupId(),
                        BasicEnumStatus.ENABLED);

        List<TagResponseDto> tagResponseDtos =
                tagService.findByTagIdIn(tagRelationGroups.stream().map(TagRelationGroupEntity::getTagId).toList());

        RelationGroupResponseDto relationGroupResponseDto = new RelationGroupResponseDto();
        relationGroupResponseDto.setRelationGroupId(relationGroupDto.getRelationGroupId());
        relationGroupResponseDto.setProductId(relationGroupDto.getProductId());
        relationGroupResponseDto.setName(relationGroupDto.getName());
        relationGroupResponseDto.setType(relationGroupDto.getType());
        relationGroupResponseDto.setStatus(relationGroupDto.getStatus());
        relationGroupResponseDto.setTags(tagResponseDtos);

        return relationGroupResponseDto;
    }

    @DeleteMapping("/{relationGroupId}/tag/{tagId}")
    private RelationGroupResponseDto deleteTagRelationGroup(@PathVariable Long relationGroupId,
                                                            @PathVariable Long tagId) {
        return relationGroupService.deleteTagRelationGroup(relationGroupId, tagId);
    }

    @DeleteMapping("/{groupId}")
    private RelationGroupResponseDto deleteRelationGroup(@PathVariable Long groupId) {
        return relationGroupService.deleteRelationGroup(groupId);
    }

    @GetMapping("/product/{productId}")
    private RelationGroupResponseDtoList getProductRelationGroups(@PathVariable Long productId) {

        ProductResponseDto productResponseDto = productService.GetProduct(productId);

        return relationGroupService.getRelationGroupsByProduct(productId);
    }

    //todo add more validations, delete relation groups, relation groups validations in products
}
