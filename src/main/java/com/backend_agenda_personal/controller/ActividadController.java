package com.backend_agenda_personal.controller;

import com.backend_agenda_personal.dto.request.ActividadRequestDTO;
import com.backend_agenda_personal.dto.response.ActividadResponseDTO;
import com.backend_agenda_personal.enums.EstadoActividad;
import com.backend_agenda_personal.enums.PrioridadActividad;
import com.backend_agenda_personal.service.ActividadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final ActividadService actividadService;

    @PostMapping
    public ResponseEntity<ActividadResponseDTO> crear(
            @Valid @RequestBody ActividadRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ActividadResponseDTO>> listar(

            @RequestParam(required = false) String nombre,

            @RequestParam(required = false) EstadoActividad estado,

            @RequestParam(required = false) PrioridadActividad prioridad

    ) {
        return ResponseEntity.ok(
                actividadService.listar(nombre, estado, prioridad)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(actividadService.buscarPorId(id));
    }

    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<ActividadResponseDTO>> listarPorMateria(
            @PathVariable Long materiaId
    ) {
        return ResponseEntity.ok(
                actividadService.listarPorMateria(materiaId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActividadRequestDTO request
    ) {
        return ResponseEntity.ok(
                actividadService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        actividadService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}