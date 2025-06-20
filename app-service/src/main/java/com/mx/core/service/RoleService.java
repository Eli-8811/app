package com.mx.core.service;

import org.springframework.stereotype.Service;

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

	public Role findByName(RoleName roleName) {
	    return roleRepository.findByName(roleName)
	            .orElseThrow(() -> new RuntimeException("User Role not set."));
	}
	
}
