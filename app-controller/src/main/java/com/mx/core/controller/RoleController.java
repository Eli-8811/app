package com.mx.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import com.mx.core.model.ResponseGeneric;
import com.mx.core.model.RoleDto;
import com.mx.core.service.RoleService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    public @ResponseBody ResponseEntity<ResponseGeneric<List<RoleDto>>> findAllRoleSummaries() {
        log.info("Fetching all roles without permissions");
        List<RoleDto> roles = roleService.findAllRoleSummaries();
        ResponseGeneric<List<RoleDto>> response = ResponseGeneric.buildSuccess("Lista de roles obtenida correctamente", roles);
        return ResponseEntity.ok(response);
    }
    
}