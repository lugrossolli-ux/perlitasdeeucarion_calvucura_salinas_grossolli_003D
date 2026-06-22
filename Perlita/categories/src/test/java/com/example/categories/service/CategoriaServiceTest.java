package com.example.categories.service;

import com.example.categories.model.Categoria;
import com.example.categories.model.ProductoCategoria;
import com.example.categories.repository.CategoriaRepository;
import com.example.categories.repository.ProductoCategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoCategoriaRepository productoCategoriaRepository;

    @InjectMocks
    private CategoriaService service;

    @Test
    void listarTodos_shouldReturnAll() {
        List<Categoria> expected = List.of(new Categoria());
        when(categoriaRepository.findAll()).thenReturn(expected);

        List<Categoria> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(categoriaRepository).findAll();
    }

    @Test
    void listarActivas_shouldReturnByStatus() {
        Categoria c = new Categoria();
        c.setActivo(1);
        when(categoriaRepository.findByActivo(1)).thenReturn(List.of(c));

        List<Categoria> actual = service.listarActivas(1);

        assertEquals(1, actual.size());
        verify(categoriaRepository).findByActivo(1);
    }

    @Test
    void buscarPorId_whenExists_shouldReturn() {
        Categoria c = new Categoria();
        c.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(c));

        Optional<Categoria> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(service.buscarPorId(99L).isEmpty());
    }

    @Test
    void guardar_shouldSave() {
        Categoria input = new Categoria();
        input.setNombre("Test");
        when(categoriaRepository.save(input)).thenReturn(input);

        Categoria actual = service.guardar(input);

        assertEquals("Test", actual.getNombre());
        verify(categoriaRepository).save(input);
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void obtenerProductosDe_shouldReturnProducts() {
        ProductoCategoria pc = new ProductoCategoria();
        when(productoCategoriaRepository.findByCategoria_Id(1L)).thenReturn(List.of(pc));

        List<ProductoCategoria> actual = service.obtenerProductosDe(1L);

        assertEquals(1, actual.size());
        verify(productoCategoriaRepository).findByCategoria_Id(1L);
    }

    @SuppressWarnings("unchecked")
    @Test
    void reasignarProductos_valida() {
        Categoria origen = new Categoria();
        origen.setId(1L);
        origen.setNombre("Origen");
        Categoria destino = new Categoria();
        destino.setId(2L);
        destino.setNombre("Destino");
        destino.setActivo(1);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(origen));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(productoCategoriaRepository.reasignarProductos(1L, 2L)).thenReturn(5);

        ResponseEntity<Object> response = service.reasignarProductos(1L, 2L);

        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(5, body.get("productosReasignados"));
        assertEquals("Origen", body.get("categoriaOrigen"));
        assertEquals("Destino", body.get("categoriaDestino"));
    }

    @Test
    void reasignarProductos_destinoInactivo() {
        Categoria origen = new Categoria();
        origen.setId(1L);
        Categoria destino = new Categoria();
        destino.setId(2L);
        destino.setActivo(0);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(origen));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.of(destino));

        ResponseEntity<Object> response = service.reasignarProductos(1L, 2L);

        assertEquals(400, response.getStatusCode().value());
        String msg = (String) response.getBody();
        assertNotNull(msg);
        assertTrue(msg.contains("inactiva"));
    }

    @Test
    void reasignarProductos_categoriaNoExiste() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());
        when(categoriaRepository.findById(99L)).thenReturn(Optional.of(new Categoria()));

        ResponseEntity<Object> response = service.reasignarProductos(1L, 99L);

        assertEquals(400, response.getStatusCode().value());
        String msg = (String) response.getBody();
        assertNotNull(msg);
        assertTrue(msg.contains("no existen"));
    }
}
