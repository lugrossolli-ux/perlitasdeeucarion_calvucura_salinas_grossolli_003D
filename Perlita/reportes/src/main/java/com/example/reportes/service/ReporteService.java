package com.example.reportes.service;

import com.example.reportes.model.Reporte;
import com.example.reportes.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> listarTodos() {
        return reporteRepository.findAll();
    }

    public Optional<Reporte> buscarPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public Reporte guardar(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminar(Long id) {
        reporteRepository.deleteById(id);
    }
}