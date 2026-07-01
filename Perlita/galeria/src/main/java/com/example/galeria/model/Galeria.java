package com.example.galeria.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "galeria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Imagen de producto en la galería")
public class Galeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del producto es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Column(name = "url_imagen", nullable = false, length = 500)
    private String urlImagen;

    @Column(length = 300)
    private String descripcion;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @NotNull(message = "La fecha de subida es obligatoria")
    @Column(name = "fecha_subida", nullable = false)
    private LocalDate fechaSubida;

    @Column(nullable = false)
    private Boolean activo = true;
}