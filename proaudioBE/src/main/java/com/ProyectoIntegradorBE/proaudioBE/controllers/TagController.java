package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsModuleListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.services.TagServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public TagResponseDto updateTag(@PathVariable Long id, @Valid @RequestBody TagRequestDto tagRequestDto) {

        return tagServiceImpl.updateTag(id, tagRequestDto);

    }

    @DeleteMapping("{id}")
    public TagResponseDto deleteTag(@PathVariable Long id) {

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

}
