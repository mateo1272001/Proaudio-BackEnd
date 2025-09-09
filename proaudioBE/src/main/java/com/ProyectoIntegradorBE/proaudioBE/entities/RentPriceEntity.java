package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rent_price")
public class RentPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentPriceId;

    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal value;

    @NotNull
    private String description;


    @Enumerated(EnumType.STRING)
    private BasicEnumStatus status = BasicEnumStatus.ENABLED;

}
