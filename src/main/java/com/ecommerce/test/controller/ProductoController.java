package com.ecommerce.test.controller;

import com.ecommerce.test.model.Producto;
import com.ecommerce.test.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/productos")
public class ProductoController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear producto con imagen
    @PostMapping
    public Producto createProducto(@RequestParam("nombre") String nombre,
                                   @RequestParam("precio") Double precio,
                                   @RequestParam("breveDescripcion") String breveDescripcion,
                                   @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setBreveDescripcion(breveDescripcion);

        // Verificar si la imagen fue recibida
        if (file != null && !file.isEmpty()) {
            System.out.println("Imagen recibida: " + file.getOriginalFilename());
            String fileName = guardarImagen(file);
            producto.setFoto(fileName); // Guardar el nombre o ruta de la imagen en el producto
        } else {
            System.out.println("No se recibió ninguna imagen.");
        }

        return productoService.saveProducto(producto);

    }


    // Actualizar producto con o sin imagen
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id,
                                                   @RequestParam("nombre") String nombre,
                                                   @RequestParam("precio") Double precio,
                                                   @RequestParam("breveDescripcion") String breveDescripcion,
                                                   @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Optional<Producto> optionalProducto = productoService.getProductoById(id);
        if (!optionalProducto.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Producto producto = optionalProducto.get();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setBreveDescripcion(breveDescripcion);

        if (file != null && !file.isEmpty()) {
            // Eliminar la imagen anterior si existe
            if (producto.getFoto() != null) {
                eliminarImagen(producto.getFoto());
            }
            // Guardar la nueva imagen
            String fileName = guardarImagen(file);
            producto.setFoto(fileName);
        }

        return ResponseEntity.ok(productoService.saveProducto(producto));
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) throws IOException {
        Optional<Producto> producto = productoService.getProductoById(id);
        if (!producto.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Eliminar la imagen si existe
        if (producto.get().getFoto() != null) {
            eliminarImagen(producto.get().getFoto());
        }

        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    /*private String guardarImagen(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return fileName;
    }*/

    public String guardarImagen(MultipartFile file) throws IOException {
        // Definir la ruta donde se almacenarán las imágenes
        String uploadDir = "src/main/resources/static/images/";

        // Crear el nombre del archivo con el nombre original del archivo
        String fileName = file.getOriginalFilename();

        // Asegurarse de que la carpeta de imágenes exista
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Guardar el archivo en el sistema de archivos
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("No se pudo guardar el archivo: " + fileName, e);
        }

        // Devolver el nombre del archivo para guardarlo en la base de datos
        return fileName;
    }

    // Método para eliminar una imagen del servidor
    private void eliminarImagen(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.deleteIfExists(filePath);
    }
}
