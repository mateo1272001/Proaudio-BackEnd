package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectRunningStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProjectRowResponseDto {

    private Long projectId;

    private String name;

    private ProjectStatusEnum status;

    private PaymentStatusEnum paymentStatus;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private ProjectRunningStatusEnum runningStatus;

}
