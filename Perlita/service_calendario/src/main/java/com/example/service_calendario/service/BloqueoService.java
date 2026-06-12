package com.example.service_calendario.service;

import com.example.service_calendario.exception.ResourceNotFoundException;
import com.example.service_calendario.model.Bloqueo;
import com.example.service_calendario.repository.BloqueoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloqueoService {

    private final BloqueoRepository bloqueoRepository;

    public BloqueoService(BloqueoRepository bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    public List<Bloqueo> listarTodos() {
        return bloqueoRepository.findAll();
    }

    public Bloqueo crear(Bloqueo bloqueo) {
        if (bloqueo.getFechaFin().isBefore(bloqueo.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha de inicio");
        }
        return bloqueoRepository.save(bloqueo);
    }

    public void eliminar(Integer id) {
        if (!bloqueoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bloqueo no encontrado con id: " + id);
        }
        bloqueoRepository.deleteById(id);
    }
}
