package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;

import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupAndTagDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup.RelationGroupResponseDtoList;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.TagRelationGroupEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;

import java.util.List;

public interface RelationGroupService {

    RelationGroupDto createTagRelationGroup(RelationGroupAndTagDto relationGroupAndTagDto, TagEntity brandRoot);

    RelationGroupResponseDto deleteTagRelationGroup(Long relationGroupId, Long tagId);

    List<TagRelationGroupEntity> getTagRelationGroupByRelationGroupAndStatus(Long relationGroupId,
                                                                             BasicEnumStatus basicEnumStatus);

    RelationGroupResponseDto deleteRelationGroup(Long groupId);

    RelationGroupResponseDtoList getRelationGroupsByProduct(Long productId);
}
