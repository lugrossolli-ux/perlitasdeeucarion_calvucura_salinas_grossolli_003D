package com.example.service_calendario.repository;

import com.example.service_calendario.model.Produccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProduccionRepository extends JpaRepository<Produccion, Integer> {

    List<Produccion> findByEstado(String estado);

    List<Produccion> findByProductoId(Integer productoId);

    // Producciones que caen dentro de un rango de fechas (para el calendario)
    List<Produccion> findByFechaInicioBetween(LocalDate desde, LocalDate hasta);
}
