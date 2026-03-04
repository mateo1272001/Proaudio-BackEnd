package com.ProyectoIntegradorBE.proaudioBE.dtos.RelationGroup;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.ProductResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationGroupAndTagDto {

    @NotNull
    private TagResponseDto tag;

    @NotNull
    private TagTypeEnum type;

    @NotNull
    private ProductResponseDto product;

    @Nullable
    private Long relationGroupId;

    @Nullable
    private String name;

}
