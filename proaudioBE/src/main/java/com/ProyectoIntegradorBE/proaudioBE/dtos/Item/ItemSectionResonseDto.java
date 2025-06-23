package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ItemSectionResonseDto {

    private List<ItemRowDto> items;

    private PageableDto pageable;

}
