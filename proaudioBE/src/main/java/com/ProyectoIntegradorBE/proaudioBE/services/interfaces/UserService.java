package com.ProyectoIntegradorBE.proaudioBE.services.interfaces;


import com.ProyectoIntegradorBE.proaudioBE.dtos.User.AuthRegisterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.ResetPasswordDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.UserEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {

    UserResponseDto findUserByEmail(String email);

    Optional<UserEntity> findByEmail(String email, BasicEnumStatus status);

    ResponseEntity<?> register(AuthRegisterRequestDto request);

    ResponseEntity<?> forgotPassword(String email);

    ResponseEntity<?> resetPassword(ResetPasswordDto resetRequest);
}
