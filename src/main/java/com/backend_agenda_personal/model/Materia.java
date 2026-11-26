package com.backend_agenda_personal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "materias",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_materia_usuario",
                        columnNames = {"usuario_id", "nombre"}
                ),
                @UniqueConstraint(
                        name = "uk_materia_id_usuario",
                        columnNames = {"id", "usuario_id"}
                )
        }
)
@Getter
@Setter
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}