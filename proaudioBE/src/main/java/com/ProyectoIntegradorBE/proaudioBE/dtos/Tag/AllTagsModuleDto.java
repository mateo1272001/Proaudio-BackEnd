package com.ProyectoIntegradorBE.proaudioBE.dtos.Tag;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AllTagsModuleDto {

    private Long tagId;

    private String name;

    private String description;

    private BasicEnumStatus status;

    private List<AllTagsModuleDto> childTags;

}
