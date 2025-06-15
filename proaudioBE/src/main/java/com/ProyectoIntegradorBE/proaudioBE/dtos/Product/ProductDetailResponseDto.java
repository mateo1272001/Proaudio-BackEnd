package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Tag.TagResponseDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDetailResponseDto {

    private String brand;

    private String model;

    private String comments;

    private List<PhotoResponseDto> photos;

    private List<PriceReponseDto> prices;

    private BigDecimal replacementValue;

//    private List<ActivitiesResponseDto> activities;

//    private ProductBalanceReponseDto productBalance;

    private List<TagResponseDto> descriptionTags;

    private List<TagResponseDto> relationTags;

    private List<TagResponseDto> dependencyTags;

}
