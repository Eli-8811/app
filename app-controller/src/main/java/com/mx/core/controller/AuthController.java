package com.mx.core.controller;

import java.time.Instant;
import java.util.Collections;

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
import com.mx.core.model.RoleName;
import com.mx.core.model.payload.JwtAuthenticationResponse;
import com.mx.core.model.payload.SignInRequest;
import com.mx.core.model.payload.SignUpRequest;
import com.mx.core.repository.entity.Role;
import com.mx.core.repository.entity.Usuario;
import com.mx.core.service.RoleService;
import com.mx.core.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

	private final RoleService roleService;
	private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

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
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        } catch (BadCredentialsException ex) {
            throw new AppBadCredentialsException("401", "Autenticación fallida", "Usuario o contraseña incorrectos");
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<ResponseGeneric<?>> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        
    	if (usuarioService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseGeneric.buildError("Username is already taken!"));
        }
        
        if (usuarioService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseGeneric.buildError("Email Address already in use!"));
        }
        
        Role userRole = null;
        
        if (signUpRequest.getRoles() == null || signUpRequest.getRoles().isEmpty()) {
        	 userRole = roleService.findByName(RoleName.ROLE_USER);
        }
        
        Usuario user = Usuario.builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(Collections.singleton(userRole))
                .build();

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        
        usuarioService.save(user);
        log.info("Nuevo usuario registrado: {}", user.getUsername());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseGeneric.buildSuccess("User registered successfully", user));
    }

}