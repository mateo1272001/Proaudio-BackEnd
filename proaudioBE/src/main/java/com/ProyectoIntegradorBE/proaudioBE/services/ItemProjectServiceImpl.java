package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseIntDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseListDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectWithModelResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Project.ProjectSimpleReponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ItemProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.ItemProjectMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.ItemProjectRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ItemProjectService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProductProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ProyectoIntegradorBE.proaudioBE.services.ItemServiceImpl.PROJECT_STARTED_STATUS;

@Service
@RequiredArgsConstructor
public class ItemProjectServiceImpl implements ItemProjectService {

    private final ProductProjectService productProjectService;

    private final ItemProjectRepository itemProjectRepository;

    private final ItemProjectMapper itemProjectMapper;

    private static ItemProjectEntity checkIfDeletionIsSuggested(ProjectSimpleReponseDto projectSimpleReponseDto,
                                                                Optional<ItemProjectEntity> itemProjectOpt) {
        ItemProjectEntity itemProjectEntity;
        itemProjectEntity = itemProjectOpt.get();

        if (itemProjectEntity.getStatus().equals(ItemProjectStatus.ENABLED)) {

            boolean projectStarted = PROJECT_STARTED_STATUS.contains(projectSimpleReponseDto.getStatus());

            if (projectStarted) {
                throw new BadRequestException(
                        "¡No podés sacar un artículo luego de empezado el proyecto! ¿Prefieres retornarlo?");
            } else {
                itemProjectEntity.setStatus(ItemProjectStatus.DISABLED);
            }
        }
        return itemProjectEntity;
    }

    @Override
    public ItemProjectResponseDto createItemProject(ItemResponseDto itemResponseDto, Long projectId,
                                                    List<ProductProjectWithModelResponseDto> productProjectWithModelResponseDtos) {

        ItemProjectEntity itemProjectEntity = new ItemProjectEntity();
        //        Optional<ItemProjectEntity> itemProjectOpt =
        //                itemProjectRepository.findByItemIdAndProjectId(itemResponseDto.getItemId(),
        //                        projectSimpleReponseDto.getProjectId());
        //        if (itemProjectOpt.isPresent()) {
        //            itemProjectEntity = checkIfDeletionIsSuggested(projectSimpleReponseDto, itemProjectOpt);
        //        }
        checkIfItemLimitIsSurpassed(itemResponseDto, projectId, productProjectWithModelResponseDtos, null);

        itemProjectEntity.setItemId(itemResponseDto.getItemId());
        itemProjectEntity.setProjectId(projectId);
        itemProjectEntity.setStatus(ItemProjectStatus.ENABLED);
        itemProjectEntity.setCreatedAt(LocalDateTime.now());
        itemProjectEntity = itemProjectRepository.save(itemProjectEntity);
        ItemProjectResponseDto itemProjectResponseDto = itemProjectMapper.toDto(itemProjectEntity);
        itemProjectResponseDto.setProductModel(
                productProjectWithModelResponseDtos.stream().map(ProductProjectWithModelResponseDto::getModel)
                        .findFirst().orElse(""));
        return itemProjectResponseDto;
    }

    public void checkIfItemLimitIsSurpassed(ItemResponseDto itemResponseDto, Long projectId,
                                            List<ProductProjectWithModelResponseDto> productProjectWithModelResponseDtos,
                                            ItemProjectEntity itemProjectEntity) {
        List<ItemProjectEntity> itemProjectEntityList =
                itemProjectRepository.findItemsOfProductInProject(itemResponseDto.getProductId(), projectId);

        int amountOfProducts = 0;

        for (ProductProjectWithModelResponseDto pp : productProjectWithModelResponseDtos) {
            amountOfProducts = amountOfProducts + pp.getAmount();
        }

        if (itemProjectEntityList.size() >= amountOfProducts && Objects.nonNull(itemProjectEntity) &&
                !itemProjectEntityList.contains(itemProjectEntity)) {
            throw new BadRequestException("¡Ya se alcanzó la cantidad de artículos necesarios!");
        }
    }

    @Override
    public ItemProjectResponseDto changeStatusItemProject(ItemResponseDto itemResponseDto,
                                                          ProjectSimpleReponseDto projectSimpleReponseDto,
                                                          ItemProjectStatus itemProjectStatus,
                                                          ItemProjectResponseDto itemProjectResponseDto) {
        //        ItemProjectEntity itemProjectEntity =
        //                itemProjectRepository.findByItemIdAndProjectIdAndStatusIn(itemResponseDto.getItemId(),
        //                                projectSimpleReponseDto.getProjectId(), List.of(ItemProjectStatus.ENABLED))
        //                        .orElseThrow(() -> new BadRequestException("¡Este artículo no se encuentra en este proyecto!"));
        ItemProjectEntity itemProjectEntity = new ItemProjectEntity();
        itemProjectEntity.setItemProjectId(itemProjectResponseDto.getItemProjectId());
        itemProjectEntity.setItemId(itemResponseDto.getItemId());
        itemProjectEntity.setProjectId(itemProjectResponseDto.getProjectId());
        itemProjectEntity.setCreatedAt(itemProjectResponseDto.getCreatedAt());

        itemProjectEntity.setStatus(itemProjectStatus);

        itemProjectEntity = itemProjectRepository.save(itemProjectEntity);

        return itemProjectMapper.toDto(itemProjectEntity);
    }

    @Override
    public ItemProjectResponseListDto getItemsInProject(Long projectId) {

        List<ItemProjectResponseIntDto> list =
                itemProjectRepository.findAllItemsInProject(projectId, ItemProjectStatus.ENABLED.name());

        return new ItemProjectResponseListDto(list);
    }

    @Override
    public ItemProjectResponseDto getItemProjectByProjectIdAndItemId(Long idProject, Long idItem) {

        ItemProjectEntity itemProjectEntity = itemProjectRepository.findByItemIdAndProjectId(idItem, idProject);

        return Objects.nonNull(itemProjectEntity) ? itemProjectMapper.toDto(itemProjectEntity) : null;
    }

}
