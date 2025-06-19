package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemResponseDto {

    private Long itemId;

    private String qrId;

    private Long productId;

    private LocationEnum location;

    private String status;

    private String description;

    private BigDecimal priceBought;

    private LocalDateTime boughtAt;

}
