package com.ProyectoIntegradorBE.proaudioBE.dtos.GeneralParmeters;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GeneralParameterRequestDto {

    @NotNull
    private String parameterKey;

    @NotNull
    private String value;

    private BasicEnumStatus status;

    @Nullable
    private Long generalParametersId;

}
