package com.elitelms.lms.security;

import com.elitelms.lms.model.Rol;
import com.elitelms.lms.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UsuarioDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private Collection<? extends GrantedAuthority> authorities;

    public UsuarioDetailsImpl(Long id, String email, String password, String fullName,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.authorities = authorities;
    }

    public static UsuarioDetailsImpl build(Usuario user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Rol::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new UsuarioDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getNombreCompleto(),
                authorities
        );
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return email; } // Spring usará email como username
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

