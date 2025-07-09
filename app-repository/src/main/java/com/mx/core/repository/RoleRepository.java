package com.mx.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mx.core.model.RoleDto;
import com.mx.core.model.RoleName;
import com.mx.core.repository.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
	@Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :id")
	Optional<Role> findByIdWithPermissions(@Param("id") Long id);
	
	@Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.name = :name")
	Optional<Role> findByNameWithPermissions(@Param("name") RoleName name);
 	
	@Query("SELECT new com.mx.core.model.RoleDto(r.id, r.name) FROM Role r ORDER BY r.name ASC")
	List<RoleDto> findAllRoleSummaries();
	
}

