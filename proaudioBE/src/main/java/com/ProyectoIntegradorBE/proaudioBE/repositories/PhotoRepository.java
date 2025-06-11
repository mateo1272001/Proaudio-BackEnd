package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.PhotoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends CrudRepository<PhotoEntity, Long> {


    List<PhotoEntity> findByProductId(Long productId);
}
