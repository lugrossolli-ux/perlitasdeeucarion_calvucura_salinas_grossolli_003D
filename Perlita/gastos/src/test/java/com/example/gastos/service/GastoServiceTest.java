package com.example.gastos.service;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.Gasto;
import com.example.gastos.repository.GastoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastoServiceTest {

    @Mock
    private GastoRepository repository;

    @Mock
    private WebClient proveedoresWebClient;

    @InjectMocks
    private GastoService service;

    @Test
    void listarTodos_shouldReturnAll() {
        List<Gasto> expected = List.of(new Gasto());
        when(repository.findAll()).thenReturn(expected);

        List<Gasto> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_whenExists_shouldReturn() {
        Gasto g = new Gasto();
        g.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(g));

        Optional<Gasto> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Gasto> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
    }

    @Test
    void listarPorCategoria_shouldReturnFiltered() {
        Gasto g = new Gasto();
        when(repository.findByCategoriaId(1L)).thenReturn(List.of(g));

        List<Gasto> actual = service.listarPorCategoria(1L);

        assertEquals(1, actual.size());
        verify(repository).findByCategoriaId(1L);
    }

    @Test
    void guardar_whenSinProveedor_shouldSetFechaAndSave() {
        Gasto input = new Gasto();
        input.setDescripcion("Test");
        input.setMonto(100.0);

        Gasto saved = new Gasto();
        saved.setId(1L);
        saved.setDescripcion("Test");
        saved.setMonto(100.0);
        saved.setFecha(LocalDate.now());

        when(repository.save(any(Gasto.class))).thenReturn(saved);

        Gasto actual = service.guardar(input);

        assertNotNull(actual.getFecha());
        assertEquals(LocalDate.now(), actual.getFecha());
        verify(repository).save(any(Gasto.class));
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}
