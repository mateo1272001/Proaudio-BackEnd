package com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GeneralParameterResponseDto {

    private Long generalParametersId;

    private String parameterKey;

    private String value;

    private BasicEnumStatus status;

}
