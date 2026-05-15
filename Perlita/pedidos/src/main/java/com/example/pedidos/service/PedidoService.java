package com.example.pedidos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.pedidos.model.Pedido;
import com.example.pedidos.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public boolean existePorId(Long id) {
        return repository.existsById(id);
    }

    public Pedido guardar(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }
        return repository.save(pedido);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Pedido> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }
}