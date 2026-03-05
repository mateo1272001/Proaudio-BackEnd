package com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import lombok.Data;

@Data
public class RelationGroupDto {

    private Long relationGroupId;

    private Long productId;

    private TagTypeEnum type;

    private BasicEnumStatus status;

    private Boolean isNew = false;

    private String name;

}
