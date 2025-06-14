package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class ProductTagResponseDto {

    @NotNull
    private Long tagId;

    @NotNull
    private TagTypeEnum type;

    @Nullable
    private Long productId;

    @Nullable
    private BasicEnumStatus status;

    @Nullable
    private String name;
}
