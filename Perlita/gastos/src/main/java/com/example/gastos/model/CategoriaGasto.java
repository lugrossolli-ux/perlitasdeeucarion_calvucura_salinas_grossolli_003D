package com.example.gastos.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "categorias_gasto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Categoría para clasificar gastos")
public class CategoriaGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categorÃ­a es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 300)
    private String descripcion;
}
