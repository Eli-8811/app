package com.mx.core.model.payload;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequest {
    
	private Long id;
	
	@Size(max = 50, message = "El nombre del rol no debe exceder 50 caracteres")
	private String name;
    
}
