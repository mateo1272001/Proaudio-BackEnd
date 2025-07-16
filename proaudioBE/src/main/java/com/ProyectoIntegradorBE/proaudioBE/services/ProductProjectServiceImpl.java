package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductProjectMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductProjectServiceImpl implements ProductProjectService {

    private final ProductProjectRepository productProjectRepository;

    private final ProductProjectMapper productProjectMapper;

    @Override
    public ProductProjectResponseDto createProductProject(ProductProjectRequestDto productProjectRequestDto) {

        if (Objects.isNull(productProjectRequestDto.getProductId()) ||
                Objects.isNull(productProjectRequestDto.getProjectId())) {
            throw new BadRequestException("¡Se debe aclarar el proyecto y el producto para vincularlos!");
        }

        ProductProjectEntity productProjectEntity = new ProductProjectEntity();
        productProjectEntity.setProductId(productProjectRequestDto.getProductId());
        productProjectEntity.setProjectId(productProjectRequestDto.getProjectId());
        productProjectEntity.setRentPriceId(productProjectRequestDto.getRentPriceId());
        productProjectEntity.setAmount(
                Objects.nonNull(productProjectEntity.getAmount()) ? productProjectEntity.getAmount() : 1);
        productProjectEntity.setStatus(
                Objects.nonNull(productProjectRequestDto.getStatus()) ? productProjectRequestDto.getStatus() :
                        BasicEnumStatus.ENABLED);

        productProjectEntity = productProjectRepository.save(productProjectEntity);

        return productProjectMapper.toDto(productProjectEntity);
    }

}
