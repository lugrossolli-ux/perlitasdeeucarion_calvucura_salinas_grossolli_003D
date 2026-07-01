package com.example.pedidos.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.example.pedidos.model.PedidoMaterial;
import com.example.pedidos.service.PedidoMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pedido-materiales")
@Tag(name = "Materiales del Pedido", description = "Gestión de materiales reservados para pedidos")
public class PedidoMaterialController {

    @Autowired
    private PedidoMaterialService service;

    @Operation(summary = "Listar materiales de un pedido")
    @GetMapping("/pedido/{pedidoId}")
    public List<PedidoMaterial> listarPorPedido(@PathVariable Long pedidoId) {
        return service.listarPorPedidoId(pedidoId);
    }

    @Operation(summary = "Asignar material a un pedido")
    @ApiResponse(responseCode = "201", description = "Material asignado exitosamente")
    @PostMapping
    public ResponseEntity<PedidoMaterial> crear(@Valid @RequestBody PedidoMaterial pedidoMaterial) {
        return new ResponseEntity<>(service.guardar(pedidoMaterial), HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar material de un pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Material eliminado del pedido"),
            @ApiResponse(responseCode = "404", description = "Material no encontrado en el pedido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!service.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
