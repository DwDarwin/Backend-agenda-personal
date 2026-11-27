package com.backend_agenda_personal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarArgumentoInvalido(
            IllegalArgumentException exception) {

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesIncorrectas(
            BadCredentialsException exception
    ) {
        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(
                "mensaje",
                "Correo o contraseña incorrectos. Verifica tus datos e inténtalo nuevamente."
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(respuesta);
    }


}