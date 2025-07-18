package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.GeneralParameterEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralParameterRepository extends CrudRepository<GeneralParameterEntity, Long> {

    Optional<GeneralParameterEntity> findByGeneralParametersIdAndStatus(Long id, BasicEnumStatus status);

    List<GeneralParameterEntity> findByParameterKeyAndStatus(String key, BasicEnumStatus basicEnumStatus);
}
