package com.example.clase9ws20232.controller;

import com.example.clase9ws20232.entity.Product;
import com.example.clase9ws20232.entity.Supplier;
import com.example.clase9ws20232.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductoController {

    final ProductRepository productRepository;

    public ProductoController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //LISTAR
    @GetMapping(value = {"/list", ""})
    public List<Product> listaProductos() {
        return productRepository.findAll();
    }

    //OBTENER
    @GetMapping(value = "/{id}")
    public ResponseEntity<HashMap<String, Object>> buscarProducto(@PathVariable("id") String idStr) {

        HashMap<String, Object> responseJson = new HashMap<>();
        try {
            Optional<Product> optProduct = productRepository.findById(Integer.parseInt(idStr));
            if (optProduct.isPresent()) {
                responseJson.put("result", "success");
                responseJson.put("product", optProduct.get());
                return ResponseEntity.ok (responseJson);
            } else {
                responseJson.put("msg", "Producto no encontrado");
            }

        } catch (NumberFormatException e) {
            responseJson.put("msg", "el ID debe ser un número entero positivo");
        }

        responseJson.put("result", "failure");
        return ResponseEntity.badRequest().body(responseJson);
        //try {
        //    int id = Integer.parseInt(idStr);
        //    Optional<Product> byId = productRepository.findById(id);

        //    HashMap<String, Object> respuesta = new HashMap<>();

        //    if (byId.isPresent()) {
        //        respuesta.put("result", "ok");
        //        respuesta.put("producto", byId.get());
        //    } else {
        //        respuesta.put("result", "no existe");
        //    }
        //    return ResponseEntity.ok(respuesta);
        //} catch (NumberFormatException e) {
        //    return ResponseEntity.badRequest().body(null);
        // }
    }

    // CREAR /product y /product/
    @PostMapping(value = {"", "/"})
    public ResponseEntity<HashMap<String, Object>> guardarProducto(
            @RequestBody Product product,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseJson = new HashMap<>();

        productRepository.save(product);
        if (fetchId) {
            responseJson.put("id", product.getId());
        }
        responseJson.put("estado", "creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    // ACTUALIZAR
    // Actualizar producto
    @PutMapping(value = {"", "/"})
    public ResponseEntity<HashMap<String, Object>> actualizarProducto(@RequestBody Product product) {
        HashMap<String, Object> responseMap = new HashMap<>();

        System.out.println("Producto recibido: " + product);

        if (product.getId() != null && product.getId() > 0) {
            Optional<Product> opt = productRepository.findById(product.getId());
            if (opt.isPresent()) {
                System.out.println("Producto encontrado en la base de datos: " + opt.get());
                productRepository.save(product);
                responseMap.put("estado", "actualizado");
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("estado", "error");
                responseMap.put("msg", "El ID del producto enviado no existe");
                return ResponseEntity.badRequest().body(responseMap);
            }
        } else {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un ID válido");
            return ResponseEntity.badRequest().body(responseMap);
        }
    }
    // /Product?id
    @DeleteMapping("")
    public ResponseEntity<HashMap<String, Object>> borrar(@RequestParam("id") String idStr){

        try{
            int id = Integer.parseInt(idStr);

            HashMap<String, Object> rpta = new HashMap<>();

            Optional<Product> byId = productRepository.findById(id);
            if(byId.isPresent()){
                productRepository.deleteById(id);
                rpta.put("result","ok");
            }else{
                rpta.put("result","no ok");
                rpta.put("msg","el ID enviado no existe");
            }

            return ResponseEntity.ok(rpta);
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping(value = "/prueba", produces = MediaType.APPLICATION_JSON_VALUE)
    public String prueba() {
        return "{\"msg\": \"esto es una prueba\"}";
    }

    @GetMapping("/buscar/{id}")
    public Product buscarF1(@PathVariable("id") int id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            return null;
        }
    }

    /*
    si id existe -> {result: "ok", producto: <producto>}
    si el id no existe -> {result: "no existe"}
     */
    @GetMapping("/buscar2/{id}")
    public HashMap<String, Object> buscarF2(@PathVariable("id") int id) {

        HashMap<String, Object> respuesta = new HashMap<>();

        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            respuesta.put("result", "ok");
            respuesta.put("producto", byId.get());
            return respuesta;
        } else {
            respuesta.put("result", "no existe");
            return respuesta;
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, String>> gestionException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        HashMap<String, String> responseMap = new HashMap<>();
        System.out.println("Error en la solicitud: " + ex.getMessage());
        if ("POST".equals(request.getMethod()) || "PUT".equals(request.getMethod())) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un producto");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }


}
