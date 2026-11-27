package com.backend_agenda_personal.repository;

import com.backend_agenda_personal.enums.EstadoActividad;
import com.backend_agenda_personal.enums.PrioridadActividad;
import com.backend_agenda_personal.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByUsuario_Id(Long usuarioId);

    Optional<Actividad> findByIdAndUsuario_Id(Long id, Long usuarioId);

    List<Actividad> findByMateria_IdAndUsuario_Id(Long materiaId, Long usuarioId);

    boolean existsByMateria_IdAndUsuario_Id(
            Long materiaId,
            Long usuarioId
    );

    boolean existsByNombreIgnoreCaseAndUsuario_Id(
            String nombre,
            Long usuarioId
    );

    boolean existsByNombreIgnoreCaseAndUsuario_IdAndIdNot(
            String nombre,
            Long usuarioId,
            Long id
    );

    @Query("""
    SELECT a FROM Actividad a
    WHERE a.usuario.id = :usuarioId
    AND (
        COALESCE(:nombre, '') = ''
        OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
    )
    AND (:estado IS NULL OR a.estado = :estado)
    AND (:prioridad IS NULL OR a.prioridad = :prioridad)
    """)
    List<Actividad> buscarConFiltros(
            @Param("usuarioId") Long usuarioId,
            @Param("nombre") String nombre,
            @Param("estado") EstadoActividad estado,
            @Param("prioridad") PrioridadActividad prioridad
    );


}
