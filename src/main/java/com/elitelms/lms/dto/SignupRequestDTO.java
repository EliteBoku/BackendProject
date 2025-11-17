package com.elitelms.lms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class SignupRequestDTO    {
    @NotBlank
    @Size(max = 150)
    private String nombreCompleto;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 30)
    private String numeroDeTelefono;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;

    @Past
    private LocalDate diaDeNacimiento; // enviar en ISO: "yyyy-MM-dd"

    private String genero; // o usar un enum: "MALE","FEMALE","OTHER"

    private Set<String> roles; // opcional: ["user","admin"]
}
