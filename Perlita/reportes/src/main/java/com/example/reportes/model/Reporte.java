package com.example.reportes.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Reporte generado por el sistema")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Column(nullable = false, length = 100)
    private String tipo;

    @NotNull(message = "La fecha de generación es obligatoria")
    @Column(name = "fecha_generacion", nullable = false)
    private LocalDate fechaGeneracion;

    @Column(columnDefinition = "TEXT")
    private String parametros;

    @NotBlank(message = "El resultado en formato JSON es obligatorio")
    @Column(name = "resultado_json", nullable = false, columnDefinition = "TEXT")
    private String resultadoJson;
}