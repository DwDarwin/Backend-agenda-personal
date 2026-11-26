package com.backend_agenda_personal.dto.response;

import com.backend_agenda_personal.enums.EstadoActividad;
import com.backend_agenda_personal.enums.PrioridadActividad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActividadResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String asignadoA;
    private LocalDate fechaEntrega;
    private EstadoActividad estado;
    private PrioridadActividad prioridad;
    private Long materiaId;
    private String materiaNombre;
    private LocalDateTime fechaCreacion;
}