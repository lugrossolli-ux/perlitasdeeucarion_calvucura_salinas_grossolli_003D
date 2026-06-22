package com.example.materiales.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "materiales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Material utilizado en la producción de productos")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del material es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 4000)
    private String descripcion;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Column(name = "unidad_medida", nullable = false, length = 50)
    private String unidadMedida;

    @NotNull(message = "El stock actual es obligatorio")
    @Column(name = "stock_actual", nullable = false)
    private Double stockActual = 0.0;

    @NotNull(message = "El stock mÃ­nimo es obligatorio")
    @Column(name = "stock_minimo", nullable = false)
    private Double stockMinimo = 0.0;

    @NotNull(message = "El precio unitario es obligatorio")
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;
}
