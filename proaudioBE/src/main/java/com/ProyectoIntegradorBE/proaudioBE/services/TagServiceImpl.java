package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ClientNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.TagNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagModuleMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.TagRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.TagService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagModuleMapper tagModuleMapper;

    @Override
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {

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

    @Override
    public TagResponseDto deleteTag(Long id) throws BadRequestException {

        TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

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

        //todo agregar validacion de productos con la etiqueta asignada

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

    public List<TagResponseDto> findByTagIdIn(List<Long> tagIds) {

        List<TagEntity> tagEntities = tagRepository.findByTagIdIn(tagIds);

        return tagMapper.toListDto(tagEntities);

    }

    public Optional<TagEntity> findByTagId(@NotNull Long tagId) {

        return tagRepository.findById(tagId);

    }

    public TagResponseDto findByProductIdAndFatherId(Long productId, Long fatherId) {

        TagEntity tagEntity = tagRepository.findByProductIdAndFatherId(productId, fatherId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new TagNotFoundException(productId));

        return tagMapper.toDto(tagEntity);
    }
}
