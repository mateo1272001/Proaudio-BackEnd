package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductTagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ClientNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagModuleMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.TagRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.TagService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ProyectoIntegradorBE.proaudioBE.Utils.AppConstants.BRAND_TAG_KEY;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    private final TagModuleMapper tagModuleMapper;

    private final ProductTagServiceImpl productTagService;

    @Override
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {

        if (tagRequestDto.getName().equalsIgnoreCase(BRAND_TAG_KEY) &&
                tagRepository.findByNameAndStatus(BRAND_TAG_KEY, BasicEnumStatus.ENABLED).isPresent()) {

            throw new BadRequestException("¡No se puede agregar otra etiqueta de nombre 'Marca'");

        }


        if(Objects.nonNull(tagRequestDto.getFatherId())){
            Long fatherId = tagRequestDto.getFatherId();
            tagRepository.findById(fatherId).orElseThrow(() -> new ClientNotFoundException(fatherId));
        }

        TagEntity entity = tagMapper.toEntity(tagRequestDto);
        entity = tagRepository.save(entity);

        return tagMapper.toDto(entity);
    }

    @Override
    public TagResponseDto updateTag(Long id, TagRequestDto tagRequestDto) throws BadRequestException {

        TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        if (tag.getName().equalsIgnoreCase(BRAND_TAG_KEY)) {
            throw new BadRequestException("¡No se puede editar esta etiqueta!");
        }

        ValidateUpdate(tagRequestDto, tag);
        if(tag.getStatus().equals(BasicEnumStatus.ENABLED)
                && tagRequestDto.getStatus().equals(BasicEnumStatus.DISABLED)) {
            ValidateDelete(tag);
        }

        tag.setName(tagRequestDto.getName());
        tag.setDescription(tagRequestDto.getDescription());
        tag.setFatherId(tagRequestDto.getFatherId());
        tag.setStatus(tagRequestDto.getStatus());

        tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    private void ValidateUpdate(TagRequestDto tagRequestDto, TagEntity tag) throws BadRequestException {
        if(Objects.nonNull(tagRequestDto.getFatherId()) && tagRequestDto.getFatherId().equals(tag.getTagId())) {
            throw new BadRequestException("¡El nuevo padre no puede ser la misma etiqueta!");
        }

        checkIfNewFatherIsChildOfId(tagRequestDto.getFatherId(), tag.getTagId());
    }

    private void checkIfNewFatherIsChildOfId(Long selectedId, Long tagId) throws BadRequestException {

        List<TagEntity> tags = tagRepository.findByFatherIdAndStatus(tagId, BasicEnumStatus.ENABLED);

        if(Objects.nonNull(tags) && !tags.isEmpty()) {

            for (TagEntity tag : tags) {

                if(tag.getTagId().equals(selectedId)) {

                    throw new BadRequestException("¡El nuevo padre no puede ser hijo de la etiqueta editada!");

                }

                checkIfNewFatherIsChildOfId(selectedId, tag.getTagId());

            }

        }

    }

    public Boolean checkIfTagIsChildOfSelected(Long tagId, Long selectedId) {

        TagEntity currentTag = tagRepository.findById(tagId).orElseThrow(() -> new InternalException(
                "Error interno chequeando si %s es hijo de %s".formatted(tagId, selectedId)));

        if (Objects.isNull(currentTag.getFatherId())) {
            return false;
        }

        if (currentTag.getFatherId().equals(selectedId)) {
            return true;
        }

        return checkIfTagIsChildOfSelected(currentTag.getFatherId(), selectedId);

    }

    @Override
    public TagResponseDto deleteTag(Long id) throws BadRequestException {

        TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        if (tag.getName().equalsIgnoreCase(BRAND_TAG_KEY)) {
            throw new BadRequestException("¡No se puede borrar esta etiqueta!");
        }

        ValidateDelete(tag);

        tag.setStatus(BasicEnumStatus.DISABLED);
        tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    private void ValidateDelete(TagEntity tag) throws BadRequestException {

        List<TagEntity> tags =  tagRepository.findByFatherIdAndStatus(tag.getTagId(), BasicEnumStatus.ENABLED);

        if(!tags.isEmpty()) {
            throw new BadRequestException("¡No podés borrar etiquetas con hijos activos!");
        }

        List<ProductTagResponseDto> productTagResponseDtos = productTagService.findProductTagsByTagId(tag.getTagId());
        if (!productTagResponseDtos.isEmpty()) {
            throw new BadRequestException("¡No podés borrar etiquetas con productos asociados!");
        }
    }

    @Override
    public AllTagsModuleListDto findAllStructured() {

        List<AllTagsModuleDto> parentTagsList = getTagsRecursive(null);
        AllTagsModuleListDto allTagsResponsedto = new AllTagsModuleListDto();
        allTagsResponsedto.setParentTags(parentTagsList);

        return allTagsResponsedto;
    }

    private List<AllTagsModuleDto> getTagsRecursive(Long tagId) {

        List<TagEntity> tags = Objects.nonNull(tagId) ?
                tagRepository.findByFatherIdAndStatus(tagId, BasicEnumStatus.ENABLED) :
                tagRepository.findByFatherIdIsNullAndStatus(BasicEnumStatus.ENABLED);

        if(Objects.nonNull(tags)){

            List<AllTagsModuleDto> tagDtos = tagModuleMapper.toListDto(tags);

            for (AllTagsModuleDto tagDto : tagDtos) {

                List<AllTagsModuleDto> childTags = getTagsRecursive(tagDto.getTagId());
                tagDto.setChildTags(childTags);

            }

            return tagDtos;
        }

        return null;
    }


    @Override
    public TagResponseListDto findAllSimple() {

        List<TagEntity> tagEntities = tagRepository.findAllByStatus(BasicEnumStatus.ENABLED).stream()
                .toList();

        TagResponseListDto tagResponseListDto = new TagResponseListDto();
        tagResponseListDto.setTags(tagMapper.toListDto(tagEntities));

        return tagResponseListDto;
    }

    @Override
    public List<TagResponseDto> findByTagIdIn(List<Long> tagIds) {

        List<TagEntity> tagEntities = tagRepository.findByTagIdIn(tagIds);

        return tagMapper.toListDto(tagEntities);

    }

    @Override
    public Optional<TagEntity> findByTagId(@NotNull Long tagId) {

        return tagRepository.findById(tagId);

    }

    public TagTypesResponseDto findTagTypes() {

        return new TagTypesResponseDto(Arrays.stream(TagTypeEnum.values()).toList());

    }

    @Override
    public TagEntity findBrandRoot() {

        return tagRepository.findByNameAndStatus(BRAND_TAG_KEY, BasicEnumStatus.ENABLED)
                .orElseThrow(() -> new InternalException("¡No hay etiqueta base MARCA!"));

    }

    @Override
    public Boolean checkIfGivenTagIsParent(Long tagId, Long searchingTag) {

        if (searchingTag.equals(tagId)) {
            return true;
        }

        TagEntity tag = findByTagId(tagId).orElseThrow(() -> new InternalException("Problema buscando etiquetas"));

        if (Objects.isNull(tag.getFatherId())) {
            return false;
        }

        return checkIfGivenTagIsParent(tag.getFatherId(), searchingTag);

    }

    @Override
    public TagResponseDto mapTag(TagEntity tag) {
        return tagMapper.toDto(tag);
    }
}
