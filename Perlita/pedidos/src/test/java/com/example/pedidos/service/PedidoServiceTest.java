package com.example.pedidos.service;

import com.example.pedidos.model.Pedido;
import com.example.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    @Mock
    private WebClient productosWebClient;

    @Mock
    private WebClient calendarioWebClient;

    @InjectMocks
    private PedidoService service;

    @Test
    void listarTodos_shouldReturnAll() {
        List<Pedido> expected = List.of(new Pedido());
        when(repository.findAll()).thenReturn(expected);

        List<Pedido> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_whenExists_shouldReturn() {
        Pedido p = new Pedido();
        p.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Pedido> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Pedido> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
        verify(repository).findById(99L);
    }

    @Test
    void existePorId_whenExists_shouldReturnTrue() {
        when(repository.existsById(1L)).thenReturn(true);
        assertTrue(service.existePorId(1L));
        verify(repository).existsById(1L);
    }

    @Test
    void existePorId_whenNotExists_shouldReturnFalse() {
        when(repository.existsById(99L)).thenReturn(false);
        assertFalse(service.existePorId(99L));
    }

    @Test
    void guardar_cuandoFechaPedidoNull_debeAsignarFechaActual() {
        Pedido input = new Pedido();
        input.setDescripcion("Test");
        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido actual = service.guardar(input);

        assertNotNull(actual.getFechaPedido());
        assertEquals(LocalDate.now(), actual.getFechaPedido());
        verify(repository).save(any(Pedido.class));
    }

    @Test
    void guardar_cuandoFechaPedidoExistente_debeMantenerla() {
        LocalDate ayer = LocalDate.now().minusDays(1);
        Pedido input = new Pedido();
        input.setFechaPedido(ayer);
        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido actual = service.guardar(input);

        assertEquals(ayer, actual.getFechaPedido());
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void listarPorEstado_shouldReturn() {
        Pedido p = new Pedido();
        p.setEstado("pendiente");
        when(repository.findByEstado("pendiente")).thenReturn(List.of(p));

        List<Pedido> actual = service.listarPorEstado("pendiente");

        assertEquals(1, actual.size());
        verify(repository).findByEstado("pendiente");
    }

    @Test
    void listarPorCliente_shouldReturn() {
        Pedido p = new Pedido();
        p.setClienteId(5L);
        when(repository.findByClienteId(5L)).thenReturn(List.of(p));

        List<Pedido> actual = service.listarPorCliente(5L);

        assertEquals(1, actual.size());
        verify(repository).findByClienteId(5L);
    }

    @Test
    void cambiarEstado_transicionValida() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("pendiente");
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));
        Pedido actualizado = new Pedido();
        actualizado.setId(1L);
        actualizado.setEstado("en_fabricacion");
        when(repository.save(any(Pedido.class))).thenReturn(actualizado);

        ResponseEntity<Object> response = service.cambiarEstado(1L, "en_fabricacion");

        assertEquals(200, response.getStatusCode().value());
        Pedido body = (Pedido) response.getBody();
        assertNotNull(body);
        assertEquals("en_fabricacion", body.getEstado());
    }

    @Test
    void cambiarEstado_transicionInvalida() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("pendiente");
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        ResponseEntity<Object> response = service.cambiarEstado(1L, "entregado");

        assertEquals(400, response.getStatusCode().value());
        String msg = (String) response.getBody();
        assertNotNull(msg);
        assertTrue(msg.contains("Transición inválida"));
    }

    @Test
    void cambiarEstado_pedidoNoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = service.cambiarEstado(99L, "en_fabricacion");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void cambiarEstado_estadoEntregado_sinTransicion() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setEstado("entregado");
        when(repository.findById(1L)).thenReturn(Optional.of(pedido));

        ResponseEntity<Object> response = service.cambiarEstado(1L, "en_proceso");

        assertEquals(400, response.getStatusCode().value());
    }
}
