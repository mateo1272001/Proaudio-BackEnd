package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsModuleListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseListDto;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;

public interface TagService {
    TagResponseDto createTag(TagRequestDto tagRequestDto);

    TagResponseDto updateTag(Long id, @Valid TagRequestDto tagRequestDto) throws BadRequestException;

    TagResponseDto deleteTag(Long id) throws BadRequestException;

    AllTagsModuleListDto findAllStructured();

    TagResponseListDto findAllSimple();
}
