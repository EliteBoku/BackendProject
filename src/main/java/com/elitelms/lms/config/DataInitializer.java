package com.elitelms.lms.config;

import com.elitelms.lms.model.Rol;
import com.elitelms.lms.model.Usuario;
import com.elitelms.lms.repository.RolRepository;
import com.elitelms.lms.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RolRepository rolRepository,
                                   UsuarioRepository usuarioRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            Rol roleUser = rolRepository.findByName("ROLE_USER")
                    .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_USER")));

            Rol roleAdmin = rolRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> rolRepository.save(new Rol(null, "ROLE_ADMIN")));

            // Crear usuario admin si no existe por email
            String adminEmail = "admin@gmail.com";
            if (!usuarioRepository.existsByEmail(adminEmail)) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Elite Boku");
                admin.setEmail(adminEmail);
                admin.setNumeroDeTelefono("3214334765");
                admin.setDiaDeNacimiento(LocalDate.of(1990,1,1));
                admin.setGenero("Masculino"); // o "MALE"/"FEMALE"

                // Encriptar contraseña (usamos el PasswordEncoder provisto por Spring)
                String rawPassword = "Admin123!"; // Cambia esto inmediatamente en prod
                admin.setPasswordHash(passwordEncoder.encode(rawPassword));

                Set<Rol> roles = new HashSet<>();
                roles.add(roleUser);
                roles.add(roleAdmin);
                admin.setRoles(roles);

                usuarioRepository.save(admin);

                System.out.println("Usuario admin creado: " + adminEmail + " / password: " + rawPassword);
            } else {
                System.out.println("Usuario admin ya existe: " + adminEmail);
            }
        };
    }
}
