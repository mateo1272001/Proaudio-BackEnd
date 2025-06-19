package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemRequestDto {

    @NotNull
    private Long productId;

    private String description;

    private BigDecimal priceBought;

    private LocalDateTime boughtAt;

    private Integer amountBought;

}
