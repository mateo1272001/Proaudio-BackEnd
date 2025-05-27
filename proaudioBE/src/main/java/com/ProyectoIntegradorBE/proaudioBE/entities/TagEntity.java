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
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String name;

    private Long fatherId;

    private String description;

    @Enumerated(EnumType.STRING) //so it doesnt save enums as their ordinal value
    @Column(nullable = false)
    private BasicEnumStatus status;
}
