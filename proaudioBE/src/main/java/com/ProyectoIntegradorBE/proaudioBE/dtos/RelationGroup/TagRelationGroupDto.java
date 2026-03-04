package com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import lombok.Data;

@Data
public class TagRelationGroupDto {

    private Long tagRelationGroupId;

    private Long tagId;

    private Long relationGroupId;

    private BasicEnumStatus status;

}
