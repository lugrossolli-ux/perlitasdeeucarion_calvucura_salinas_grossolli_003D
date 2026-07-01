package com.example.ventas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.example.ventas.model.Venta;
import com.example.ventas.model.VentaProducto;
import com.example.ventas.repository.VentaProductoRepository;
import com.example.ventas.repository.VentaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private VentaProductoRepository ventaProductoRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private VentaService service;

    @Test
    void listarTodas_shouldReturnAllSales() {
        List<Venta> expected = List.of(new Venta());
        when(ventaRepository.findAll()).thenReturn(expected);

        List<Venta> actual = service.listarTodas();

        assertEquals(expected, actual);
        verify(ventaRepository).findAll();
    }

    @Test
    void buscarPorId_whenExists_shouldReturnSale() {
        Venta v = new Venta();
        v.setId(1L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(v));

        Optional<Venta> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        verify(ventaRepository).findById(1L);
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Venta> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
        verify(ventaRepository).findById(99L);
    }

    @Test
    void buscarPorCliente_shouldReturnSalesByClient() {
        Venta v = new Venta();
        v.setClienteId(5L);
        List<Venta> expected = List.of(v);
        when(ventaRepository.findByClienteId(5L)).thenReturn(expected);

        List<Venta> actual = service.buscarPorCliente(5L);

        assertEquals(expected, actual);
        verify(ventaRepository).findByClienteId(5L);
    }

    @Test
    void crearVenta_conProductos_debeCalcularTotalYGuardar() {
        Venta venta = new Venta();
        venta.setClienteId(1L);
        VentaProducto vp1 = new VentaProducto();
        vp1.setPrecioUnitario(100.0);
        vp1.setCantidad(2);
        VentaProducto vp2 = new VentaProducto();
        vp2.setPrecioUnitario(50.0);
        vp2.setCantidad(3);
        venta.setProductos(List.of(vp1, vp2));

        Venta guardada = new Venta();
        guardada.setId(1L);
        guardada.setClienteId(1L);
        guardada.setFecha(LocalDate.now());
        guardada.setTotal(350.0);
        guardada.setProductos(List.of(vp1, vp2));

        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just("datos-producto"));
        when(ventaRepository.save(any(Venta.class))).thenReturn(guardada);

        Venta actual = service.crearVenta(venta);

        assertNotNull(actual);
        assertEquals(350.0, actual.getTotal());
        verify(ventaRepository).save(any(Venta.class));
        verify(ventaProductoRepository).saveAll(anyList());
    }

    @Test
    void crearVenta_sinProductos_debeGuardarDirectamente() {
        Venta venta = new Venta();
        venta.setClienteId(1L);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta actual = service.crearVenta(venta);

        assertNotNull(actual);
        assertNotNull(actual.getFecha());
        assertEquals(LocalDate.now(), actual.getFecha());
        verify(ventaRepository).save(any(Venta.class));
        verify(ventaProductoRepository, never()).saveAll(anyList());
    }

    @Test
    void obtenerVentaCompleta_whenExists_shouldReturnEnriched() {
        Venta venta = new Venta();
        venta.setId(1L);
        VentaProducto vp = new VentaProducto();
        vp.setProductoId(10L);
        venta.setProductos(List.of(vp));
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just("datos"));

        Venta actual = service.obtenerVentaCompleta(1L);

        assertNotNull(actual);
        assertEquals("datos", actual.getProductos().get(0).getDatosProducto());
    }

    @Test
    void obtenerVentaCompleta_whenNotExists_shouldReturnNull() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());
        assertNull(service.obtenerVentaCompleta(99L));
    }

    @Test
    void resumenPorMetodoPago_shouldReturnMappedResults() {
        Object[] fila1 = {"efectivo", 5L, 1500.0};
        Object[] fila2 = {"tarjeta", 3L, 900.0};
        when(ventaRepository.resumenPorMetodoPago()).thenReturn(List.of(fila1, fila2));

        List<Map<String, Object>> actual = service.resumenPorMetodoPago();

        assertEquals(2, actual.size());
        assertEquals("efectivo", actual.get(0).get("metodoPago"));
        assertEquals(5L, actual.get(0).get("cantidadVentas"));
        assertEquals(1500.0, actual.get(0).get("totalRecaudado"));
        verify(ventaRepository).resumenPorMetodoPago();
    }
}
