package com.mx.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mx.core.common.exception.ResourceNotFoundException;
import com.mx.core.repository.UsuarioRepository;
import com.mx.core.repository.entity.Usuario;
import com.mx.core.repository.entity.UsuarioPrincipal;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		Usuario user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));
		return UsuarioPrincipal.create(user);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		Usuario user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
		return UsuarioPrincipal.create(user);
	}

}