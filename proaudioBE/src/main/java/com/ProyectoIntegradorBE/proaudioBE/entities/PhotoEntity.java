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
@Table(name = "photo")
public class PhotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private Long productId;

    private String url;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BasicEnumStatus status;

}
