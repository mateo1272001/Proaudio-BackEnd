package com.ProyectoIntegradorBE.proaudioBE.dtos.Tag;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AllTagsResponseDto {

    private List<AllTagsModuleDto> parentTags;

}
