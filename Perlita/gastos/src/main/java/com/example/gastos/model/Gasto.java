package com.example.gastos.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Gasto operativo registrado")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La categoría es obligatoria")
    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @NotBlank(message = "La descripción del gasto es obligatoria")
    @Column(nullable = false, length = 300)
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @Column(nullable = false)
    private Double monto;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "comprobante_ref", length = 100)
    private String comprobanteRef;

    @Column(name = "proveedor_id")
    private Long proveedorId;
}
