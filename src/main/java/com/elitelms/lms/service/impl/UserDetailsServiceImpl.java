package com.elitelms.lms.service.impl;

import com.elitelms.lms.model.Usuario;
import com.elitelms.lms.repository.UsuarioRepository;
import com.elitelms.lms.security.UsuarioDetailsImpl;
import com.elitelms.lms.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return UsuarioDetailsImpl.build(user);
    }
}
