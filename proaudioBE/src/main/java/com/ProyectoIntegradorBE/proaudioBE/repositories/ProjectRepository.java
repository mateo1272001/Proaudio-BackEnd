package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Client.ProjectParticipatedResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import com.ProyectoIntegradorBE.proaudioBE.repositories.customrepositories.ProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long>, JpaRepository<ProjectEntity, Long>,
        JpaSpecificationExecutor<ProjectEntity>, ProjectRepositoryCustom {

    List<ProjectEntity> findByStatusIn(List<ProjectStatusEnum> planned);

    List<ProjectParticipatedResponseDto> findByClientId(Long id);

}
