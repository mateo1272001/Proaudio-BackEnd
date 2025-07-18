package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductProjectServiceImpl implements ProductProjectService {

    private final ProductProjectRepository productProjectRepository;

    private final ProductProjectMapper productProjectMapper;

    private static ProductProjectEntity makeProductProjectEntity(ProductProjectRequestDto productProjectRequestDto) {
        ProductProjectEntity productProjectEntity = new ProductProjectEntity();

        productProjectEntity.setProductId(productProjectRequestDto.getProductId());
        productProjectEntity.setProjectId(productProjectRequestDto.getProjectId());
        productProjectEntity.setRentPriceId(productProjectRequestDto.getRentPriceId());
        productProjectEntity.setAmount(
                Objects.nonNull(productProjectRequestDto.getAmount()) ? productProjectRequestDto.getAmount() : 1);
        productProjectEntity.setStatus(
                Objects.nonNull(productProjectRequestDto.getStatus()) ? productProjectRequestDto.getStatus() :
                        BasicEnumStatus.ENABLED);
        return productProjectEntity;
    }

    @Override
    public ProductProjectResponseDto createProductProject(ProductProjectRequestDto productProjectRequestDto,
                                                          List<ItemResponseDto> itemsOfProduct) {

        List<ProductProjectEntity> ppFromProject =
                productProjectRepository.findByProjectIdAndProductIdAndStatus(productProjectRequestDto.getProjectId(),
                        productProjectRequestDto.getProductId(), BasicEnumStatus.ENABLED);

        Integer totalOfProductInProject = ppFromProject.stream().mapToInt(ProductProjectEntity::getAmount).sum();

        if (itemsOfProduct.size() < totalOfProductInProject + productProjectRequestDto.getAmount()) {
            throw new BadRequestException("¡No hay suficientes artículos disponibles!");
        }

        ProductProjectEntity productProjectEntity = makeProductProjectEntity(productProjectRequestDto);

        productProjectEntity = productProjectRepository.save(productProjectEntity);

        return productProjectMapper.toDto(productProjectEntity);
    }

    @Override
    public ProductProjectResponseDto deleteProductProject(Long id) {

        ProductProjectEntity productProjectEntity =
                productProjectRepository.findByProductProjectIdAndStatus(id, BasicEnumStatus.ENABLED).orElseThrow(
                        () -> new BadRequestException("¡No existe vínculo entre este producto y este proyecto!"));

        productProjectEntity.setStatus(BasicEnumStatus.DISABLED);

        productProjectEntity = productProjectRepository.save(productProjectEntity);

        return productProjectMapper.toDto(productProjectEntity);
    }

}
