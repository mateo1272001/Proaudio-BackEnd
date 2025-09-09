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
    public List<PhotoResponseDto> createPhotos(Long productId, List<MultipartFile> photos) {

        List<PhotoEntity> photoEntities = new ArrayList<>();

        for(MultipartFile photo : photos) {

            PhotoEntity photoEntity = new PhotoEntity();
            photoEntity.setProductId(productId);
            photoEntity.setStatus(BasicEnumStatus.ENABLED);

            String name = Objects.nonNull(photo.getOriginalFilename())
                    ? photo.getOriginalFilename().length() > 20
                        ? photo.getOriginalFilename().substring(0, 20)
                        : photo.getOriginalFilename()
                    : "Foto de producto";
            photoEntity.setName(name);

            try {
                photoEntity.setData(photo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo de imagen", e);
            }

            photoEntities.add(photoEntity);
        }

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

}
