package com.example.pedidos.controller;

import com.example.pedidos.model.Pedido;
import com.example.pedidos.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Pedido()));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Pedido p = new Pedido();
        p.setId(1L);
        p.setDescripcion("Test");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Test"));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Pedido input = new Pedido();
        input.setClienteId(1L);
        input.setDescripcion("Nuevo pedido");
        input.setTotal(BigDecimal.valueOf(500));

        Pedido saved = new Pedido();
        saved.setId(1L);
        saved.setClienteId(1L);
        saved.setDescripcion("Nuevo pedido");
        saved.setTotal(BigDecimal.valueOf(500));
        saved.setFechaPedido(LocalDate.now());
        when(service.guardar(any(Pedido.class))).thenReturn(saved);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Nuevo pedido"));
    }

    @Test
    void actualizar_whenExists_shouldReturn200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Pedido()));

        Pedido update = new Pedido();
        update.setDescripcion("Actualizado");
        update.setTotal(BigDecimal.valueOf(999));
        when(service.guardar(any(Pedido.class))).thenReturn(update);

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Actualizado"));
    }

    @Test
    void eliminar_whenExists_shouldReturn204() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Pedido()));

        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void listarPorEstado_shouldReturn200() throws Exception {
        Pedido p = new Pedido();
        p.setEstado("pendiente");
        when(service.listarPorEstado("pendiente")).thenReturn(List.of(p));

        mockMvc.perform(get("/pedidos/estado/pendiente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("pendiente"));
    }

    @Test
    void cambiarEstado_valido_shouldReturn200() throws Exception {
        Pedido actualizado = new Pedido();
        actualizado.setId(1L);
        actualizado.setEstado("en_fabricacion");
        when(service.cambiarEstado(1L, "en_fabricacion"))
                .thenReturn(ResponseEntity.ok((Object) actualizado));

        mockMvc.perform(patch("/pedidos/1/estado")
                        .param("nuevoEstado", "en_fabricacion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("en_fabricacion"));
    }

    @Test
    void cambiarEstado_invalido_shouldReturn400() throws Exception {
        when(service.cambiarEstado(1L, "entregado"))
                .thenReturn(ResponseEntity.badRequest().body((Object) "Transición inválida"));

        mockMvc.perform(patch("/pedidos/1/estado")
                        .param("nuevoEstado", "entregado"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPorCliente_shouldReturn200() throws Exception {
        when(service.listarPorCliente(5L)).thenReturn(List.of(new Pedido()));

        mockMvc.perform(get("/pedidos/cliente/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
