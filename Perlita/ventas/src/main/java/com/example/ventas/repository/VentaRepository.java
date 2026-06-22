package com.example.ventas.repository;

import com.example.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteId(Long clienteId);

    @Query("SELECT v.metodoPago, COUNT(v), SUM(v.total) FROM Venta v GROUP BY v.metodoPago")
    List<Object[]> resumenPorMetodoPago();
}