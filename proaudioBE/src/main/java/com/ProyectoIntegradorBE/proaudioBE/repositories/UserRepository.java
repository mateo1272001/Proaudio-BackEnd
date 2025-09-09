package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.UserEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndStatus(String email, BasicEnumStatus status);

    Optional<UserEntity> findByUserIdAndStatus(Long userId, BasicEnumStatus basicEnumStatus);

    List<String> findEmailByStatus(BasicEnumStatus status);

    List<UserEntity> findByStatus(BasicEnumStatus basicEnumStatus);
}
