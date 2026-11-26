package com.backend_agenda_personal.controller;

import com.backend_agenda_personal.dto.request.MateriaRequestDTO;
import com.backend_agenda_personal.dto.response.MateriaResponseDTO;
import com.backend_agenda_personal.service.MateriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @PostMapping
    public ResponseEntity<MateriaResponseDTO> crear(
            @Valid @RequestBody MateriaRequestDTO request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(materiaService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<MateriaResponseDTO>> listar() {

        return ResponseEntity.ok(materiaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(materiaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MateriaRequestDTO request
    ) {
        return ResponseEntity.ok(
                materiaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        materiaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
