package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.Utils.CollectionUtils;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.PhotoEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.ImagesNotFoundException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.PhotoMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.PhotoRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    @Override
    public List<PhotoResponseDto> createPhotos(List<PhotoRequestDto> dtos, Long productId, MultipartFile file) {

        List <PhotoEntity> photoEntities = photoMapper.toEntityList(dtos);

        photoEntities.forEach(photo -> {
            photo.setProductId(productId);
            photo.setStatus(BasicEnumStatus.ENABLED);
            try {
                photo.setData(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo de imagen", e);
            }
        });
        photoEntities = CollectionUtils.toList(photoRepository.saveAll(photoEntities));

        return photoMapper.toDtoList(photoEntities);
    }
    @Override
    public List<PhotoResponseDto> updatePhotos(List<PhotoRequestDto> dtos, Long productId) {
        return List.of();
    }

    @Override
    public PhotoResponseDto deletePhoto(Long id) {

        Optional<PhotoEntity> photoEntityOpt = photoRepository.findByPhotoIdAndStatus(id, BasicEnumStatus.ENABLED);

        if(photoEntityOpt.isEmpty()) {
            throw new BadRequestException("¡Esta foto no existe!");
        }

        photoEntityOpt.get().setStatus(BasicEnumStatus.DISABLED);
        PhotoEntity photoEntity = photoRepository.save(photoEntityOpt.get());

        return photoMapper.toDto(photoEntity);
    }

    @Override
    public PhotoResponseDto createPhoto(PhotoRequestDto dto, Long productId) {

        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setProductId(productId);
        photoEntity.setName(dto.getName());
//        photoEntity.setUrl(dto.getName());
        photoEntity.setStatus(BasicEnumStatus.ENABLED);

        photoEntity = photoRepository.save(photoEntity);

        return photoMapper.toDto(photoEntity);
    }

    public List<PhotoResponseDto> findPhotosByProductId(Long productId) {

        List<PhotoEntity> photoEntities = photoRepository.findByProductIdAndStatus(productId, BasicEnumStatus.ENABLED);

        if(photoEntities.isEmpty()) {
            throw new ImagesNotFoundException(productId);
        }

        List<PhotoResponseDto> response = new ArrayList<>();

        photoEntities.forEach(photo -> response.add(CreateResponseImage(photo)));

        return response;

    }

    public PhotoResponseDto CreateResponseImage(PhotoEntity photo) {

        PhotoResponseDto dto = new PhotoResponseDto();
        dto.setPhotoId(photo.getPhotoId());
        dto.setProductId(photo.getProductId());
        dto.setName(photo.getName());
        dto.setStatus(photo.getStatus());
        dto.setImage(Base64.getEncoder().encodeToString(photo.getData()));

        return dto;
    }

    public PhotoResponseDto UploadPhoto(MultipartFile file, Long productId, String name) {

        try{
            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setProductId(productId);
            photoEntity.setData(file.getBytes());
            photoEntity.setName(name);
            photoEntity.setStatus(BasicEnumStatus.ENABLED);

            photoEntity = photoRepository.save(photoEntity);

            return photoMapper.toDto(photoEntity);

        } catch (Exception exception) {
            throw new BadRequestException(String.format("Error al subir imagen%n%s", exception.getMessage()));
        }

    }

    public List<PhotoResponseDto> UploadMultiplePhotos(MultipartFile[] files, Long productId) {

        List<PhotoResponseDto> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = Objects.requireNonNull(file.getOriginalFilename()).substring(0,9);
            PhotoResponseDto response = UploadPhoto(file, productId, fileName);
            responses.add(response);
        }

        return responses;
    }
}
