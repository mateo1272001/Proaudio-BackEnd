package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "general_parameters")
public class GeneralParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long generalParametersId;

    private String parameterKey;

    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BasicEnumStatus status;

}
