package com.example.galeria.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "galeria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Galeria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "url_imagen", nullable = false, length = 500)
    private String urlImagen;

    @Column(length = 300)
    private String descripcion;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal = false;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDate fechaSubida;

    @Column(nullable = false)
    private Boolean activo = true;
}