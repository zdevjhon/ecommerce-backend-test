package com.ecommerce.test.controller;

import com.ecommerce.test.dto.DetallePedidoRequest;
import com.ecommerce.test.dto.PedidoRequest;
import com.ecommerce.test.model.DetallePedido;
import com.ecommerce.test.model.Pedido;
import com.ecommerce.test.model.Producto;
import com.ecommerce.test.model.Usuario;
import com.ecommerce.test.repository.PedidoRepository;
import com.ecommerce.test.repository.ProductoRepository;
import com.ecommerce.test.service.DetallePedidoService;
import com.ecommerce.test.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-pedidos")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

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

    /*@PostMapping
    public DetallePedido createDetallePedido(@RequestBody DetallePedido detallePedido) {
        return detallePedidoService.saveDetallePedido(detallePedido);
    }*/

    @PostMapping
    public ResponseEntity<DetallePedido> createDetallePedido(@RequestBody DetallePedidoRequest detallePedidoRequest) {
        // Buscar el peido por ID
        Optional<Pedido> optionalPedido = pedidoRepository.findById(detallePedidoRequest.getPedido_id());
        if (optionalPedido.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado");
        }

        Pedido pedido = optionalPedido.get();

        Optional<Producto> optionalProducto = productoRepository.findById(detallePedidoRequest.getProducto_id());
        if (optionalProducto.isEmpty()) {
            throw new RuntimeException("producto no encontrado");
        }

        Producto producto = optionalProducto.get();

        // Crear y configurar el pedido
        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(detallePedidoRequest.getCantidad());
        detalle.setPrecio(detallePedidoRequest.getPrecio());

        DetallePedido saved = detallePedidoService.saveDetallePedido(detalle);

        if (saved != null) {
            return ResponseEntity.ok(saved); // Devolvemos el pedido con el ID incluido
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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