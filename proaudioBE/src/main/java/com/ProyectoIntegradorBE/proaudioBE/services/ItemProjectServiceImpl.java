package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject.ItemProjectResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.ProductProject.ProductProjectResponseDto;
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

@Service
@RequiredArgsConstructor
public class ItemProjectServiceImpl implements ItemProjectService {

    private final ProductProjectService productProjectService;

    private final ItemProjectRepository itemProjectRepository;

    private final ItemProjectMapper itemProjectMapper;

    @Override
    public ItemProjectResponseDto createItemProject(ItemResponseDto itemResponseDto,
                                                    ProjectSimpleReponseDto projectSimpleReponseDto) {

        ProductProjectResponseDto productProjectResponseDto =
                productProjectService.getByProductAndProjectId(itemResponseDto.getProductId(),
                        projectSimpleReponseDto.getProjectId());

        List<ItemProjectEntity> itemProjectEntityList =
                itemProjectRepository.findItemsOfProductInProject(itemResponseDto.getProductId(),
                        projectSimpleReponseDto.getProjectId());

        if (itemProjectEntityList.size() >= productProjectResponseDto.getAmount()) {
            throw new BadRequestException("¡Ya se alcanzó la cantidad de artículos necesarios!");
        }

        ItemProjectEntity itemProjectEntity = new ItemProjectEntity();
        itemProjectEntity.setItemId(itemResponseDto.getItemId());
        itemProjectEntity.setProjectId(projectSimpleReponseDto.getProjectId());
        itemProjectEntity.setStatus(ItemProjectStatus.ENABLED);
        itemProjectEntity.setCreatedAt(LocalDateTime.now());
        itemProjectEntity = itemProjectRepository.save(itemProjectEntity);

        return itemProjectMapper.toDto(itemProjectEntity);
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

}
