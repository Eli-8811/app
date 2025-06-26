package com.mx.core.model.payload;

import lombok.Data;

@Data
public class UsuarioUpdateRequest {
	
    private String name;
    private String username;
    private String email;
    
}