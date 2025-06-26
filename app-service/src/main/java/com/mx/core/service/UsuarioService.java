package com.mx.core.service;

import org.springframework.stereotype.Service;

import com.mx.core.repository.UsuarioRepository;
import com.mx.core.repository.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository userRepository;

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
	
}