package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;

public interface ProductProjectService {
    ProductProjectResponseDto createProductProject(ProductProjectRequestDto productProjectRequestDto);

}
