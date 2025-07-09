package com.mx.core.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mx.core.common.exception.AppBadCredentialsException;
import com.mx.core.controller.security.JwtTokenProvider;
import com.mx.core.model.ResponseGeneric;
import com.mx.core.model.payload.JwtAuthenticationResponse;
import com.mx.core.model.payload.SignInRequest;
import com.mx.core.model.payload.SignUpRequest;
import com.mx.core.repository.entity.Role;
import com.mx.core.repository.entity.Usuario;
import com.mx.core.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

	private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
	private final UserService usuarioService;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
                )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            log.debug(jwt);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Bearer"));
        } catch (BadCredentialsException ex) {
            throw new AppBadCredentialsException("401", "Autenticación fallida", "Usuario o contraseña incorrectos");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseGeneric<?>> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (usuarioService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ResponseGeneric.buildError("El correo electrónico ya está registrado."));
        }       
        if (usuarioService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ResponseGeneric.buildError("El username ya está en uso."));
        }
        Set<Role> userRoles = usuarioService.setRolesByUser(signUpRequest);
        Usuario user = Usuario.builder()
                .name(signUpRequest.getName())
                .lastname(signUpRequest.getLastname())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .mobile(signUpRequest.getMobile())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .gender(signUpRequest.getGender())
                .born(signUpRequest.getBorn())
                .roles(userRoles)
                .build();
        usuarioService.save(user);
        log.info("Nuevo usuario registrado: {}", user.getUsername());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseGeneric.buildSuccess("Usuario registrado exitosamente.", user));
       
    }
    
}