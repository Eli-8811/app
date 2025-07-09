package com.mx.core.model.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank
    @Size(min = 1, max = 50)
    private String username;
    
    @NotBlank
    @Size(min = 1, max = 200)
    private String lastname;

    @NotBlank
    @Size(max = 50)
    private String mobile;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 20)
    private String password;

    @NotNull
    private LocalDate born;
    
    @NotBlank
    @Pattern(regexp = "M|F|X", message = "Género inválido")
    private String gender;
    
    private Set<RoleRequest> roles;
    
}