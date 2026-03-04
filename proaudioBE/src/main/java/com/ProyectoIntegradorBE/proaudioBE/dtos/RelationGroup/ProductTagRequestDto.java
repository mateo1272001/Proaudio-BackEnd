package com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup;

import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductTagRequestDto {

    @NotNull
    private Long tagId;

    @NotNull
    private TagTypeEnum type;

    @Nullable
    private Long productId;

    @Nullable
    private Long relationGroupId;

    @Nullable
    private String name;
}
