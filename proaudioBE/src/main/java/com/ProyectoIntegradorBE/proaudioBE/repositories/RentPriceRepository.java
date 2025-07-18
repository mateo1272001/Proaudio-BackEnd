package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.RentPriceEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RentPriceRepository extends CrudRepository<RentPriceEntity, Long> {

    List<RentPriceEntity> findByProductIdAndStatus(Long id, BasicEnumStatus status);

    Optional<RentPriceEntity> findByRentPriceIdAndStatus(Long id, BasicEnumStatus basicEnumStatus);

}
