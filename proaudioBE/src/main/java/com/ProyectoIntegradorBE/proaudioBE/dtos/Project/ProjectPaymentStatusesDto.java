package com.ProyectoIntegradorBE.proaudioBE.dtos.Project;

import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ProjectPaymentStatusesDto {

    private List<PaymentStatusEnum> paymentStatuses;

}
