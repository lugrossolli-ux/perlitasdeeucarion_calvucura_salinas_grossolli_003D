package com.example.productos.controller;

import com.example.productos.exception.ResourceNotFoundException;
import com.example.productos.model.Producto;
import com.example.productos.model.ProductoMaterial;
import com.example.productos.service.ProductoService;
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

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(new Producto()));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarActivos_shouldReturn200() throws Exception {
        when(service.listarActivos()).thenReturn(List.of(new Producto()));

        mockMvc.perform(get("/productos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_whenExists_shouldReturn200() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Test");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test"));
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturn404() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Producto input = new Producto();
        input.setNombre("Nuevo");
        input.setPrecioVenta(150.0);

        Producto saved = new Producto();
        saved.setId(1L);
        saved.setNombre("Nuevo");
        saved.setPrecioVenta(150.0);
        when(service.guardar(any(Producto.class))).thenReturn(saved);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void actualizar_whenExists_shouldReturn200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Producto()));

        Producto update = new Producto();
        update.setNombre("Actualizado");
        update.setPrecioVenta(200.0);
        when(service.guardar(any(Producto.class))).thenReturn(update);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizado"));
    }

    @Test
    void eliminar_whenExists_shouldReturn204() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(new Producto()));

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void verMateriales_shouldReturn200() throws Exception {
        when(service.obtenerMateriales(1L)).thenReturn(List.of(new ProductoMaterial()));

        mockMvc.perform(get("/productos/1/materiales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void agregarMaterial_whenProductoExists_shouldReturn201() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        when(service.buscarPorId(1L)).thenReturn(Optional.of(p));

        ProductoMaterial pm = new ProductoMaterial();
        pm.setId(1L);
        pm.setMaterialId(10L);
        pm.setCantidadRequerida(5.0);
        when(service.agregarMaterial(any(ProductoMaterial.class))).thenReturn(pm);

        mockMvc.perform(post("/productos/1/materiales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pm)))
                .andExpect(status().isCreated());
    }
}
