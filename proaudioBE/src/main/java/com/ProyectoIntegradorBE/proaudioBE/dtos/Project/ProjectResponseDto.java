package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;


import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ClientResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Item.ItemResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectResponseDto {

    private Long projectId;

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private EventResponseDto event;

    private ClientResponseDto client;

    private ProjectStatusEnum status;

    private PaymentStatusEnum paymentStatus;

    private ProjectTypeEnum projectType;

    private BigDecimal costAddition;

    private List<ProductProjectResponseForProjectDto> products;

    private List<ExpenseResponseDto> expenses;

    private List<ItemResponseDto> items;

}
