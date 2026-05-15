package com.example.pedidos.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedido_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "cantidad_reservada", nullable = false)
    private BigDecimal cantidadReservada = BigDecimal.ZERO;
}