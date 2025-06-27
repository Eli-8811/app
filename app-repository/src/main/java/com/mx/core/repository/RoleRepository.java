package com.mx.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mx.core.model.RoleName;
import com.mx.core.repository.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
	@Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.name = :name")
	Optional<Role> findByNameWithPermissions(@Param("name") RoleName name);
    
}

