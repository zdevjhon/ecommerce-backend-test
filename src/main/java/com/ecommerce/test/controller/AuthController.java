package com.ecommerce.test.controller;

import com.ecommerce.test.model.AuthRequest;
import com.ecommerce.test.model.Usuario;
import com.ecommerce.test.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/authenticate")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /*@PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            Usuario usuario = usuarioService.findByEmail(authRequest.getEmail());
            if (usuario != null && authRequest.getContrasena().equals(usuario.getContrasena())) {
                return ResponseEntity.ok("Autenticación exitosa");
            } else {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }*/

    /*@PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            Usuario usuario = usuarioService.findByEmail(authRequest.getEmail());
            if (usuario != null && authRequest.getContrasena().equals(usuario.getContrasena())) {
                return ResponseEntity.ok(Map.of("message", "Autenticación exitosa"));
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }
    }*/

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            Usuario usuario = usuarioService.findByEmail(authRequest.getEmail());
            if (usuario != null && authRequest.getContrasena().equals(usuario.getContrasena())) {
                // Devolver una respuesta exitosa con los datos del usuario
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Autenticación exitosa");
                response.put("usuario", Map.of(
                        "id", usuario.getId(),
                        "nombre", usuario.getNombre(),
                        "email", usuario.getEmail()
                ));
                return ResponseEntity.ok(response);
            } else {
                // Credenciales inválidas
                return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            // Manejo de excepciones
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales inválidas"));
        }
    }
}