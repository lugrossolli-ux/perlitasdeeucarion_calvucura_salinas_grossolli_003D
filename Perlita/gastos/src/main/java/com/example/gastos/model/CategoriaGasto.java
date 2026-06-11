package com.example.gastos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias_gasto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 300)
    private String descripcion;
}
