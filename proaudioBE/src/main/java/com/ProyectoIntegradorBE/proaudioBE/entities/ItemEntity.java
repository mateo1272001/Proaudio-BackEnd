package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.ItemStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.enums.LocationEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationEnum location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatusEnum status;

    private String description;

    private BigDecimal priceBought;

    private LocalDate boughtAt;

    private LocalDateTime updatedAt;

    //    private String serialNumber; //todo [ITEM] add serial number usage to code

}
