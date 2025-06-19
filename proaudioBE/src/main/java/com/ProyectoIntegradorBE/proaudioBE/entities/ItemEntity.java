package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private Long qrId;

    private Long productId;

    private LocationEnum location;

    private String description;

    private BigDecimal priceBought;

    private LocalDateTime boughtAt;

}
