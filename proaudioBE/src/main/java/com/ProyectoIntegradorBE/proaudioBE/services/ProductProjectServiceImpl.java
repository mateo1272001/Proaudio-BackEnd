package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectWithModelResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductInProjectResponseDtoImpl;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProductsInProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProductProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ProductProjectMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ProductRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductProjectServiceImpl implements ProductProjectService {

    private final ProductProjectRepository productProjectRepository;

    private final ProductRepository productRepository;

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
    public ProductInProjectResponseDtoImpl createProductProject(ProductProjectRequestDto productProjectRequestDto,
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

        return getProductInProjectResponseDto(productProjectEntity);
    }

    private ProductInProjectResponseDtoImpl getProductInProjectResponseDto(ProductProjectEntity productProjectEntity) {
        ProductEntity productEntity = productRepository.findById(productProjectEntity.getProductId())
                .orElseThrow(() -> new BadRequestException("¡Este producto no está asociado al proyecto espeficiado!"));

        ProductInProjectResponseDtoImpl productInProjectResponseDto = new ProductInProjectResponseDtoImpl();
        productInProjectResponseDto.setProductProjectId(productProjectEntity.getProductProjectId());
        productInProjectResponseDto.setId(productProjectEntity.getProductId());
        productInProjectResponseDto.setModel(productEntity.getModel());
        productInProjectResponseDto.setComments(productEntity.getComments());
        productInProjectResponseDto.setAmount(productProjectEntity.getAmount());
        productInProjectResponseDto.setRentPrice(productProjectEntity.getRentPriceId());

        return productInProjectResponseDto;
    }

    @Override
    public ProductInProjectResponseDtoImpl deleteProductProject(Long id) {

        ProductProjectEntity productProjectEntity =
                productProjectRepository.findByProductProjectIdAndStatus(id, BasicEnumStatus.ENABLED).orElseThrow(
                        () -> new BadRequestException("¡No existe vínculo entre este producto y este proyecto!"));

        productProjectEntity.setStatus(BasicEnumStatus.DISABLED);

        productProjectEntity = productProjectRepository.save(productProjectEntity);

        return getProductInProjectResponseDto(productProjectEntity);
    }

    @Override
    public ProductsInProjectResponseDto getProductsInProject(Long id) {

        List<ProductInProjectResponseDto> productProjectDetailsDto =
                productProjectRepository.findProductProjectDetail(id);

        return new ProductsInProjectResponseDto(productProjectDetailsDto);
    }

    @Override
    public List<ProductProjectWithModelResponseDto> getByProductAndProjectId(Long productId, Long projectId) {

        return productProjectRepository.findByProductIdAndProjectIdAndStatus(productId, projectId,
                BasicEnumStatus.ENABLED);
    }

}
