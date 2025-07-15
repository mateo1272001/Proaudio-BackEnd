package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ProjectEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.ProjectStatusEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByStatusIn(List<ProjectStatusEnum> planned);
}
