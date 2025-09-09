package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.ActionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends CrudRepository<ActionEntity, Long> {

    Optional<ActionEntity> findByActionId(Long id);

    Optional<ActionEntity> findByActionKey(String key);

}
