package com.backend_agenda_personal.service;

import com.backend_agenda_personal.dto.request.MateriaRequestDTO;
import com.backend_agenda_personal.dto.response.MateriaResponseDTO;

import java.util.List;

public interface MateriaService {


    MateriaResponseDTO crear(MateriaRequestDTO request);

    List<MateriaResponseDTO> listar();

    MateriaResponseDTO buscarPorId(Long id);

    MateriaResponseDTO actualizar(Long id, MateriaRequestDTO request);

    void eliminar(Long id);
}