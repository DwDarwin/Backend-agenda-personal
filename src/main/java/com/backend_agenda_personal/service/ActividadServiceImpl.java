package com.backend_agenda_personal.service;

import com.backend_agenda_personal.dto.request.ActividadRequestDTO;
import com.backend_agenda_personal.dto.response.ActividadResponseDTO;
import com.backend_agenda_personal.enums.EstadoActividad;
import com.backend_agenda_personal.enums.PrioridadActividad;
import com.backend_agenda_personal.model.Actividad;
import com.backend_agenda_personal.model.Materia;
import com.backend_agenda_personal.model.Usuario;
import com.backend_agenda_personal.repository.ActividadRepository;
import com.backend_agenda_personal.repository.MateriaRepository;
import com.backend_agenda_personal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActividadServiceImpl implements ActividadService {

    private final ActividadRepository actividadRepository;
    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public ActividadResponseDTO crear(ActividadRequestDTO request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        Materia materia = obtenerMateria(
                request.getMateriaId(),
                usuario.getId()
        );

        Actividad actividad = new Actividad();

        actividad.setNombre(request.getNombre().trim());
        actividad.setDescripcion(request.getDescripcion());
        actividad.setAsignadoA(request.getAsignadoA());
        actividad.setFechaEntrega(request.getFechaEntrega());

        actividad.setEstado(
                request.getEstado() != null
                        ? request.getEstado()
                        : EstadoActividad.PENDIENTE
        );

        actividad.setPrioridad(
                request.getPrioridad() != null
                        ? request.getPrioridad()
                        : PrioridadActividad.MEDIA
        );

        actividad.setUsuario(usuario);
        actividad.setMateria(materia);

        Actividad guardada = actividadRepository.save(actividad);

        return convertirAResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listar(
            String nombre,
            EstadoActividad estado,
            PrioridadActividad prioridad
    ) {

        Usuario usuario = obtenerUsuarioAutenticado();

        // Evitamos enviar cadenas vacías a la consulta.
        if (nombre != null) {
            nombre = nombre.trim();

            if (nombre.isEmpty()) {
                nombre = null;
            }
        }

        return actividadRepository.buscarConFiltros(
                        usuario.getId(),
                        nombre,
                        estado,
                        prioridad
                )
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ActividadResponseDTO buscarPorId(Long id) {

        // Buscar la actividad y verificar que pertenezca al usuario.
        Actividad actividad = obtenerActividad(id);

        // Convertir la entidad en DTO.
        return convertirAResponse(actividad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> listarPorMateria(Long materiaId) {

        Usuario usuario = obtenerUsuarioAutenticado();

        // Verificamos que la materia pertenezca al usuario.
        obtenerMateria(materiaId, usuario.getId());

        return actividadRepository
                .findByMateria_IdAndUsuario_Id(materiaId, usuario.getId())
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public ActividadResponseDTO actualizar(
            Long id,
            ActividadRequestDTO request
    ) {

        Actividad actividad = obtenerActividad(id);

        Usuario usuario = obtenerUsuarioAutenticado();

        // Comprobamos que la nueva materia pertenezca al usuario.
        Materia materia = obtenerMateria(
                request.getMateriaId(),
                usuario.getId()
        );

        actividad.setNombre(request.getNombre().trim());
        actividad.setDescripcion(request.getDescripcion());
        actividad.setAsignadoA(request.getAsignadoA());
        actividad.setFechaEntrega(request.getFechaEntrega());

        // Una actividad completada no puede cambiar nuevamente de estado.
        if (actividad.getEstado() == EstadoActividad.COMPLETADA
                && request.getEstado() != null
                && request.getEstado() != EstadoActividad.COMPLETADA) {

            throw new IllegalStateException(
                    "Una actividad completada no puede cambiar de estado."
            );
        }

        // Actualizar el estado cuando se haya enviado.
        if (request.getEstado() != null) {
            actividad.setEstado(request.getEstado());
        }

        if (request.getPrioridad() != null) {
            actividad.setPrioridad(request.getPrioridad());
        }

        actividad.setMateria(materia);

        return convertirAResponse(actividadRepository.save(actividad));
    }

    @Override
    public void eliminar(Long id) {

        Actividad actividad = obtenerActividad(id);

        actividadRepository.delete(actividad);
    }

    private Actividad obtenerActividad(Long id) {

        Usuario usuario = obtenerUsuarioAutenticado();

        return actividadRepository
                .findByIdAndUsuario_Id(id, usuario.getId())
                .orElseThrow(() ->
                        new RuntimeException("Actividad no encontrada"));
    }

    private Materia obtenerMateria(Long materiaId, Long usuarioId) {

        return materiaRepository
                .findByIdAndUsuario_Id(materiaId, usuarioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "La materia no existe o no pertenece a tu cuenta"
                        ));
    }

    private Usuario obtenerUsuarioAutenticado() {

        Authentication autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        if (autenticacion == null ||
                !autenticacion.isAuthenticated() ||
                autenticacion instanceof AnonymousAuthenticationToken) {

            throw new IllegalStateException("No hay un usuario autenticado");
        }

        String correo = autenticacion.getName();

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    private ActividadResponseDTO convertirAResponse(Actividad actividad) {

        ActividadResponseDTO response = new ActividadResponseDTO();

        response.setId(actividad.getId());
        response.setNombre(actividad.getNombre());
        response.setDescripcion(actividad.getDescripcion());
        response.setAsignadoA(actividad.getAsignadoA());
        response.setFechaEntrega(actividad.getFechaEntrega());
        response.setEstado(actividad.getEstado());
        response.setPrioridad(actividad.getPrioridad());
        response.setMateriaId(actividad.getMateria().getId());
        response.setMateriaNombre(actividad.getMateria().getNombre());
        response.setFechaCreacion(actividad.getFechaCreacion());

        return response;
    }
}
