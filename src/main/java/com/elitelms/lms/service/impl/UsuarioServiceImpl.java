package com.elitelms.lms.service.impl;
import com.elitelms.lms.dto.SignupRequestDTO;
import com.elitelms.lms.model.Rol;
import com.elitelms.lms.model.Usuario;
import com.elitelms.lms.repository.RolRepository;
import com.elitelms.lms.repository.UsuarioRepository;
import com.elitelms.lms.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registerUser(SignupRequestDTO signUpRequest) {
        // Verificar email único
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email ya registrado");
        }

        // Construir entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(signUpRequest.getNombreCompleto());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setNumeroDeTelefono(signUpRequest.getNumeroDeTelefono());
        usuario.setDiaDeNacimiento(signUpRequest.getDiaDeNacimiento());
        usuario.setGenero(signUpRequest.getGenero());
        usuario.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));

        // Resolver roles: si no vienen, asignar ROLE_USER por defecto
        Set<Rol> roles = new HashSet<>();
        Set<String> requestedRoles = signUpRequest.getRoles();
        if (requestedRoles == null || requestedRoles.isEmpty()) {
            Rol userRole = rolRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role ROLE_USER no encontrado."));
            roles.add(userRole);
        } else {
            for (String r : requestedRoles) {
                String roleName = r.startsWith("ROLE_") ? r : "ROLE_" + r.toUpperCase();
                Rol role = rolRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " no encontrado."));
                roles.add(role);
            }
        }
        usuario.setRoles(roles);

        // Persistir usuario (dentro de la transacción)
        return usuarioRepository.save(usuario);
    }
}