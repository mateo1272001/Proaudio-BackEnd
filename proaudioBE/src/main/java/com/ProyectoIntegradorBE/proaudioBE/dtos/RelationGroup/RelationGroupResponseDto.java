package com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RelationGroupResponseDto {

    @Nullable
    private Long relationGroupId;

    @Nullable
    private String name;

    @Nullable
    private Long productId;

    @NotNull
    private TagTypeEnum type;

    @NotNull
    private BasicEnumStatus status;

    @NotNull
    private List<TagResponseDto> tags;

}
