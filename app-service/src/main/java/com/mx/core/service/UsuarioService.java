package com.mx.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.core.repository.UsuarioRepository;
import com.mx.core.repository.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

	public boolean existsByUsername(String username) {
		return usuarioRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	public Usuario findByIdWithRoles(Long userId) {
	    return usuarioRepository.findByIdWithRoles(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
	}

	public void save(Usuario user) {
		usuarioRepository.save(user);
	}

	public void deleteById(Long id) {
	    Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
	    usuarioRepository.delete(usuario);
	}
	
    public List<Usuario> getAllUsersWithRolesAndPermissions() {
        return usuarioRepository.findAllWithRolesAndPermissions();
    }
    
}