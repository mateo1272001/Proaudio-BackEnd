package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoResponseDto;

import java.util.List;

public interface PhotoService {

    PhotoResponseDto createPhoto(PhotoRequestDto dto, Long productId);

    List<PhotoResponseDto> createPhotos(List<PhotoRequestDto> dtos, Long productId);

    List<PhotoResponseDto> updatePhotos(List<PhotoRequestDto> dtos, Long productId);

}
