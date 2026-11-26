package com.backend_agenda_personal.service;

import com.backend_agenda_personal.dto.request.ActividadRequestDTO;
import com.backend_agenda_personal.dto.response.ActividadResponseDTO;
import com.backend_agenda_personal.enums.EstadoActividad;
import com.backend_agenda_personal.enums.PrioridadActividad;

import java.util.List;

public interface ActividadService {

    ActividadResponseDTO crear(ActividadRequestDTO request);

    List<ActividadResponseDTO> listar(
            String nombre,
            EstadoActividad estado,
            PrioridadActividad prioridad
    );

    ActividadResponseDTO buscarPorId(Long id);

    List<ActividadResponseDTO> listarPorMateria(Long materiaId);

    ActividadResponseDTO actualizar(Long id, ActividadRequestDTO request);

    void eliminar(Long id);
}