package com.example.pedidos.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.pedidos.model.PedidoMaterial;
import com.example.pedidos.repository.PedidoMaterialRepository;

@Service
public class PedidoMaterialService {

    @Autowired
    private PedidoMaterialRepository repository;

    public List<PedidoMaterial> listarPorPedidoId(Long pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

    public Optional<PedidoMaterial> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public PedidoMaterial guardar(PedidoMaterial pedidoMaterial) {
        return repository.save(pedidoMaterial);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}