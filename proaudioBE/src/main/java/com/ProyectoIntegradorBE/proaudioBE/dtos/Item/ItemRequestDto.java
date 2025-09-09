package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemRequestDto {

    @NotNull
    private Long productId;

    private String description;

    private BigDecimal priceBought;

    private LocalDate boughtAt;

    private Integer amountBought;

    private String itemRange;

    private List<String> serialNumbers;

}
