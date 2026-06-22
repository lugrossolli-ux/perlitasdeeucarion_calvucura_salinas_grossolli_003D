package com.example.galeria.controller;

import com.example.galeria.model.Galeria;
import com.example.galeria.service.GaleriaService;
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

@WebMvcTest(GaleriaController.class)
@AutoConfigureMockMvc(addFilters = false)
class GaleriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GaleriaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Galeria()));

        mockMvc.perform(get("/galeria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Galeria g = new Galeria();
        g.setId(1L);
        g.setUrlImagen("http://img.jpg");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(g));

        mockMvc.perform(get("/galeria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/galeria/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorProducto_shouldReturn200() throws Exception {
        when(service.buscarPorProducto(1L)).thenReturn(List.of(new Galeria()));

        mockMvc.perform(get("/galeria/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Galeria input = new Galeria();
        input.setUrlImagen("http://img.jpg");
        input.setFechaSubida(LocalDate.now());
        input.setProductoId(1L);

        Galeria saved = new Galeria();
        saved.setId(1L);
        saved.setUrlImagen("http://img.jpg");
        saved.setProductoId(1L);
        when(service.guardar(any(Galeria.class))).thenReturn(saved);

        mockMvc.perform(post("/galeria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}
