package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectSimpleReponseDto {

    private Long projectId;

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long eventId;

    private Long clientId;

    private ProjectStatusEnum status;

    private PaymentStatusEnum paymentStatus;

    private ProjectTypeEnum projectType;

    private BigDecimal costAddition;

    @Nullable
    private EventResponseDto event;

    @Nullable
    private ClientResponseDto client;


}
