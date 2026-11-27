package com.backend_agenda_personal.repository;


import com.backend_agenda_personal.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findByUsuario_Id(Long usuarioId);

    Optional<Materia> findByIdAndUsuario_Id(Long id, Long usuarioId);

    boolean existsByNombreAndUsuario_Id(String nombre, Long usuarioId);

    boolean existsByNombreIgnoreCaseAndUsuario_Id(
            String nombre,
            Long usuarioId
    );

    boolean existsByNombreIgnoreCaseAndUsuario_IdAndIdNot(
            String nombre,
            Long usuarioId,
            Long id
    );


}