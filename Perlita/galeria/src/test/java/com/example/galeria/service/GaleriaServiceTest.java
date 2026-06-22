package com.example.galeria.service;

import com.example.galeria.model.Galeria;
import com.example.galeria.repository.GaleriaRepository;
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
class GaleriaServiceTest {

    @Mock
    private GaleriaRepository galeriaRepository;

    @InjectMocks
    private GaleriaService service;

    @Test
    void listarTodos_shouldReturnAll() {
        List<Galeria> expected = List.of(new Galeria());
        when(galeriaRepository.findAll()).thenReturn(expected);

        List<Galeria> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(galeriaRepository).findAll();
    }

    @Test
    void buscarPorId_whenExists_shouldReturn() {
        Galeria g = new Galeria();
        g.setId(1L);
        when(galeriaRepository.findById(1L)).thenReturn(Optional.of(g));

        Optional<Galeria> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(galeriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Galeria> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
    }

    @Test
    void buscarPorProducto_shouldReturnFiltered() {
        Galeria g = new Galeria();
        when(galeriaRepository.findByProductoId(1L)).thenReturn(List.of(g));

        List<Galeria> actual = service.buscarPorProducto(1L);

        assertEquals(1, actual.size());
        verify(galeriaRepository).findByProductoId(1L);
    }

    @Test
    void guardar_shouldSave() {
        Galeria input = new Galeria();
        input.setUrlImagen("http://image.jpg");
        input.setFechaSubida(LocalDate.now());

        Galeria saved = new Galeria();
        saved.setId(1L);
        saved.setUrlImagen("http://image.jpg");
        when(galeriaRepository.save(any(Galeria.class))).thenReturn(saved);

        Galeria actual = service.guardar(input);

        assertEquals(1L, actual.getId());
        verify(galeriaRepository).save(any(Galeria.class));
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(galeriaRepository).deleteById(1L);
    }
}
