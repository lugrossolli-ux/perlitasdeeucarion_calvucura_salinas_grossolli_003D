package com.example.pedidos.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pedidos.model.PedidoMaterial;
import com.example.pedidos.service.PedidoMaterialService;

@RestController
@RequestMapping("/pedido-materiales")
public class PedidoMaterialController {

    @Autowired
    private PedidoMaterialService service;

    @GetMapping("/pedido/{pedidoId}")
    public List<PedidoMaterial> listarPorPedido(@PathVariable Long pedidoId) {
        return service.listarPorPedidoId(pedidoId);
    }

    @PostMapping
    public ResponseEntity<PedidoMaterial> crear(@RequestBody PedidoMaterial pedidoMaterial) {
        return new ResponseEntity<>(service.guardar(pedidoMaterial), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!service.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}