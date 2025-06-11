package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.RentPriceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RentPriceRepository extends CrudRepository<RentPriceEntity, Long> {

    public List<RentPriceEntity> findByProductId(Long id);
}
