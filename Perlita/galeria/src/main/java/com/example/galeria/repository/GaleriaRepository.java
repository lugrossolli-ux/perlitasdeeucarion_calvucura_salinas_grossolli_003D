package com.example.galeria.repository;

import com.example.galeria.model.Galeria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GaleriaRepository extends JpaRepository<Galeria, Long> {

    List<Galeria> findByProductoId(Long productoId);
}