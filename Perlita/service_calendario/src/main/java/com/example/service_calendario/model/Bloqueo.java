package com.example.service_calendario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "bloqueos")
@Data
@Schema(description = "Bloqueo de fechas que impide crear producciones en ese rango")
public class Bloqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado", example = "1")
    private Integer id;

    @Column(name = "fecha_inicio", nullable = false)
    @Schema(description = "Inicio del bloqueo", example = "2026-12-24")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @Schema(description = "Fin del bloqueo", example = "2026-12-31")
    private LocalDate fechaFin;

    @Column(nullable = false, length = 300)
    @Schema(description = "Motivo del bloqueo", example = "Vacaciones de fin de año")
    private String motivo;
}