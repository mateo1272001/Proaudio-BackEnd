package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.RelationGroupEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagRelationGroupEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.RelationGroupMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagRelationGroupMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.RelationGroupRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.TagRelationGroupRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.RelationGroupService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RelationGroupServiceImpl implements RelationGroupService {

    private final RelationGroupRepository relationGroupRepository;

    private final TagRelationGroupRepository tagRelationGroupRepository;

    private final RelationGroupMapper relationGroupMapper;

    private final TagRelationGroupMapper tagRelationGroupMapper;

    private final TagService tagService;

    @Override
    @Transactional
    public RelationGroupDto createTagRelationGroup(RelationGroupAndTagDto relationGroupAndTagDto, TagEntity brandRoot) {

        if (Objects.isNull(relationGroupAndTagDto.getTag())) {
            throw new BadRequestException("¡Debe tener una etiqueta asociada!");
        }

        groupTagValidations(relationGroupAndTagDto, brandRoot);

        RelationGroupDto relationGroupDto = getOrCreateRelationGroupEntity(relationGroupAndTagDto);

        getOrCreateTagRelationGroup(relationGroupAndTagDto.getTag(), relationGroupDto);

        return relationGroupDto;

        //todo: get products owned by group and add them to the response. change getOrCreateTagRelationGroup to use the tagGroup list in logic

    }

    private void groupTagValidations(RelationGroupAndTagDto relationGroupAndTagDto, TagEntity brandRoot) {

        if (relationGroupAndTagDto.getType().equals(TagTypeEnum.DESCRIPTIVE)) {

            List<RelationGroupEntity> relationGroupEntities = relationGroupRepository.findByProductIdAndTypeAndStatus(
                    relationGroupAndTagDto.getProduct().getProductId(), relationGroupAndTagDto.getType(),
                    BasicEnumStatus.ENABLED);

            if (!relationGroupEntities.isEmpty()) {

                RelationGroupEntity relationGroupEntity = relationGroupEntities.getLast();

                if (!relationGroupEntity.getRelationGroupId().equals(relationGroupAndTagDto.getRelationGroupId())) {
                    throw new BadRequestException("¡No se pueden tener dos grupos descriptivos distintos activos!");
                }

                TagResponseDto tag = relationGroupAndTagDto.getTag();
                if (tag.getFatherId().equals(brandRoot.getTagId())) {

                    List<TagRelationGroupWithTagInfoDto> tags =
                            tagRelationGroupRepository.findByRelationGroupIdWithTagInfo(
                                    relationGroupEntity.getRelationGroupId());

                    if (tags.stream().anyMatch(t -> t.getFatherId().equals(brandRoot.getTagId()))) {
                        throw new BadRequestException("¡Ya hay una etiqueta de marca asociada a este producto!");
                    }

                }
            }
        }

    }

    public List<TagRelationGroupEntity> getTagRelationGroupByRelationGroupAndStatus(Long relationGroupId,
                                                                                    BasicEnumStatus status) {

        return tagRelationGroupRepository.findByRelationGroupIdAndStatus(relationGroupId, status);

    }

    private TagRelationGroupDto getOrCreateTagRelationGroup(TagResponseDto tag, RelationGroupDto relationGroupDto) {

        if (relationGroupDto.getIsNew()) {

            return createTagRelationGroup(relationGroupDto, tag);

        } else {

            Optional<TagRelationGroupEntity> tagRelationGroupEntityOptional =
                    tagRelationGroupRepository.findByTagIdAndRelationGroupId(tag.getTagId(),
                            relationGroupDto.getRelationGroupId());

            if (tagRelationGroupEntityOptional.isEmpty()) {

                return createTagRelationGroup(relationGroupDto, tag);
            }

            TagRelationGroupEntity tagRelationGroupEntity = tagRelationGroupEntityOptional.get();

            if (tagRelationGroupEntity.getStatus().equals(BasicEnumStatus.DISABLED)) {

                tagRelationGroupEntity.setStatus(BasicEnumStatus.ENABLED);
                tagRelationGroupRepository.save(tagRelationGroupEntity);
            }

            throw new BadRequestException("¡Esta etiqueta ya se encuentra asociada al producto!");
        }
    }

    private TagRelationGroupDto createTagRelationGroup(RelationGroupDto relationGroupDto, TagResponseDto tag) {
        TagRelationGroupEntity tagRelationGroupEntity = new TagRelationGroupEntity();
        tagRelationGroupEntity.setTagId(tag.getTagId());
        tagRelationGroupEntity.setRelationGroupId(relationGroupDto.getRelationGroupId());
        tagRelationGroupEntity.setStatus(BasicEnumStatus.ENABLED);
        tagRelationGroupEntity = tagRelationGroupRepository.save(tagRelationGroupEntity);
        return tagRelationGroupMapper.toDto(tagRelationGroupEntity);
    }

    private RelationGroupDto getOrCreateRelationGroupEntity(RelationGroupAndTagDto relationGroupAndTagDto) {

        if (Objects.isNull(relationGroupAndTagDto.getRelationGroupId())) {

            RelationGroupEntity relationGroupEntity = new RelationGroupEntity();
            relationGroupEntity.setProductId(relationGroupAndTagDto.getProduct().getProductId());
            relationGroupEntity.setType(relationGroupAndTagDto.getType());
            relationGroupEntity.setStatus(BasicEnumStatus.ENABLED);
            relationGroupEntity.setName(Objects.isNull(relationGroupAndTagDto.getName()) ?
                    String.format("group %s", relationGroupAndTagDto.getRelationGroupId()) :
                    relationGroupAndTagDto.getName());
            relationGroupEntity = relationGroupRepository.save(relationGroupEntity);

            RelationGroupDto relationGroupDto = relationGroupMapper.toDto(relationGroupEntity);
            relationGroupDto.setIsNew(true);

            return relationGroupDto;
        }

        return findRelationGroupById(relationGroupAndTagDto.getRelationGroupId());
    }

    private RelationGroupDto findRelationGroupById(Long relationGroupId) {

        Optional<RelationGroupEntity> relationGroupEntityOptional =
                relationGroupRepository.findByRelationGroupId(relationGroupId);

        if (relationGroupEntityOptional.isEmpty()) {
            throw new BadRequestException("¡No existe este groupo de etiquetas!");
        }

        return relationGroupMapper.toDto(relationGroupEntityOptional.get());
    }

    @Override
    @Transactional
    public RelationGroupResponseDto deleteTagRelationGroup(Long relationGroupId, Long tagId) {

        RelationGroupEntity relationGroupEntity = relationGroupRepository.findByRelationGroupId(relationGroupId)
                .orElseThrow(() -> new BadRequestException("No existe este grupo de etiquetas!"));

        if (relationGroupEntity.getStatus().equals(BasicEnumStatus.DISABLED)) {
            throw new BadRequestException("El grupo de etiquetas ya esta deshabilitado!");
        }

        Optional<TagRelationGroupEntity> tagRelationGroupOpt =
                tagRelationGroupRepository.findByTagIdAndRelationGroupId(tagId, relationGroupId);

        if (tagRelationGroupOpt.isEmpty() || tagRelationGroupOpt.get().getStatus().equals(BasicEnumStatus.DISABLED)) {
            throw new BadRequestException("No existe vinculo actual entre este grupo y esta etiqueta!");
        }

        TagRelationGroupEntity tagRelationGroupEntity = tagRelationGroupOpt.get();
        tagRelationGroupEntity.setStatus(BasicEnumStatus.DISABLED);
        tagRelationGroupRepository.save(tagRelationGroupEntity);

        validateTagsLeft(relationGroupId, relationGroupEntity);

        return buildProductTagGroupedResponse(relationGroupEntity);
    }

    private void validateTagsLeft(Long relationGroupId, RelationGroupEntity relationGroupEntity) {
        List<TagRelationGroupEntity> tagRelationGroupEntities =
                tagRelationGroupRepository.findByRelationGroupIdAndStatus(relationGroupId, BasicEnumStatus.ENABLED);

        if (tagRelationGroupEntities.isEmpty()) {
            relationGroupEntity.setStatus(BasicEnumStatus.DISABLED);
            relationGroupRepository.save(relationGroupEntity);
        }
    }

    @Override
    @Transactional
    public RelationGroupResponseDto deleteRelationGroup(Long groupId) {

        RelationGroupEntity relationGroupEntity = relationGroupRepository.findByRelationGroupId(groupId)
                .orElseThrow(() -> new BadRequestException("No existe este grupo de etiquetas!"));

        if (relationGroupEntity.getStatus().equals(BasicEnumStatus.DISABLED)) {
            throw new BadRequestException("El grupo de etiquetas ya esta deshabilitado!");
        }

        List<TagRelationGroupEntity> tagRelationGroups =
                tagRelationGroupRepository.findByRelationGroupIdAndStatus(groupId, BasicEnumStatus.ENABLED);

        for (TagRelationGroupEntity tagRelationGroupEntity : tagRelationGroups) {
            tagRelationGroupEntity.setStatus(BasicEnumStatus.DISABLED);
        }

        if (!tagRelationGroups.isEmpty()) {
            tagRelationGroupRepository.saveAll(tagRelationGroups);
        }

        relationGroupEntity.setStatus(BasicEnumStatus.DISABLED);
        relationGroupRepository.save(relationGroupEntity);

        return buildProductTagGroupedResponse(relationGroupEntity);
    }

    private RelationGroupResponseDto buildProductTagGroupedResponse(RelationGroupEntity relationGroupEntity) {
        List<TagRelationGroupEntity> tagRelationGroups =
                getTagRelationGroupByRelationGroupAndStatus(relationGroupEntity.getRelationGroupId(),
                        BasicEnumStatus.ENABLED);

        List<TagResponseDto> tagResponseDtos = tagRelationGroups.isEmpty() ? List.of() :
                tagService.findByTagIdIn(tagRelationGroups.stream().map(TagRelationGroupEntity::getTagId).toList());

        RelationGroupResponseDto relationGroupResponseDto = new RelationGroupResponseDto();
        relationGroupResponseDto.setRelationGroupId(relationGroupEntity.getRelationGroupId());
        relationGroupResponseDto.setProductId(relationGroupEntity.getProductId());
        relationGroupResponseDto.setType(relationGroupEntity.getType());
        relationGroupResponseDto.setStatus(relationGroupEntity.getStatus());
        relationGroupResponseDto.setTags(tagResponseDtos);

        return relationGroupResponseDto;
    }

    @Override
    public RelationGroupResponseDtoList getRelationGroupsByProduct(Long productId) {

        List<RelationGroupEntity> relationGroupEntities =
                relationGroupRepository.findByProductIdAndStatus(productId, BasicEnumStatus.ENABLED);

        RelationGroupResponseDtoList relationGroupResponseDtoList = new RelationGroupResponseDtoList();
        relationGroupResponseDtoList.setRelation(new ArrayList<>());
        relationGroupResponseDtoList.setDependency(new ArrayList<>());

        for (RelationGroupEntity relationGroupEntity : relationGroupEntities) {

            RelationGroupResponseDto relationGroupResponseDto = buildRelationGroupResponseDto(relationGroupEntity);

            switch (relationGroupResponseDto.getType()) {
                case DESCRIPTIVE -> relationGroupResponseDtoList.setDescriptive(relationGroupResponseDto);
                case RELATION -> relationGroupResponseDtoList.getRelation().add(relationGroupResponseDto);
                case DEPENDENCY -> relationGroupResponseDtoList.getDependency().add(relationGroupResponseDto);
            }

        }

        return relationGroupResponseDtoList;
    }

    private RelationGroupResponseDto buildRelationGroupResponseDto(RelationGroupEntity relationGroupEntity) {

        RelationGroupResponseDto relationGroupResponseDto = new RelationGroupResponseDto();
        relationGroupResponseDto.setProductId(relationGroupEntity.getProductId());
        relationGroupResponseDto.setType(relationGroupEntity.getType());
        relationGroupResponseDto.setStatus(relationGroupEntity.getStatus());
        relationGroupResponseDto.setRelationGroupId(relationGroupEntity.getRelationGroupId());
        relationGroupResponseDto.setName(relationGroupEntity.getName());

        List<TagRelationGroupEntity> tagRelationGroupEntities =
                tagRelationGroupRepository.findByRelationGroupIdAndStatus(relationGroupEntity.getRelationGroupId(),
                        BasicEnumStatus.ENABLED);

        List<TagResponseDto> tags = tagService.findByTagIdIn(
                tagRelationGroupEntities.stream().map(TagRelationGroupEntity::getTagId).toList());

        relationGroupResponseDto.setTags(tags);
        return relationGroupResponseDto;
    }

    @Transactional
    public void createMultipleRelationGroups(List<ProductTagRequestDto> productTagRequestDtos,
                                             ProductResponseDto product) {

        Map<String, Long> relationGroupName = new HashMap<>();

        for (ProductTagRequestDto productTagRequestDto : productTagRequestDtos) {

            productTagRequestDto.setProductId(product.getProductId());

            if (productTagRequestDto.getType().equals(TagTypeEnum.DESCRIPTIVE)) {
                productTagRequestDto.setName("Valores Descriptivos");
            }

            TagResponseDto tagResponseDto = tagValidations(product, productTagRequestDto);

            RelationGroupDto relationGroupDto = ObtainRelationGroup(productTagRequestDto, relationGroupName);

            getOrCreateTagRelationGroup(tagResponseDto, relationGroupDto);

        }

    }

    private TagResponseDto tagValidations(ProductResponseDto product, ProductTagRequestDto productTagRequestDto) {
        TagResponseDto tagResponseDto = validateTag(productTagRequestDto);

        RelationGroupAndTagDto relationGroupAndTagDto =
                new RelationGroupAndTagDto(tagResponseDto, productTagRequestDto.getType(), product,
                        productTagRequestDto.getRelationGroupId(), productTagRequestDto.getName());

        //        groupTagValidations(relationGroupAndTagDto, tagService.findBrandRoot());
        return tagResponseDto;
    }

    private TagResponseDto validateTag(ProductTagRequestDto productTagRequestDto) {
        Optional<TagEntity> tagEntityOpt = tagService.findByTagId(productTagRequestDto.getTagId());

        if (tagEntityOpt.isEmpty()) {
            throw new BadRequestException("¡La etiqueta no existe!");
        }

        return tagService.mapTag(tagEntityOpt.get());
    }

    private RelationGroupDto ObtainRelationGroup(ProductTagRequestDto productTagRequestDto,
                                                 Map<String, Long> relationGroupName) {

        if (Objects.nonNull(productTagRequestDto.getRelationGroupId())) {
            return obtainThoughId(productTagRequestDto);
        }

        if (Objects.nonNull(productTagRequestDto.getName())) {
            return obtainThroughName(productTagRequestDto, relationGroupName);
        }

        throw new BadRequestException("¡Se debe seleccionar o crear un grupo de etiquetas!");
    }

    private RelationGroupDto obtainThroughName(ProductTagRequestDto productTagRequestDto,
                                               Map<String, Long> relationGroupName) {
        if (Objects.nonNull(relationGroupName.get(productTagRequestDto.getName()))) {

            Long relationGroupId = relationGroupName.get(productTagRequestDto.getName());
            return findRelationGroupById(relationGroupId);

        } else {

            RelationGroupDto relationGroupDto = createRelationGroup(productTagRequestDto);
            relationGroupName.put(relationGroupDto.getName(), relationGroupDto.getRelationGroupId());
            return relationGroupDto;
        }
    }

    private RelationGroupDto obtainThoughId(ProductTagRequestDto productTagRequestDto) {
        if (Objects.nonNull(productTagRequestDto.getName())) {
            throw new BadRequestException("¡No se puede seleccionar un grupo y crear otro a la vez!");
        }

        return findRelationGroupById(productTagRequestDto.getRelationGroupId());
    }

    private RelationGroupDto createRelationGroup(ProductTagRequestDto productTagRequestDto) {

        RelationGroupEntity relationGroupEntity = new RelationGroupEntity();
        relationGroupEntity.setProductId(productTagRequestDto.getProductId());
        relationGroupEntity.setType(productTagRequestDto.getType());
        relationGroupEntity.setStatus(BasicEnumStatus.ENABLED);
        relationGroupEntity.setName(productTagRequestDto.getName());
        relationGroupEntity = relationGroupRepository.save(relationGroupEntity);

        return relationGroupMapper.toDto(relationGroupEntity);

    }

}

