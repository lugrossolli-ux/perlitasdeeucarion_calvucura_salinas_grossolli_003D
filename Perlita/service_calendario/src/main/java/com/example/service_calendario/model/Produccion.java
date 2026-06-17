package com.example.service_calendario.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "produccion")
@Data
public class Produccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pedido_id")
    private Integer pedidoId; // puede ser null

    @Column(name = "producto_id", nullable = false)
    private Integer productoId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada", nullable = false)
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Column(nullable = false)
    private String estado = "programado";

    @Column(length = 4000)
    private String notas;

    // -------- Campos transitorios (no van a la BD) --------

    @Transient
    @JsonProperty("datosProducto")
    private Object datosProducto;

    @Transient
    @JsonProperty("datosPedido")
    private Object datosPedido;
}