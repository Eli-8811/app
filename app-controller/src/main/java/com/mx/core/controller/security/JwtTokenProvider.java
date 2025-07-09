package com.mx.core.controller.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.mx.core.repository.entity.UsuarioPrincipal;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {

        UsuarioPrincipal userPrincipal = (UsuarioPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Set<String> roles = new HashSet<>();

        for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
            String value = authority.getAuthority();
            if (value.startsWith("ROLE_")) {
                roles.add(value);
            }
        }

		return Jwts.builder().setSubject(Long.toString(userPrincipal.getId()))
				.claim("id", userPrincipal.getId())
				.claim("name", userPrincipal.getName())
				.claim("lastname", userPrincipal.getLastname())
				.claim("username", userPrincipal.getUsername())
				.claim("mobile", userPrincipal.getMobile())
				.claim("email", userPrincipal.getEmail())
				.claim("roles", roles)
				.claim("permissions", userPrincipal.getPermisos())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(key, SignatureAlgorithm.HS512).compact();
		
    }
    
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public String generateTokenFromUserId(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
}