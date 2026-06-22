package com.example.materiales.service;

import com.example.materiales.model.Material;
import com.example.materiales.repository.MaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository repository;

    @InjectMocks
    private MaterialService service;

    @Test
    void listarTodos_shouldReturnAllMaterials() {
        List<Material> expected = List.of(new Material());
        when(repository.findAll()).thenReturn(expected);

        List<Material> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_whenExists_shouldReturnMaterial() {
        Material material = new Material();
        material.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(material));

        Optional<Material> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Material> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
        verify(repository).findById(99L);
    }

    @Test
    void guardar_shouldSetFechaActualizacionAndSave() {
        Material input = new Material();
        input.setNombre("Test");
        Material saved = new Material();
        saved.setId(1L);
        saved.setNombre("Test");
        saved.setFechaActualizacion(LocalDate.now());
        when(repository.save(any(Material.class))).thenReturn(saved);

        Material actual = service.guardar(input);

        assertNotNull(actual.getFechaActualizacion());
        assertEquals(LocalDate.now(), actual.getFechaActualizacion());
        assertEquals(1L, actual.getId());
        verify(repository).save(any(Material.class));
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void stockBajo_shouldReturnMaterialsBelowThreshold() {
        Material m = new Material();
        m.setStockActual(-5.0);
        List<Material> expected = List.of(m);
        when(repository.findByStockActualLessThan(0.0)).thenReturn(expected);

        List<Material> actual = service.stockBajo();

        assertEquals(expected, actual);
        verify(repository).findByStockActualLessThan(0.0);
    }
}
