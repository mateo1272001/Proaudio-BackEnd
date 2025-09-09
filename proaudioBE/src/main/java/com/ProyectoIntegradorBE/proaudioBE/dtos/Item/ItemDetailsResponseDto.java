package com.ProyectoIntegradorBE.proaudioBE.dtos.Item;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemDetailsResponseDto {

    private String description;

    private ItemStatusEnum status;

    private LocationEnum location;

    private BigDecimal priceBought;

    private LocalDate boughtAt;

    private ItemProductResponseDto product;

    private String range;

    private String serialNumber;


    //    private Object activities;

}
