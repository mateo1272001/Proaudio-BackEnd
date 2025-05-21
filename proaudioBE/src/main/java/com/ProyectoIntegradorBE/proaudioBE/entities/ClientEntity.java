package com.ProyectoIntegradorBE.proaudioBE.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    private Long id;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

}
