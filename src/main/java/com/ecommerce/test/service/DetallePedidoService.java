package com.ecommerce.test.service;

import com.ecommerce.test.model.DetallePedido;
import com.ecommerce.test.repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> getAllDetallesPedido() {
        return detallePedidoRepository.findAll();
    }

    public Optional<DetallePedido> getDetallePedidoById(Long id) {
        return detallePedidoRepository.findById(id);
    }

    public DetallePedido saveDetallePedido(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    public void deleteDetallePedido(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}