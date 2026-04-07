package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemResponseDto {

    private Long itemId;

    private Long productId;

    private LocationEnum location;

    private ItemStatusEnum status;

    private String description;

    private BigDecimal priceBought;

    private LocalDate boughtAt;

    private LocalDateTime updatedAt;

    private String QrImage;

    private String itemRange;

    private String serialNumber;

    private String assignedId;
}
