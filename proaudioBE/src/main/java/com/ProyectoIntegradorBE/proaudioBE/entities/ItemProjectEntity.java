package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemProjectStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "item_project")
public class ItemProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemProjectId;

    private Long itemId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemProjectStatus status;

    private LocalDateTime createdAt;

}
