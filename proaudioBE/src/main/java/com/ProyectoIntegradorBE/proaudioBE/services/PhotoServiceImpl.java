package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.Utils.CollectionUtils;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PhotoResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.PhotoEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.PhotoMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.PhotoRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    private final PhotoMapper photoMapper;

    @Override
    public List<PhotoResponseDto> createPhotos(List<PhotoRequestDto> dtos, Long productId) {

        List <PhotoEntity> photoEntities = photoMapper.toEntityList(dtos);

        photoEntities.forEach(photo -> {
            photo.setProductId(productId);
            photo.setStatus(BasicEnumStatus.ENABLED);
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
        photoEntity.setUrl(dto.getName());
        photoEntity.setStatus(BasicEnumStatus.ENABLED);

        photoEntity = photoRepository.save(photoEntity);

        return photoMapper.toDto(photoEntity);
    }

    public List<PhotoResponseDto> findPhotosByProductId(Long productId) {

        List<PhotoEntity> photoEntities = photoRepository.findByProductId(productId);

        return photoMapper.toDtoList(photoEntities);

    }
//
//    public List<PhotoResponseDto> UpdatePhoto(MultipartFile file, Long productId, String name) {
//
//        PhotoEntity photoEntity = new PhotoEntity();
//        photoEntity.setName(name);
//
//
//    }
}
