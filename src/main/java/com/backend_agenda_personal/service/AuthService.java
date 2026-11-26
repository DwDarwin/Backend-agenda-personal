package com.backend_agenda_personal.service;

import com.backend_agenda_personal.dto.request.LoginRequestDTO;
import com.backend_agenda_personal.dto.request.UsuarioRegistroRequestDTO;
import com.backend_agenda_personal.dto.response.AuthResponseDTO;
import com.backend_agenda_personal.enums.RolUsuario;
import com.backend_agenda_personal.model.Usuario;
import com.backend_agenda_personal.repository.UsuarioRepository;
import com.backend_agenda_personal.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthResponseDTO registrar(UsuarioRegistroRequestDTO request) {

        String correo = request.getCorreo().trim().toLowerCase();


        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException(
                    "El correo electrónico ya está registrado"
            );
        }


        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre().trim());
        usuario.setCorreo(correo);

        // Cifrar la contraseña antes de guardarla
        usuario.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );


        usuario.setRol(RolUsuario.USER);
        usuario.setActivo(true);


        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Generar JWT
        String token = jwtService.generarToken(usuarioGuardado);

        return construirRespuesta(usuarioGuardado, token);
    }


    public AuthResponseDTO iniciarSesion(LoginRequestDTO request) {

        String correo = request.getCorreo().trim().toLowerCase();

        // Spring Security verifica las credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        correo,
                        request.getPassword()
                )
        );

        // Recuperar el usuario autenticado
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        // Generar JWT
        String token = jwtService.generarToken(usuario);

        return construirRespuesta(usuario, token);
    }

    // Construir respuesta de autenticación
    private AuthResponseDTO construirRespuesta(
            Usuario usuario,
            String token
    ) {

        return new AuthResponseDTO(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().name()
        );
    }
}