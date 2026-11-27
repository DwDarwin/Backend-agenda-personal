package com.backend_agenda_personal.service;


import com.backend_agenda_personal.dto.request.MateriaRequestDTO;
import com.backend_agenda_personal.dto.response.MateriaResponseDTO;
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
public class MateriaServiceImpl implements MateriaService {

    private final MateriaRepository materiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActividadRepository actividadRepository;

    @Override
    public MateriaResponseDTO crear(MateriaRequestDTO request) {

        Usuario usuario = obtenerUsuarioAutenticado();

        boolean existe = materiaRepository.existsByNombreIgnoreCaseAndUsuario_Id(
                request.getNombre().trim(),
                usuario.getId()
        );

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya tienes una materia con ese nombre"
            );
        }

        Materia materia = new Materia();
        materia.setNombre(request.getNombre().trim());
        materia.setDescripcion(request.getDescripcion());
        materia.setUsuario(usuario);

        Materia guardada = materiaRepository.save(materia);

        return convertirAResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MateriaResponseDTO> listar() {

        Usuario usuario = obtenerUsuarioAutenticado();

        return materiaRepository.findByUsuario_Id(usuario.getId())
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MateriaResponseDTO buscarPorId(Long id) {

        Materia materia = obtenerMateria(id);

        return convertirAResponse(materia);
    }

    @Override
    public MateriaResponseDTO actualizar(
            Long id,
            MateriaRequestDTO request
    ) {

        Materia materia = obtenerMateria(id);

        Usuario usuario = obtenerUsuarioAutenticado();

        boolean existe = materiaRepository.existsByNombreIgnoreCaseAndUsuario_IdAndIdNot(
                request.getNombre().trim(),
                usuario.getId(),
                id
        );

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya tienes otra materia con ese nombre"
            );
        }

        materia.setNombre(request.getNombre().trim());
        materia.setDescripcion(request.getDescripcion());

        return convertirAResponse(materiaRepository.save(materia));
    }

    @Override
    public void eliminar(Long id) {

        Materia materia = obtenerMateria(id);

        Usuario usuario = obtenerUsuarioAutenticado();

        boolean tieneActividades =
                actividadRepository.existsByMateria_IdAndUsuario_Id(
                        materia.getId(),
                        usuario.getId()
                );

        if (tieneActividades) {
            throw new IllegalArgumentException(
                    "No puedes eliminar esta materia porque tiene actividades asociadas."
            );
        }

        materiaRepository.delete(materia);
    }

    private Materia obtenerMateria(Long id) {

        Usuario usuario = obtenerUsuarioAutenticado();

        return materiaRepository.findByIdAndUsuario_Id(id, usuario.getId())
                .orElseThrow(() ->
                        new RuntimeException("Materia no encontrada"));
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

    private MateriaResponseDTO convertirAResponse(Materia materia) {

        MateriaResponseDTO response = new MateriaResponseDTO();

        response.setId(materia.getId());
        response.setNombre(materia.getNombre());
        response.setDescripcion(materia.getDescripcion());
        response.setFechaCreacion(materia.getFechaCreacion());

        return response;
    }



}
