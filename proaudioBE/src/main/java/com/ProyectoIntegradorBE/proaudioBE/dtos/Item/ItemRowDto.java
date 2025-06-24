package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemRowDto {

    @NotNull
    private Long itemId;

    @NotNull
    private String location;

    private ItemStatusEnum status;

    private LocalDate boughtAt;

    @NotNull
    private String nextProjectName;

    @NotNull
    private LocalDate nextProject;

}
