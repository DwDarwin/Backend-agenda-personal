package com.backend_agenda_personal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tipo;
    private Long id;
    private String nombre;
    private String correo;
    private String rol;
}