package com.example.pedidos.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.pedidos.model.PedidoMaterial;

@Repository
public interface PedidoMaterialRepository extends JpaRepository<PedidoMaterial, Long> {
    List<PedidoMaterial> findByPedidoId(Long pedidoId);
    List<PedidoMaterial> findByMaterialId(Long materialId);
}