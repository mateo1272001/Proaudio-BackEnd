package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsModuleDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ClientNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.TagMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

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
    public AllTagsResponseDto findAll() {

        List<AllTagsModuleDto> parentTagsList = getTagsRecursive(null);
        AllTagsResponseDto allTagsResponsedto = new AllTagsResponseDto();
        allTagsResponsedto.setParentTags(parentTagsList);

        return allTagsResponsedto;
    }

    private List<AllTagsModuleDto> getTagsRecursive(Long tagId) {

        List<TagEntity> tags = Objects.nonNull(tagId) ?
                tagRepository.findByFatherIdAndStatus(tagId, BasicEnumStatus.ENABLED) :
                tagRepository.findByFatherIdIsNullAndStatus(BasicEnumStatus.ENABLED);

        if(Objects.nonNull(tags)){

            List<AllTagsModuleDto> tagDtos = tagMapper.toListDto(tags);

            for (AllTagsModuleDto tagDto : tagDtos) {

                List<AllTagsModuleDto> childTags = getTagsRecursive(tagDto.getTagId());
                tagDto.setChildTags(childTags);

            }

            return tagDtos;
        }

        return null;
    }

}
