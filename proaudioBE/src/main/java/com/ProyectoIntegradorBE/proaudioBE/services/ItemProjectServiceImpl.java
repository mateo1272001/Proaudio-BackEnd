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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemProjectServiceImpl implements ItemProjectService {

    private final ProductProjectService productProjectService;

    private final ItemProjectRepository itemProjectRepository;

    private final ItemProjectMapper itemProjectMapper;

    @Override
    public ItemProjectResponseDto createItemProject(ItemResponseDto itemResponseDto,
                                                    ProjectSimpleReponseDto projectSimpleReponseDto) {

        ItemProjectEntity itemProjectEntity = new ItemProjectEntity();

        Optional<ItemProjectEntity> itemProjectOpt =
                itemProjectRepository.findByItemIdAndProjectId(itemResponseDto.getItemId(),
                        projectSimpleReponseDto.getProjectId());

        if (itemProjectOpt.isPresent()) {
            itemProjectEntity = itemProjectOpt.get();

            if (itemProjectEntity.getStatus().equals(ItemProjectStatus.ENABLED)) {
                throw new BadRequestException("¡El artículo ya está en el proyecto!");
            }
        }

        ProductProjectWithModelResponseDto productProjectWithModelResponseDto =
                productProjectService.getByProductAndProjectId(itemResponseDto.getProductId(),
                        projectSimpleReponseDto.getProjectId());

        List<ItemProjectEntity> itemProjectEntityList =
                itemProjectRepository.findItemsOfProductInProject(itemResponseDto.getProductId(),
                        projectSimpleReponseDto.getProjectId());

        if (itemProjectEntityList.size() >= productProjectWithModelResponseDto.getAmount()) {
            throw new BadRequestException("¡Ya se alcanzó la cantidad de artículos necesarios!");
        }

        itemProjectEntity.setItemId(itemResponseDto.getItemId());
        itemProjectEntity.setProjectId(projectSimpleReponseDto.getProjectId());
        itemProjectEntity.setStatus(ItemProjectStatus.ENABLED);
        itemProjectEntity.setCreatedAt(LocalDateTime.now());
        itemProjectEntity = itemProjectRepository.save(itemProjectEntity);

        ItemProjectResponseDto itemProjectResponseDto = itemProjectMapper.toDto(itemProjectEntity);
        itemProjectResponseDto.setProductModel(productProjectWithModelResponseDto.getModel());
        return itemProjectResponseDto;
    }

    @Override
    public ItemProjectResponseDto changeStatusItemProject(ItemResponseDto itemResponseDto,
                                                          ProjectSimpleReponseDto projectSimpleReponseDto,
                                                          ItemProjectStatus itemProjectStatus) {

        ItemProjectEntity itemProjectEntity =
                itemProjectRepository.findByItemIdAndProjectIdAndStatusIn(itemResponseDto.getItemId(),
                                projectSimpleReponseDto.getProjectId(),
                                List.of(ItemProjectStatus.ENABLED, ItemProjectStatus.RETURNED))
                        .orElseThrow(() -> new BadRequestException("¡Este artículo no se encuentra en este proyecto!"));

        itemProjectEntity.setStatus(itemProjectStatus);

        itemProjectEntity = itemProjectRepository.save(itemProjectEntity);

        return itemProjectMapper.toDto(itemProjectEntity);
    }

    @Override
    public ItemProjectResponseListDto getItemsInProject(Long projectId) {

        List<ItemProjectResponseIntDto> list = itemProjectRepository.findAllItemsInProject(projectId);

        return new ItemProjectResponseListDto(list);
    }

}
