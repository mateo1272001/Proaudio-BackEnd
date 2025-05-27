package com.ProyectoIntegradorBE.proaudioBE.dtos.Tag;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TagResponseDto {

    private Long tagId;

    private String name;

    private Long fatherId;

    private String description;

    private BasicEnumStatus status;

}
