package com.backend_agenda_personal.dto.request;

import com.backend_agenda_personal.enums.EstadoActividad;
import com.backend_agenda_personal.enums.PrioridadActividad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ActividadRequestDTO {

    @NotBlank(message = "El nombre de la actividad es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    private String descripcion;

    @Size(max = 100, message = "El campo asignado a no puede superar los 100 caracteres")
    private String asignadoA;

    @NotNull(message = "La fecha de entrega es obligatoria")
    private LocalDate fechaEntrega;

    private EstadoActividad estado;

    private PrioridadActividad prioridad;

    @NotNull(message = "Debes seleccionar una materia")
    private Long materiaId;
}