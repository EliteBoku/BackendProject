package com.elitelms.lms.service;
import com.elitelms.lms.dto.SignupRequestDTO;
import com.elitelms.lms.model.Usuario;

public interface UsuarioService {
    /**
     * Crea y persiste un usuario a partir del DTO de signup.
     * Lanza RuntimeException (o una excepción específica) si hay errores (email duplicado, role no encontrado, etc).
     */
    Usuario registerUser(SignupRequestDTO signUpRequest);
}