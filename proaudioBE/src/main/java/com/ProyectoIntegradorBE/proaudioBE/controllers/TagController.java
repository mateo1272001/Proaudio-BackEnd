package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.AllTagsResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
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
    public TagResponseDto updateTag(@PathVariable Long id) {

        return tagServiceImpl.deleteTag(id);

    }

    @GetMapping("/all")
    public AllTagsResponseDto updateTag() {

        return tagServiceImpl.findAll();

    }

}
