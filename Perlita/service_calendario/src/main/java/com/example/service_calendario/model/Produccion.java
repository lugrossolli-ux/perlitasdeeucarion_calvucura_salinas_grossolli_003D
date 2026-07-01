package com.example.service_calendario.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "produccion")
@Data
@Schema(description = "Orden de producción vinculada a un producto y opcionalmente a un pedido")
public class Produccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID autogenerado", example = "1")
    private Integer id;

    @Column(name = "pedido_id")
    @Schema(description = "ID del pedido asociado (opcional)", example = "3")
    private Integer pedidoId;

    @Column(name = "producto_id", nullable = false)
    @Schema(description = "ID del producto a producir", example = "5")
    private Integer productoId;

    @Column(name = "fecha_inicio", nullable = false)
    @Schema(description = "Fecha de inicio de producción", example = "2026-07-01")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada", nullable = false)
    @Schema(description = "Fecha estimada de fin", example = "2026-07-10")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    @Schema(description = "Fecha real de fin (se asigna al finalizar)", example = "2026-07-09")
    private LocalDate fechaFinReal;

    @Column(nullable = false)
    @Schema(description = "Estado: programado | en_proceso | finalizado | cancelado", example = "programado")
    private String estado = "programado";

    @Column(length = 4000)
    @Schema(description = "Notas adicionales", example = "Producción urgente")
    private String notas;

    @Transient
    @JsonProperty("datosProducto")
    @Schema(description = "Datos del producto traídos del microservicio de productos (no se guarda en BD)")
    private Object datosProducto;

    @Transient
    @JsonProperty("datosPedido")
    @Schema(description = "Datos del pedido traídos del microservicio de pedidos (no se guarda en BD)")
    private Object datosPedido;
}