package com.mx.core.controller;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.core.model.ResponseGeneric;
import com.mx.core.model.RoleName;
import com.mx.core.model.payload.RoleRequest;
import com.mx.core.model.payload.SignUpRequest;
import com.mx.core.model.payload.UsuarioUpdateRequest;
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
@RequestMapping("/usuario")
@RestController
public class UsuarioController {
	
	private final RoleService roleService;
	private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/registrar")
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

        Role userRoleDefault = roleService.findByNameWithPermissions(RoleName.ROLE_USER);
        
        if (userRoleDefault == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseGeneric.buildError("No se encontró el rol de usuario por defecto."));
        }
        
    	Set<Role> userRoles = new HashSet<>();

        if (signUpRequest.getRoles() == null || signUpRequest.getRoles().isEmpty()) {
        	
            userRoles = Set.of(userRoleDefault);
            
        } else {
        	
        	for (RoleRequest r : signUpRequest.getRoles()) {
        	    try {
        	    	Role foundRole = roleService.findByIdWithPermissions(r.getId());
        	        if (foundRole == null) {
        	            throw new IllegalArgumentException("Rol no válido: " + r.getName());
        	        }
        	        userRoles.add(foundRole);
        	    } catch (IllegalArgumentException e) {
        	        throw new RuntimeException("Rol no reconocido: " + r.getName(), e);
        	    }
        	}
            
        }

        Usuario user = Usuario.builder()
                .name(signUpRequest.getName())
                .lastname(signUpRequest.getLastname())
                .username(signUpRequest.getUsername())
                .mobile(signUpRequest.getMobile())
                .email(signUpRequest.getEmail())
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
    
	@GetMapping("/consultar/{id}")
	public ResponseEntity<ResponseGeneric<?>> getUserById(@PathVariable Long id) {
		
	    try {
	    	
	    	Usuario safeUser = new Usuario();
	    	
	        Usuario user = usuarioService.findByIdWithRoles(id);
	        
	        safeUser.setId(user.getId());
	        safeUser.setName(user.getName());
	        safeUser.setUsername(user.getUsername());
	        safeUser.setEmail(user.getEmail());
	        safeUser.setRoles(user.getRoles());
	        safeUser.setCreatedAt(user.getCreatedAt());
	        safeUser.setUpdatedAt(user.getUpdatedAt());

	        return ResponseEntity.ok(ResponseGeneric.buildSuccess("Usuario encontrado", safeUser));
	    
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(ResponseGeneric.buildError("Usuario no encontrado con ID: " + id));
	    }
	    
	}
	
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<ResponseGeneric<?>> updateUser(
			@PathVariable Long id,
			@RequestBody UsuarioUpdateRequest updateRequest) {
		
	    try {
	    	
	        Usuario existingUser = usuarioService.findByIdWithRoles(id);
	        existingUser.setName(updateRequest.getName());
	        existingUser.setUsername(updateRequest.getUsername());
	        existingUser.setEmail(updateRequest.getEmail());
	        existingUser.setUpdatedAt(Instant.now());
	        
	        if (usuarioService.existsByEmail(updateRequest.getEmail())
	        	    && !existingUser.getEmail().equals(updateRequest.getEmail())) {
	        	    return ResponseEntity
	        	        .status(HttpStatus.BAD_REQUEST)
	        	        .body(ResponseGeneric.buildError("El email ya está en uso por otro usuario"));
	        }
	        
	        usuarioService.save(existingUser);
	        Usuario safeUser = new Usuario();
	        safeUser.setId(existingUser.getId());
	        safeUser.setName(existingUser.getName());
	        safeUser.setUsername(existingUser.getUsername());
	        safeUser.setEmail(existingUser.getEmail());
	        safeUser.setGender(existingUser.getGender());
	        safeUser.setActive(existingUser.getActive());
	        safeUser.setCreatedAt(existingUser.getCreatedAt());
	        safeUser.setUpdatedAt(existingUser.getUpdatedAt());

	        return ResponseEntity.ok(ResponseGeneric.buildSuccess("Usuario actualizado correctamente", safeUser));
	        
	    } catch (RuntimeException e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(ResponseGeneric.buildError("Usuario no encontrado con ID: " + id));
	    }
	    
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseGeneric<?>> deleteUser(@PathVariable Long id) {
	    try {
	        usuarioService.deleteById(id);
	        return ResponseEntity.ok(ResponseGeneric.buildSuccess("Usuario eliminado exitosamente", null));
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(ResponseGeneric.buildError("Usuario no encontrado con ID: " + id));
	    }
	}
	
    @GetMapping("/listar")
    public ResponseEntity<ResponseGeneric<List<Usuario>>> getAllUsers() {
        List<Usuario> usuarios = usuarioService.getAllUsersWithRolesAndPermissions();
        return ResponseEntity.ok(ResponseGeneric.buildSuccess("Usuarios encontrados", usuarios));
    }
    
}
