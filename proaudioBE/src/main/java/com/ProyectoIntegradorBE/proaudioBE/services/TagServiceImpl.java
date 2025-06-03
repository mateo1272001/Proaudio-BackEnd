package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.*;
import com.ProyectoIntegradorBE.proaudioBE.entities.ClientEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ClientNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagModuleMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

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
    public TagResponseDto updateTag(Long id, TagRequestDto tagRequestDto) {

        TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        tag.setName(tagRequestDto.getName());
        tag.setDescription(tagRequestDto.getDescription());
        tag.setFatherId(tagRequestDto.getFatherId());
        tag.setStatus(tagRequestDto.getStatus());

        tagRepository.save(tag);
        return tagMapper.toDto(tag);
    }

    @Override
    public TagResponseDto deleteTag(Long id) {

        TagEntity tag = tagRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        tag.setStatus(BasicEnumStatus.DISABLED);

        tagRepository.save(tag);
        return tagMapper.toDto(tag);
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

        List<TagEntity> tagEntities = StreamSupport
                .stream(tagRepository.findAll().spliterator(), false)
                .toList();

        TagResponseListDto tagResponseListDto = new TagResponseListDto();
        tagResponseListDto.setTags(tagMapper.toListDto(tagEntities));

        return tagResponseListDto;
    }

    @Override
    public TagResponseDto getTagById(Long id) {
        TagEntity clientEntity = tagRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return tagMapper.toDto(clientEntity);
    }

}
