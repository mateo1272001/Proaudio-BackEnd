package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PhotoResponseDto {

//    private String url;

    private String name;

    private BasicEnumStatus status;

    private byte[] data;

}
