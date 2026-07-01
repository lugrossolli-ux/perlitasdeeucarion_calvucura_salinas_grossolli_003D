package com.example.service_calendario.repository;

import com.example.service_calendario.model.Bloqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BloqueoRepository extends JpaRepository<Bloqueo, Integer> {

    List<Bloqueo> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fin, LocalDate inicio);
}