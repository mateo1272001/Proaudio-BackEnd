package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PhotoRequestListDto {

    @NotNull
    private List<PhotoRequestDto> photos;

    @NotNull()
    private Long productId;

}
