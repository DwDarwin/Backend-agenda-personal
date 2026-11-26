package com.backend_agenda_personal.controller;

import com.backend_agenda_personal.dto.request.LoginRequestDTO;
import com.backend_agenda_personal.dto.request.UsuarioRegistroRequestDTO;
import com.backend_agenda_personal.dto.response.AuthResponseDTO;
import com.backend_agenda_personal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> registrar(
            @Valid @RequestBody UsuarioRegistroRequestDTO request
    ) {

        AuthResponseDTO respuesta = authService.registrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> iniciarSesion(
            @Valid @RequestBody LoginRequestDTO request
    ) {

        AuthResponseDTO respuesta = authService.iniciarSesion(request);

        return ResponseEntity.ok(respuesta);
    }
}