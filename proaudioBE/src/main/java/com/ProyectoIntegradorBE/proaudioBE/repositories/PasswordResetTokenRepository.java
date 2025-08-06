package com.ProyectoIntegradorBE.proaudioBE.repositories;


import com.ProyectoIntegradorBE.proaudioBE.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

}
