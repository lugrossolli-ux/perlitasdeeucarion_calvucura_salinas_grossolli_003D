package com.example.proveedores.service;

import com.example.proveedores.model.Proveedor;
import com.example.proveedores.repository.ProveedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository repository;

    @InjectMocks
    private ProveedorService service;

    @Test
    void listarTodos_shouldReturnAll() {
        List<Proveedor> expected = List.of(new Proveedor());
        when(repository.findAll()).thenReturn(expected);

        List<Proveedor> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(repository).findAll();
    }

    @Test
    void listarActivos_shouldReturnActive() {
        Proveedor p = new Proveedor();
        p.setActivo(1);
        when(repository.findByActivo(1)).thenReturn(List.of(p));

        List<Proveedor> actual = service.listarActivos();

        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getActivo());
        verify(repository).findByActivo(1);
    }

    @Test
    void buscarPorId_whenExists_shouldReturn() {
        Proveedor p = new Proveedor();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Proveedor> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Proveedor> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
        verify(repository).findById(99L);
    }

    @Test
    void guardar_shouldSave() {
        Proveedor input = new Proveedor();
        input.setNombre("Test");
        when(repository.save(input)).thenReturn(input);

        Proveedor actual = service.guardar(input);

        assertEquals("Test", actual.getNombre());
        verify(repository).save(input);
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }
}
