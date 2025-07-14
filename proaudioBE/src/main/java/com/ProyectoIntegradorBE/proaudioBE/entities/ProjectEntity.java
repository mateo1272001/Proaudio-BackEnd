package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.PaymentStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String name;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long eventId;

    private Long clientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatusEnum paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectTypeEnum projectType;

    private BigDecimal costAddition;

}
