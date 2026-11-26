package com.backend_agenda_personal.dto.response;

import com.backend_agenda_personal.enums.RolUsuario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private RolUsuario rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
