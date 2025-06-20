package com.mx.core.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario", schema = "sistema")
public class Usuario {

    @Id
    private Integer usuarioId;

    private String nombre;
    private String apellidoMaterno;
    private String apellidoPaterno;
    
}