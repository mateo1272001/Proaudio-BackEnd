package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;

import java.time.LocalDateTime;

public interface ProjectParticipatedResponseDto {

    Long getProjectId();

    String getName();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    ProjectStatusEnum getStatus();

    PaymentStatusEnum getPaymentStatus();

}
