package com.mx.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.core.repository.UsuarioRepository;
import com.mx.core.repository.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> findByNombre(String nombre) {
        log.info("Buscando usuarios por nombre: {}", nombre);
        return usuarioRepository.findByNombre(nombre);
    }
	
}