package com.example.ventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.ventas.model.Venta;
import com.example.ventas.service.VentaService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
@AutoConfigureMockMvc(addFilters = false)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_shouldReturn200() throws Exception {
        when(service.listarTodas()).thenReturn(List.of(new Venta()));

        mockMvc.perform(get("/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void obtenerCompleta_whenExists_shouldReturn200() throws Exception {
        Venta v = new Venta();
        v.setId(1L);
        v.setTotal(500.0);
        v.setFecha(LocalDate.now());
        when(service.obtenerVentaCompleta(1L)).thenReturn(v);

        mockMvc.perform(get("/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void obtenerCompleta_whenNotExists_shouldReturn404() throws Exception {
        when(service.obtenerVentaCompleta(99L)).thenReturn(null);

        mockMvc.perform(get("/ventas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void porCliente_shouldReturn200() throws Exception {
        when(service.buscarPorCliente(5L)).thenReturn(List.of(new Venta()));

        mockMvc.perform(get("/ventas/cliente/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void crear_shouldReturn201() throws Exception {
        Venta input = new Venta();
        input.setClienteId(1L);
        input.setMetodoPago("efectivo");
        input.setTotal(200.0);

        Venta saved = new Venta();
        saved.setId(1L);
        saved.setClienteId(1L);
        saved.setMetodoPago("efectivo");
        saved.setTotal(200.0);
        saved.setFecha(LocalDate.now());

        when(service.crearVenta(any(Venta.class))).thenReturn(saved);

        mockMvc.perform(post("/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.metodoPago").value("efectivo"));
    }

    @Test
    void resumenPorMetodoPago_shouldReturn200() throws Exception {
        when(service.resumenPorMetodoPago()).thenReturn(List.of(
                Map.of("metodoPago", "efectivo", "cantidadVentas", 5L, "totalRecaudado", 1500.0)
        ));

        mockMvc.perform(get("/ventas/resumen/metodo-pago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].metodoPago").value("efectivo"));
    }
}
