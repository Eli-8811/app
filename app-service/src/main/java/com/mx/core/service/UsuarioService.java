package com.mx.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.core.repository.UsuarioRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    
}