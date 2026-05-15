package com.example.productos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "cantidad_requerida", nullable = false)
    private Double cantidadRequerida;

    @Transient
    private Object datosMaterial;
}