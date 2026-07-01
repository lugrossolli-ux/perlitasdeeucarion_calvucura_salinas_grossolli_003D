package com.example.categories.controller;

import com.example.categories.model.Categoria;
import com.example.categories.model.ProductoCategoria;
import com.example.categories.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Categoria()));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarActivas_shouldReturn200() throws Exception {
        Categoria c = new Categoria();
        c.setActivo(1);
        when(service.listarActivas(1)).thenReturn(List.of(c));

        mockMvc.perform(get("/categorias/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(1));
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Categoria c = new Categoria();
        c.setId(1L);
        c.setNombre("Test");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test"));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categorias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Categoria input = new Categoria();
        input.setNombre("Nueva Categoría");

        Categoria saved = new Categoria();
        saved.setId(1L);
        saved.setNombre("Nueva Categoría");
        when(service.guardar(any(Categoria.class))).thenReturn(saved);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nueva Categoría"));
    }

    @Test
    void actualizar_whenExists_shouldReturn200() throws Exception {
        Categoria existente = new Categoria();
        existente.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(Optional.of(existente));

        Categoria saved = new Categoria();
        saved.setId(1L);
        saved.setNombre("Actualizada");
        when(service.guardar(any(Categoria.class))).thenReturn(saved);

        Categoria input = new Categoria();
        input.setNombre("Actualizada");
        input.setDescripcion("Desc");

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizada"));
    }

    @Test
    void eliminar_whenExists_shouldReturn204() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Categoria()));

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void obtenerProductosDe_shouldReturn200() throws Exception {
        when(service.obtenerProductosDe(1L)).thenReturn(List.of(new ProductoCategoria()));

        mockMvc.perform(get("/categorias/obtenerProducto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void reasignar_valid_shouldReturn200() throws Exception {
        Map<String, Object> respuesta = Map.of(
                "productosReasignados", 3,
                "categoriaOrigen", "Vieja",
                "categoriaDestino", "Nueva"
        );
        when(service.reasignarProductos(1L, 2L))
                .thenReturn(ResponseEntity.ok((Object) respuesta));

        mockMvc.perform(patch("/categorias/reasignar")
                        .param("origenId", "1")
                        .param("destinoId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productosReasignados").value(3));
    }

    @Test
    void reasignar_invalid_shouldReturn400() throws Exception {
        when(service.reasignarProductos(1L, 99L))
                .thenReturn(ResponseEntity.badRequest().body((Object) "Una o ambas categorías no existen."));

        mockMvc.perform(patch("/categorias/reasignar")
                        .param("origenId", "1")
                        .param("destinoId", "99"))
                .andExpect(status().isBadRequest());
    }
}
