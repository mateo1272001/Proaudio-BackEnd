package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemStatusResponseDto {

    List<ItemStatusEnum> statusList;

}
