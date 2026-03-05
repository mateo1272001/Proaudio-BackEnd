package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsModuleListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface TagService {

    TagResponseDto createTag(TagRequestDto tagRequestDto);

    TagResponseDto updateTag(Long id, @Valid TagRequestDto tagRequestDto) throws BadRequestException;

    TagResponseDto deleteTag(Long id) throws BadRequestException;

    AllTagsModuleListDto findAllStructured();

    TagResponseListDto findAllSimple();

    List<TagResponseDto> findByTagIdIn(List<Long> tagIds);

    Optional<TagEntity> findByTagId(@NotNull Long tagId);

    TagEntity findBrandRoot();

    Boolean checkIfGivenTagIsParent(Long tagId, Long searchingTag);

    TagResponseDto mapTag(TagEntity tag);
}
