package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.PhotoEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends CrudRepository<PhotoEntity, Long> {


    List<PhotoEntity> findByProductId(Long productId);

    Optional<PhotoEntity> findByIdAndStatus(Long id, BasicEnumStatus basicEnumStatus);
}
