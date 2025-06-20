package com.mx.core.controller;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mx.core.model.RoleName;
import com.mx.core.controller.security.JwtTokenProvider;
import com.mx.core.model.ResponseGeneric;
import com.mx.core.model.payload.JwtAuthenticationResponse;
import com.mx.core.model.payload.RefreshTokenRequest;
import com.mx.core.model.payload.SignInRequest;
import com.mx.core.model.payload.SignUpRequest;
import com.mx.core.repository.entity.RefreshToken;
import com.mx.core.repository.entity.Role;
import com.mx.core.repository.entity.Usuario;
import com.mx.core.service.RefreshTokenService;
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

	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;    
	private final UsuarioService usuarioService;
    private final RoleService roleService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest loginRequest) {
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
        Usuario user = new Usuario(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));        
        Role userRole = roleService.findByName(RoleName.ROLE_USER);
        user.setRoles(Collections.singleton(userRole));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());       
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseGeneric.buildSuccess("User registered successfully", user));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {      
    	String refreshToken = request.getRefreshToken();        
        Optional<RefreshToken> storedTokenOpt = refreshTokenService.findByToken(refreshToken);        
        if (storedTokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseGeneric.buildError("Refresh token not found"));
        }
        try {        	
            RefreshToken storedToken = refreshTokenService.verifyExpiration(storedTokenOpt.get());
            Long userId = storedToken.getUser().getId();
            String newAccessToken = tokenProvider.generateTokenFromUserId(userId);
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(userId);
            return ResponseEntity.ok(new JwtAuthenticationResponse(newAccessToken, newRefreshToken.getToken()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseGeneric.buildError(ex.getMessage()));
        }      
    }
    
}