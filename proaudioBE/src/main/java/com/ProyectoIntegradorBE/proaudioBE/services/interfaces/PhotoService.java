package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    PhotoResponseDto createPhoto(PhotoRequestDto dto, Long productId);

    public List<PhotoResponseDto> createPhotos(ProductRequestDto productRequestDto, Long productId,
                                               List<MultipartFile> photos);

    List<PhotoResponseDto> updatePhotos(List<PhotoRequestDto> dtos, Long productId);

    PhotoResponseDto deletePhoto(Long id);
}