package com.ecommerce.test.controller;

import com.ecommerce.test.model.DetallePedido;
import com.ecommerce.test.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public List<DetallePedido> getAllDetallesPedido() {
        return detallePedidoService.getAllDetallesPedido();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> getDetallePedidoById(@PathVariable Long id) {
        Optional<DetallePedido> detallePedido = detallePedidoService.getDetallePedidoById(id);
        return detallePedido.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public DetallePedido createDetallePedido(@RequestBody DetallePedido detallePedido) {
        return detallePedidoService.saveDetallePedido(detallePedido);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> updateDetallePedido(@PathVariable Long id, @RequestBody DetallePedido detallePedido) {
        if (!detallePedidoService.getDetallePedidoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        detallePedido.setId(id);
        return ResponseEntity.ok(detallePedidoService.saveDetallePedido(detallePedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetallePedido(@PathVariable Long id) {
        if (!detallePedidoService.getDetallePedidoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        detallePedidoService.deleteDetallePedido(id);
        return ResponseEntity.noContent().build();
    }
}