package com.mx.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.core.model.ResponseGeneric;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<ResponseGeneric<Boolean>> checkHealth() {
    	log.debug("Servidor activo ", true);
        return ResponseEntity.ok(ResponseGeneric.buildSuccess("Servidor activo", true));
    }
    
}