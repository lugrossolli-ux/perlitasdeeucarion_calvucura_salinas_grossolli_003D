package com.example.productos.service;

import com.example.productos.model.Producto;
import com.example.productos.model.ProductoMaterial;
import com.example.productos.repository.ProductoMaterialRepository;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMaterialRepository productoMaterialRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private ProductoService service;

    @Test
    void listarTodos_shouldReturnAllProducts() {
        List<Producto> expected = List.of(new Producto());
        when(productoRepository.findAll()).thenReturn(expected);

        List<Producto> actual = service.listarTodos();

        assertEquals(expected, actual);
        verify(productoRepository).findAll();
    }

    @Test
    void listarActivos_shouldReturnActiveProducts() {
        List<Producto> expected = List.of(new Producto());
        when(productoRepository.findByActivoTrue()).thenReturn(expected);

        List<Producto> actual = service.listarActivos();

        assertEquals(expected, actual);
        verify(productoRepository).findByActivoTrue();
    }

    @Test
    void buscarPorId_whenExists_shouldReturnProduct() {
        Producto p = new Producto();
        p.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Producto> actual = service.buscarPorId(1L);

        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        verify(productoRepository).findById(1L);
    }

    @Test
    void buscarPorId_whenNotExists_shouldReturnEmpty() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> actual = service.buscarPorId(99L);

        assertTrue(actual.isEmpty());
        verify(productoRepository).findById(99L);
    }

    @Test
    void guardar_shouldSaveProduct() {
        Producto input = new Producto();
        input.setNombre("Test");
        when(productoRepository.save(input)).thenReturn(input);

        Producto actual = service.guardar(input);

        assertEquals("Test", actual.getNombre());
        verify(productoRepository).save(input);
    }

    @Test
    void eliminar_shouldDeleteById() {
        service.eliminar(1L);
        verify(productoRepository).deleteById(1L);
    }

    @Test
    void obtenerMateriales_shouldEnrichWithWebClient() {
        ProductoMaterial pm = new ProductoMaterial();
        pm.setId(1L);
        pm.setMaterialId(10L);
        List<ProductoMaterial> materiales = List.of(pm);
        when(productoMaterialRepository.findByProductoId(1L)).thenReturn(materiales);

        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8081/materiales/10")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just("material-data"));

        List<ProductoMaterial> actual = service.obtenerMateriales(1L);

        assertEquals(1, actual.size());
        assertEquals("material-data", actual.get(0).getDatosMaterial());
        verify(productoMaterialRepository).findByProductoId(1L);
    }

    @Test
    void agregarMaterial_shouldSaveAndReturn() {
        ProductoMaterial pm = new ProductoMaterial();
        when(productoMaterialRepository.save(pm)).thenReturn(pm);

        ProductoMaterial actual = service.agregarMaterial(pm);

        assertSame(pm, actual);
        verify(productoMaterialRepository).save(pm);
    }
}
