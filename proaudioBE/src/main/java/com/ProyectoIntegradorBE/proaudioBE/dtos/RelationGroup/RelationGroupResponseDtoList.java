package com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RelationGroupResponseDtoList {

    private RelationGroupResponseDto descriptive;

    private List<RelationGroupResponseDto> relation;

    private List<RelationGroupResponseDto> dependency;

}
