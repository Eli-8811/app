package com.mx.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import com.mx.core.model.RoleName;
import com.mx.core.model.payload.RoleRequest;
import com.mx.core.model.payload.SignUpRequest;
import com.mx.core.repository.UserRepository;
import com.mx.core.repository.entity.Role;
import com.mx.core.repository.entity.Usuario;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Usuario findByIdWithRoles(Long userId) {
	    return userRepository.findByIdWithRoles(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
	}

	public void save(Usuario user) {
		userRepository.save(user);
	}

	public void deleteById(Long id) {
	    Usuario usuario = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
	    userRepository.delete(usuario);
	}
	
    public List<Usuario> getAllUsersWithRolesAndPermissions() {
        return userRepository.findAllWithRolesAndPermissions();
    }

	public Set<Role> setRolesByUser(@Valid SignUpRequest signUpRequest) {		
		Set<Role> userRoles = new HashSet<>();		
        Role userRoleDefault = roleService.findByNameWithPermissions(RoleName.ROLE_USER);       
        if (userRoleDefault == null) {
            throw new IllegalArgumentException("No se encontró el rol de usuario por defecto.");
        }
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
		return userRoles;
	}
    
}