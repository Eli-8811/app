package com.mx.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mx.core.model.RoleDto;
import com.mx.core.model.RoleName;
import com.mx.core.repository.RoleRepository;
import com.mx.core.repository.entity.Role;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class RoleService {

	private final RoleRepository roleRepository;

	public Role findById(Long id) {
		return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontró el rol con ID: " + id));
	}
	
	public Role findByIdWithPermissions(Long id) {
	    return roleRepository.findByIdWithPermissions(id).orElseThrow(() -> new RuntimeException("No se encontró el rol con ID: " + id));
	}
	
	public Role findByNameWithPermissions(RoleName roleName) {
	    return roleRepository.findByNameWithPermissions(roleName).orElseThrow(() -> new RuntimeException("No se encontró el rol con Nombre: " + roleName));
	}
	
	public List<RoleDto> findAllRoleSummaries() {
	    List<RoleDto> roles = roleRepository.findAllRoleSummaries();
	    return roles;
	}
	
}
