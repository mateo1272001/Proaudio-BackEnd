package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.User.AuthRegisterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.ChangePasswordDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.ResetPasswordDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.PasswordResetTokenEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.UserEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.BasicEnumStatus;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.UserMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.PasswordResetTokenRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.UserRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

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

        validatePassword(request.getPassword());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setName(request.getName());
        userEntity.setPhone_number(request.getPhoneNumber());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setStatus(BasicEnumStatus.ENABLED);

        userRepository.save(userEntity);

        return ResponseEntity.ok(Collections.singletonMap("message", "Usuario registrado exitosamente"));

    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            String token = UUID.randomUUID().toString();
            PasswordResetTokenEntity prt = new PasswordResetTokenEntity();
            prt.setToken(token);
            prt.setUserId(user.getUserId());
            prt.setExpiryDate(LocalDateTime.now().plusMinutes(30));
            passwordResetTokenRepository.save(prt);

            emailService.sendPasswordResetEmail(user.getEmail(), token);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Si el email existe, recibirás un link."));
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetRequest) {

        Optional<PasswordResetTokenEntity> tokenOpt = passwordResetTokenRepository.findByToken(resetRequest.getToken());

        if (tokenOpt.isEmpty() || tokenOpt.get().isUsed() ||
                tokenOpt.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Este token no es válido");
        }

        if (!resetRequest.getNewPassword().equals(resetRequest.getNewPasswordRepeat())) {
            throw new BadRequestException("¡Las contraseñas son distintas!");
        }

        validatePassword(resetRequest.getNewPassword());

        UserEntity userEntity =
                userRepository.findByUserIdAndStatus(tokenOpt.get().getUserId(), BasicEnumStatus.ENABLED)
                        .orElseThrow(() -> new InternalException("El ususerio asociado al token no se encuentra"));
        userEntity.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
        userRepository.save(userEntity);

        PasswordResetTokenEntity prt = tokenOpt.get();
        prt.setUsed(true);
        passwordResetTokenRepository.save(prt);

        return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña actualizada exitosamente"));

    }

    @Override
    public ResponseEntity<?> changePassword(ChangePasswordDto changePasswordDto) {

        String loggedUserEmail = AuthUtilsSerivce.getLoggedUserEmail();

        UserEntity userEntity =
                userRepository.findByEmail(loggedUserEmail).orElseThrow(() -> new InternalException(""));
        String currentPass = userEntity.getPassword();

        if (passwordEncoder.matches(changePasswordDto.getNewPassword(), currentPass)) {
            throw new BadRequestException("¡La contraseña actual es incorrecta!");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getNewPasswordRepeat())) {
            throw new BadRequestException("¡Las contraseñas son distintas!");
        }

        userEntity.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(userEntity);

        return ResponseEntity.ok(Collections.singletonMap("message", "Contraseña actualizada exitosamente"));
    }

    private void validatePassword(String newPassword) {

        if (StringUtils.isBlank(newPassword)) {
            throw new BadRequestException("La contraseña está vacía");
        }

        if (newPassword.trim().length() <= 8) {
            throw new BadRequestException("La contraseña debe tener más de 8 caracteres");
        }

        if (newPassword.chars().noneMatch(Character::isDigit)) {
            throw new BadRequestException("La contraseña debe contener al menos un número");
        }

        if (newPassword.chars().noneMatch(Character::isUpperCase)) {
            throw new BadRequestException("La contraseña debe contener al menos una mayúscula");
        }

        if (newPassword.chars().noneMatch(Character::isLowerCase)) {
            throw new BadRequestException("La contraseña debe contener al menos una minúscula");
        }

    }

}
