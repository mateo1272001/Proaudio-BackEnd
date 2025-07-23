package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Event.EventResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Expense.ExpenseResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectDetailsResponseDto {

    private Long projectId;

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private EventResponseDto event;

    //    private ClientResponseDto client; todo [CLIENT] add client to response

    private ProjectStatusEnum status;

    private PaymentStatusEnum paymentStatus;

    private ProjectTypeEnum projectType;

    private ProductsInProjectResponseDto products;

    private List<ExpenseResponseDto> expenses;

    //    private ItemsInProjectResponseDto items; todo [PROJECT] add items to response

}
