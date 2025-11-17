package com.elitelms.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(name = "full_name", nullable = false)
    private String nombreCompleto;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 30)
    @Column(name = "phone_number")
    private String numeroDeTelefono;

    /**
     * Almacena el hash de la contraseña (ej: BCrypt). No guardar texto plano.
     */
    @NotBlank
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate diaDeNacimiento;

    @Column(length = 20)
    private String Genero;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.OffsetDateTime createdAt = java.time.OffsetDateTime.now();

    @Column(name = "updated_at")
    private java.time.OffsetDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Rol> roles = new HashSet<>();
}

