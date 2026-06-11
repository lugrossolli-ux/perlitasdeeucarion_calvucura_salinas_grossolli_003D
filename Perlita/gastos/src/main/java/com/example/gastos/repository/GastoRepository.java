package com.example.gastos.repository;

import com.example.gastos.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByCategoriaId(Long categoriaId);
    List<Gasto> findByProveedorId(Long proveedorId);
}