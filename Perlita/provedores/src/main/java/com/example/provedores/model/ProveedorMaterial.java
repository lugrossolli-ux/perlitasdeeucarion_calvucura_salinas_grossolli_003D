package com.example.provedores.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "proveedor_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "precio_proveedor", nullable = false)
    private BigDecimal precioProveedor = BigDecimal.ZERO;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;
}