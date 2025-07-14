package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "product_project")
public class ProductProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productProjectId;

    @NotNull
    private Long productId;

    @NotNull
    private Long projectId;

    @NotNull
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BasicEnumStatus status;

}
