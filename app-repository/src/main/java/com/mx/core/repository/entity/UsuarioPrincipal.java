package com.mx.core.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UsuarioPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String lastname;
	private String username;
	private String mobile;
	
	@JsonIgnore
	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	private Set<String> permisos;

	public UsuarioPrincipal(
			Long id, 
			String name,
			String lastname,
			String username,
			String mobile,
			String email,
			String password,
			Collection<? extends GrantedAuthority> authorities, Set<String> permisos) {
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.username = username;
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.permisos = permisos;
	}

	public static UsuarioPrincipal create(Usuario user) {

		Set<GrantedAuthority> authorities = new HashSet<>();
		Set<String> permisos = new HashSet<>();

		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName().name()));
			role.getPermissions().forEach(permission -> {
				String permiso = permission.getName();
				authorities.add(new SimpleGrantedAuthority(permiso));
				permisos.add(permiso);
			});
		});

		return new UsuarioPrincipal(
				user.getId(),
				user.getName(),
				user.getLastname(),
				user.getUsername(),
				user.getMobile(),
				user.getEmail(),
				user.getPassword(),
				authorities,
				permisos
		);
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Set<String> getPermisos() {
		return permisos;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UsuarioPrincipal that))
			return false;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}