package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRowDto {

    private String brand;

    private String model;

    private int items;

    private int available;

    private String comments;

}
