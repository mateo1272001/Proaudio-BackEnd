package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.User.*;
import com.ProyectoIntegradorBE.proaudioBE.services.CustomUserDetailsService;
import com.ProyectoIntegradorBE.proaudioBE.services.JwtService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final CustomUserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("email/{email}")
    UserResponseDto getUserByEmail(String email) {
        return userService.findUserByEmail(email);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterRequestDto request) {

        return userService.register(request);

    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody AuthRequestDto request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword())); //authenticationManager instance from securityConfig

        UserResponseDto userResponseDto = userService.findUserByEmail(request.getEmail());
        String token = jwtService.generateToken(userResponseDto);

        return new LoginResponseDto(userResponseDto.getUserId(), userResponseDto.getEmail(), userResponseDto.getName(),
                token);

    }

    @PostMapping("/forgot/password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        return userService.forgotPassword(email);
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetRequest) {
        return userService.resetPassword(resetRequest);
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }

}
