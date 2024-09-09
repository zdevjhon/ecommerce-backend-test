package com.ecommerce.test.controller;

import com.ecommerce.test.model.Carrito;
import com.ecommerce.test.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<Carrito> getAllCarritos() {
        return carritoService.getAllCarritos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getCarritoById(@PathVariable Long id) {
        Optional<Carrito> carrito = carritoService.getCarritoById(id);
        return carrito.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Carrito createCarrito(@RequestBody Carrito carrito) {
        return carritoService.saveCarrito(carrito);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrito> updateCarrito(@PathVariable Long id, @RequestBody Carrito carrito) {
        if (!carritoService.getCarritoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carrito.setId(id);
        return ResponseEntity.ok(carritoService.saveCarrito(carrito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrito(@PathVariable Long id) {
        if (!carritoService.getCarritoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carritoService.deleteCarrito(id);
        return ResponseEntity.noContent().build();
    }
}