package com.ProyectoIntegradorBE.proaudioBE.dtos.Client;

import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClientDetailsResponseDto {

    private Long clientId;

    private String name;

    private String phoneNumber;

    private String email;

    private String address;

    private String details;

    private BasicEnumStatus status;

    private List<ProjectParticipatedResponseDto> projectsParticipated;

    //    private List<Activities> activities //todo [ACTIVITIES] add when activities are implemented

}
