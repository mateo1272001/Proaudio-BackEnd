package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.User.AuthRegisterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.UserEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.UserMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.UserRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public UserResponseDto findUserByEmail(String email) {
        Optional<UserEntity> userEntityOptional = findByEmail(email, BasicEnumStatus.ENABLED);

        if (userEntityOptional.isEmpty()) {
            throw new BadRequestException("No se encontró usuario con ese email");
        }

        return userMapper.toDto(userEntityOptional.get());
    }

    @Override
    public Optional<UserEntity> findByEmail(String email, BasicEnumStatus status) {

        return userRepository.findByEmailAndStatus(email, status);

    }

    @Override
    public ResponseEntity<?> register(AuthRegisterRequestDto request) {

        if (findByEmail(request.getEmail(), BasicEnumStatus.ENABLED).isPresent()) {
            throw new BadRequestException("¡El usuario ya existe!");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setName(request.getName());
        userEntity.setPhone_number(request.getPhoneNumber());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setStatus(BasicEnumStatus.ENABLED);

        userRepository.save(userEntity);

        return ResponseEntity.ok(Collections.singletonMap("message", "Usuario registrado exitosamente"));

    }

}
