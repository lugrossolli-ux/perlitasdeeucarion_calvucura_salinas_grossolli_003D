package com.example.productos.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "producto_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Relación entre un producto y sus materiales requeridos")
public class ProductoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @NotNull(message = "El ID del material es obligatorio")
    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @NotNull(message = "La cantidad requerida es obligatoria")
    @Column(name = "cantidad_requerida", nullable = false)
    private Double cantidadRequerida;

    @Transient
    private Object datosMaterial;
}
