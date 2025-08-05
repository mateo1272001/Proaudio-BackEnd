package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.User.AuthRegisterRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.AuthRequestDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.services.CustomUserDetailsService;
import com.ProyectoIntegradorBE.proaudioBE.services.JwtService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

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
    public ResponseEntity<?> login(@RequestBody AuthRequestDto request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword())); //authenticationManager instance from securityConfig

        UserDetails user = (UserDetails) auth.getPrincipal(); // already loaded

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(Collections.singletonMap("token", token));

    }


}
