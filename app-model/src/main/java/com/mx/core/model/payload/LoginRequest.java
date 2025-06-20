package com.mx.core.model.payload;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    
	@NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
    
}

