package com.mx.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mx.core.repository.entity.RefreshToken;
import com.mx.core.repository.entity.Usuario;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
    Optional<RefreshToken> findByToken(String token);
    
    void deleteByUser(Usuario user);
    
}