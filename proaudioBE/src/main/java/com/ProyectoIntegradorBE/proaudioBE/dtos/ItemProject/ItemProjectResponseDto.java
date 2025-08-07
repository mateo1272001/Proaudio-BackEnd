package com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemProjectResponseDto {

    private Long itemProjectId;

    private Long itemId;

    private Long projectId;

    private ItemProjectStatus status;

    private LocalDateTime createdAt;

    private LocalDate itemBoughtAt;

    private BigDecimal itemPriceBought;

    private String itemDescription;

    private ItemStatusEnum itemStatus;

    private String itemSerialNumber;

    private String itemRange;

    private Long productId;

    private String productModel;

}
