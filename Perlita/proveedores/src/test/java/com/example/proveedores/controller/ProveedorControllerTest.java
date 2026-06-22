package com.example.proveedores.controller;

import com.example.proveedores.model.Proveedor;
import com.example.proveedores.service.ProveedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProveedorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProveedorService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Proveedor()));

        mockMvc.perform(get("/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarActivos_shouldReturn200() throws Exception {
        Proveedor p = new Proveedor();
        p.setActivo(1);
        when(service.listarActivos()).thenReturn(List.of(p));

        mockMvc.perform(get("/proveedores/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(1));
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Proveedor p = new Proveedor();
        p.setId(1L);
        p.setNombre("Test");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test"));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/proveedores/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Proveedor input = new Proveedor();
        input.setNombre("Nuevo Proveedor");

        Proveedor saved = new Proveedor();
        saved.setId(1L);
        saved.setNombre("Nuevo Proveedor");
        when(service.guardar(any(Proveedor.class))).thenReturn(saved);

        mockMvc.perform(post("/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nuevo Proveedor"));
    }

    @Test
    void actualizar_whenExists_shouldReturn200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Proveedor()));

        Proveedor update = new Proveedor();
        update.setNombre("Actualizado");
        when(service.guardar(any(Proveedor.class))).thenReturn(update);

        mockMvc.perform(put("/proveedores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizado"));
    }

    @Test
    void eliminar_whenExists_shouldReturn204() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Proveedor()));

        mockMvc.perform(delete("/proveedores/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void desactivar_shouldReturn200() throws Exception {
        Proveedor p = new Proveedor();
        p.setId(1L);
        p.setActivo(1);
        when(service.buscarPorId(1L)).thenReturn(Optional.of(p));

        Proveedor desactivado = new Proveedor();
        desactivado.setId(1L);
        desactivado.setActivo(0);
        when(service.guardar(any(Proveedor.class))).thenReturn(desactivado);

        mockMvc.perform(patch("/proveedores/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(0));
    }

    @Test
    void activar_shouldReturn200() throws Exception {
        Proveedor p = new Proveedor();
        p.setId(1L);
        p.setActivo(0);
        when(service.buscarPorId(1L)).thenReturn(Optional.of(p));

        Proveedor activado = new Proveedor();
        activado.setId(1L);
        activado.setActivo(1);
        when(service.guardar(any(Proveedor.class))).thenReturn(activado);

        mockMvc.perform(patch("/proveedores/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(1));
    }
}
