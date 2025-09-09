package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.*;
import com.ProyectoIntegradorBE.proaudioBE.services.TagServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagServiceImpl tagServiceImpl;


    @PostMapping()
    public TagResponseDto createTag(@Valid @RequestBody TagRequestDto tagRequestDto) {

        return tagServiceImpl.createTag(tagRequestDto);

    }

    @PutMapping("{id}")
    public TagResponseDto updateTag(@PathVariable Long id, @Valid @RequestBody TagRequestDto tagRequestDto)
            throws BadRequestException {

        return tagServiceImpl.updateTag(id, tagRequestDto);

    }

    @DeleteMapping("{id}")
    public TagResponseDto deleteTag(@PathVariable Long id) throws BadRequestException {

        return tagServiceImpl.deleteTag(id);

    }

    @GetMapping("/structured/all")
    public AllTagsModuleListDto getAllStructured() {

        return tagServiceImpl.findAllStructured();

    }

    @GetMapping("/all")
    public TagResponseListDto getAllTags() {

        return tagServiceImpl.findAllSimple();

    }

    @GetMapping("/types")
    public TagTypesResponseDto getTagTypes() {

        return tagServiceImpl.findTagTypes();

    }

}
