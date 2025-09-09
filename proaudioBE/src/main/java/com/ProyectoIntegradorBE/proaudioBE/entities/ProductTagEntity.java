package com.ProyectoIntegradorBE.proaudioBE.entities;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.enums.TagTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_tag")
public class ProductTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productTagId;

    @NotNull
    private Long tagId;

    @NotNull
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagTypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BasicEnumStatus status;

}
