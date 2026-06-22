package com.example.pedidos.model;
import io.swagger.v3.oas.annotations.media.Schema;import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedido_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Material solicitado en un pedido")
public class PedidoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @NotNull(message = "El ID del material es obligatorio")
    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "cantidad_reservada", nullable = false)
    private BigDecimal cantidadReservada = BigDecimal.ZERO;
}
