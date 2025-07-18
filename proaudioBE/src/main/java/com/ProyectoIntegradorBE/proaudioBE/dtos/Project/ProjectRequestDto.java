package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectRequestDto {

    @NotNull
    private String name;

    @Nullable
    private String description;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private EventRequestDto event;

    @Nullable
    private ClientRequestDto client;

    private ProjectStatusEnum status;

    private PaymentStatusEnum paymentStatus;

    private ProjectTypeEnum projectType;

    @Nullable
    private BigDecimal costAddition;

    @Nullable
    private List<ProjectProductRequestDto> products;

    @Nullable
    private List<ExpenseRequestDto> expenses;

}

