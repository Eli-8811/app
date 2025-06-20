package com.mx.core.controller;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.mx.core.model.ResponseGeneric;
import com.mx.core.repository.entity.Usuario;
import com.mx.core.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.AllArgsConstructor;

import static com.mx.core.common.message.Message.USUARIO_ENCONTRADO;
import static com.mx.core.common.message.Message.USUARIO_NO_ENCONTRADO;

@Validated
@AllArgsConstructor
@RequestMapping("/usuario")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(
        summary = "Buscar usuarios por nombre",
        description = "Devuelve una lista de usuarios que coinciden con el nombre proporcionado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
        @ApiResponse(responseCode = "400", description = "Parámetro inválido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<ResponseGeneric<List<Usuario>>> findByNombre(
        @RequestParam
        @NotBlank(message = "El nombre no puede estar vacío")
        @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$",
            message = "El nombre solo debe contener letras")
        String nombre) {
        
        List<Usuario> usuarios = usuarioService.findByNombre(nombre);

        ResponseGeneric<List<Usuario>> response = usuarios.isEmpty()
            ? ResponseGeneric.buildSuccess(false, USUARIO_NO_ENCONTRADO, List.of())
            : ResponseGeneric.buildSuccess(true, USUARIO_ENCONTRADO, usuarios);

        return ResponseEntity.ok(response);
        
    }
    
}