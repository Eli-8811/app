package com.mx.core.controller.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI sistemaGestionOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Sistema de Gestión API")
                .version("v1.0.0")
                .description("Documentación de la API para el Sistema de Gestión")
                .contact(new Contact()
                    .name("Equipo de Desarrollo")
                    .email("soporte@sistemagestion.com")
                    .url("https://sistemagestion.com")));
    }
    
}