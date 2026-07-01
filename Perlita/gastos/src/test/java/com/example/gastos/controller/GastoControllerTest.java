package com.example.gastos.controller;

import com.example.gastos.exception.ResourceNotFoundException;
import com.example.gastos.model.Gasto;
import com.example.gastos.service.GastoService;
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

@WebMvcTest(GastoController.class)
@AutoConfigureMockMvc(addFilters = false)
class GastoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GastoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Gasto()));

        mockMvc.perform(get("/gastos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Gasto g = new Gasto();
        g.setId(1L);
        g.setDescripcion("Test");
        g.setMonto(100.0);
        when(service.buscarPorId(1L)).thenReturn(Optional.of(g));

        mockMvc.perform(get("/gastos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Test"));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/gastos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Gasto input = new Gasto();
        input.setDescripcion("Nuevo gasto");
        input.setMonto(500.0);
        input.setFecha(LocalDate.now());
        input.setCategoriaId(1L);

        Gasto saved = new Gasto();
        saved.setId(1L);
        saved.setDescripcion("Nuevo gasto");
        saved.setMonto(500.0);
        saved.setFecha(LocalDate.now());
        saved.setCategoriaId(1L);

        when(service.guardar(any(Gasto.class))).thenReturn(saved);

        mockMvc.perform(post("/gastos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_whenExists_shouldReturn200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Gasto()));

        Gasto update = new Gasto();
        update.setDescripcion("Actualizado");
        update.setMonto(999.0);
        update.setFecha(LocalDate.now());
        update.setCategoriaId(1L);
        when(service.guardar(any(Gasto.class))).thenReturn(update);

        mockMvc.perform(put("/gastos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("Actualizado"));
    }

    @Test
    void eliminar_whenExists_shouldReturn204() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Gasto()));

        mockMvc.perform(delete("/gastos/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }
}
