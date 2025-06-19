package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemRowDto {

    @NotNull
    private Long id;

    @NotNull
    private String location;

    private LocalDate boughtAt;

    @NotNull
    private String nextProjectName;

    @NotNull
    private LocalDate nextProject;

}
