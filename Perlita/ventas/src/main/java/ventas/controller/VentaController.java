package ventas.controller;

import ventas.model.Venta;
import ventas.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaService service;

    @GetMapping
    public List<Venta> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerCompleta(@PathVariable Long id) {
        Venta venta = service.obtenerVentaCompleta(id);
        if (venta == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(venta);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<Venta> porCliente(@PathVariable Long clienteId) {
        return service.buscarPorCliente(clienteId);
    }

    @PostMapping
    public ResponseEntity<Venta> crear(@RequestBody Venta venta) {
        return new ResponseEntity<>(service.crearVenta(venta), HttpStatus.CREATED);
    }
}