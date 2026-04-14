package com.ProyectoIntegradorBE.proaudioBE.dtos.ItemProject;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface ItemProjectResponseIntDto {

    Long getItemProjectId();

    Long getItemId();

    Long getProjectId();

    ItemProjectStatus getStatus();

    LocalDate getCreatedAt();

    LocalDate getItemBoughtAt();

    BigDecimal getItemPriceBought();

    String getItemDescription();

    ItemStatusEnum getItemStatus();

    String getItemSerialNumber();

    String getItemRange();

    LocationEnum getItemLocation();

    Long getProductId();

    String getProductModel();

    String getAssignedId();

}
