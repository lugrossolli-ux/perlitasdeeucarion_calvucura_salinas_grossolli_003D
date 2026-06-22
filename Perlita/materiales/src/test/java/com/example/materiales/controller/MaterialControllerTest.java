package com.example.materiales.controller;

import com.example.materiales.exception.ResourceNotFoundException;
import com.example.materiales.model.Material;
import com.example.materiales.service.MaterialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaterialController.class)
@AutoConfigureMockMvc(addFilters = false)
class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaterialService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200WithList() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Material()));

        mockMvc.perform(get("/materiales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(service).listarTodos();
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Material m = new Material();
        m.setId(1L);
        m.setNombre("Test");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(m));

        mockMvc.perform(get("/materiales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test"));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/materiales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Material input = new Material();
        input.setNombre("Nuevo");
        input.setUnidadMedida("unidad");
        input.setStockActual(10.0);
        input.setStockMinimo(5.0);
        input.setPrecioUnitario(100.0);

        Material saved = new Material();
        saved.setId(1L);
        saved.setNombre("Nuevo");
        saved.setUnidadMedida("unidad");
        saved.setStockActual(10.0);
        saved.setStockMinimo(5.0);
        saved.setPrecioUnitario(100.0);
        saved.setFechaActualizacion(LocalDate.now());

        when(service.guardar(any(Material.class))).thenReturn(saved);

        mockMvc.perform(post("/materiales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void actualizar_whenExists_shouldReturn200() throws Exception {
        Material existing = new Material();
        existing.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(Optional.of(existing));

        Material update = new Material();
        update.setNombre("Actualizado");
        update.setUnidadMedida("kg");
        update.setStockActual(20.0);
        update.setStockMinimo(10.0);
        update.setPrecioUnitario(200.0);
        when(service.guardar(any(Material.class))).thenReturn(update);

        mockMvc.perform(put("/materiales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizado"));
    }

    @Test
    void eliminar_whenExists_shouldReturn204() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Material()));

        mockMvc.perform(delete("/materiales/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void stockBajo_shouldReturn200() throws Exception {
        when(service.stockBajo()).thenReturn(List.of(new Material()));

        mockMvc.perform(get("/materiales/stock-bajo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
