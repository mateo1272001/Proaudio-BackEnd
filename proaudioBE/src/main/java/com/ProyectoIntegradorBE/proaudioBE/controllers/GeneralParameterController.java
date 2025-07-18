package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterListReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters.GeneralParameterResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.GeneralParameterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parameter")
public class GeneralParameterController {

    private final GeneralParameterService generalParameterService;

    @PostMapping()
    private GeneralParameterResponseDto CreateParameter(
            @Valid @RequestBody GeneralParameterRequestDto generalParameterRequestDto) {

        return generalParameterService.CreateParameter(generalParameterRequestDto);

    }

    @PutMapping("{id}")
    private GeneralParameterResponseDto UpdateParameter(@PathVariable Long id, @RequestBody
    GeneralParameterRequestDto generalParameterRequestDto) {

        return generalParameterService.UpdateParameter(id, generalParameterRequestDto);

    }

    @DeleteMapping("{id}")
    private GeneralParameterResponseDto DeleteParameter(@PathVariable Long id) {

        return generalParameterService.DeleteParameter(id);

    }

    @GetMapping("{id}")
    private GeneralParameterResponseDto GetParameter(@PathVariable Long id) {
        return generalParameterService.GetParameter(id);
    }

    @GetMapping()
    private GeneralParameterListReponseDto GetParameters() {
        return generalParameterService.GetAllParameters();
    }

    @GetMapping("/key")
    private GeneralParameterListReponseDto GetParametersByKey(@RequestParam String key) {
        return generalParameterService.GetParametersByKey(key);
    }

}
