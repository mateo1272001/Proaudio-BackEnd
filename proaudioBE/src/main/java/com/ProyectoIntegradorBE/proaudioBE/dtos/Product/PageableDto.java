package com.ProyectoIntegradorBE.proaudioBE.dtos.Product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PageableDto {

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    private long totalElements;

    private boolean hasNext;

    private boolean hasPrevious;

}
