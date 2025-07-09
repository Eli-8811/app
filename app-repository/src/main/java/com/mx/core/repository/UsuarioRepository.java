package com.mx.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mx.core.repository.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsernameOrEmail(String username, String email);

    List<Usuario> findByIdIn(List<Long> userIds);

    Optional<Usuario> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.id = :id")
    Optional<Usuario> findByIdWithRoles(@Param("id") Long id);
    
    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles r JOIN FETCH r.permissions ORDER BY u.name ASC")
    List<Usuario> findAllWithRolesAndPermissions();
    
}
