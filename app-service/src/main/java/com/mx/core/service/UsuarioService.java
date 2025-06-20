package com.mx.core.service;

import org.springframework.stereotype.Service;

import com.mx.core.repository.UsuarioRepository;
import com.mx.core.repository.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository userRepository;

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public Usuario findById(Long userId) {
	    return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
	}
    
}