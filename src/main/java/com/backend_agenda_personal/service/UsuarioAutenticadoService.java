package com.backend_agenda_personal.service;

import com.backend_agenda_personal.model.Usuario;
import com.backend_agenda_personal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuarioActual() {

        Authentication autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        if (autenticacion == null
                || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {

            throw new IllegalStateException(
                    "No se pudo identificar al usuario. Debes iniciar sesión."
            );
        }

        String correo = autenticacion.getName();

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No se encontró el usuario autenticado"
                ));
    }
}
