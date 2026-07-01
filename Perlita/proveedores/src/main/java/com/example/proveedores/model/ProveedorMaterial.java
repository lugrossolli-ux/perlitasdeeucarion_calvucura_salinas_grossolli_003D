package com.example.proveedores.model;
import io.swagger.v3.oas.annotations.media.Schema;import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "proveedor_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Material ofrecido por un proveedor con su precio")
public class ProveedorMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del proveedor es obligatorio")
    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @NotNull(message = "El ID del material es obligatorio")
    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @NotNull(message = "El precio del proveedor es obligatorio")
    @Column(name = "precio_proveedor", nullable = false)
    private BigDecimal precioProveedor = BigDecimal.ZERO;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;
}
