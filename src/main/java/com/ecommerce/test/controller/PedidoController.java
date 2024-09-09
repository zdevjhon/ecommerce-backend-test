package com.ecommerce.test.controller;

import com.ecommerce.test.dto.PedidoRequest;
import com.ecommerce.test.model.Pedido;
import com.ecommerce.test.model.Usuario;
import com.ecommerce.test.service.PedidoService;
import com.ecommerce.test.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService; // Servicio para obtener Usuario

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.getPedidoById(id);
        return pedido.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody PedidoRequest pedidoRequest) {
        // Buscar el usuario por ID
        Usuario usuario = usuarioService.findById(pedidoRequest.getUsuario_id());

        if (usuario == null) {
            return ResponseEntity.badRequest().body(null); // Usuario no encontrado
        }

        // Crear y configurar el pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(pedidoRequest.getFecha());
        pedido.setEstado(pedidoRequest.getEstado());
        pedido.setTotal(pedidoRequest.getTotal());

        Pedido savedPedido = pedidoService.savePedido(pedido);
        if (savedPedido != null) {
            return ResponseEntity.ok(savedPedido); // Devolvemos el pedido con el ID incluido
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*@PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        Pedido savedPedido = pedidoService.savePedido(pedido);
        if (savedPedido != null) {
            return ResponseEntity.ok(savedPedido); // Devolvemos el pedido con el ID incluido
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        if (!pedidoService.getPedidoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        pedido.setId(id);
        return ResponseEntity.ok(pedidoService.savePedido(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        if (!pedidoService.getPedidoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }
}