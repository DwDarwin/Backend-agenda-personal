package com.backend_agenda_personal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MateriaResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;


}