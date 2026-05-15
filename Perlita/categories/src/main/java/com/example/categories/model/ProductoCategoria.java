package com.example.categories.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCategoria{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
}
