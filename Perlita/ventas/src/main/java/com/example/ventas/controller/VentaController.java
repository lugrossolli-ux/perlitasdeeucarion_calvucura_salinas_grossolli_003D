package com.example.ventas.controller;

import com.example.ventas.exception.ResourceNotFoundException;
import com.example.ventas.model.Venta;
import com.example.ventas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ventas")
@Tag(name = "Ventas", description = "Gestión de ventas y transacciones")
public class VentaController {

    @Autowired
    private VentaService service;

    @Operation(summary = "Listar todas las ventas")
    @GetMapping
    public List<Venta> listar() {
        return service.listarTodas();
    }

    @Operation(summary = "Buscar venta por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerCompleta(@PathVariable Long id) {
        Venta venta = service.obtenerVentaCompleta(id);
        if (venta == null) throw new ResourceNotFoundException("Venta no encontrada con id: " + id);
        return ResponseEntity.ok(venta);
    }

    @Operation(summary = "Listar ventas por cliente")
    @GetMapping("/cliente/{clienteId}")
    public List<Venta> porCliente(@PathVariable Long clienteId) {
        return service.buscarPorCliente(clienteId);
    }

    @Operation(summary = "Registrar una nueva venta",
               description = "El total se calcula automáticamente. Descuenta stock de productos.")
    @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente")
    @PostMapping
    public ResponseEntity<Venta> crear(@Valid @RequestBody Venta venta) {
        return new ResponseEntity<>(service.crearVenta(venta), HttpStatus.CREATED);
    }

    @Operation(summary = "Resumen de ventas por método de pago")
    @GetMapping("/resumen/metodo-pago")
    public List<Map<String, Object>> resumenPorMetodoPago() {
        return service.resumenPorMetodoPago();
    }
}
