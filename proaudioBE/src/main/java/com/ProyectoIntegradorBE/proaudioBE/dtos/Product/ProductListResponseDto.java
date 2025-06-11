package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductListResponseDto {

    private List<ProductRowDto> products;

    private Pageable pageable;

}
