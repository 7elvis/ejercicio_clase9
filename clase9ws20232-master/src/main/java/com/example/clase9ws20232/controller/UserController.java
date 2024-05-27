package com.example.clase9ws20232.controller;

import com.example.clase9ws20232.entity.User;
import com.example.clase9ws20232.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // LISTAR
    @GetMapping(value = {"/list", ""})
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    // OBTENER
    @GetMapping(value = "/{id}")
    public ResponseEntity<HashMap<String, Object>> getUser(@PathVariable("id") String idStr) {
        HashMap<String, Object> responseJson = new HashMap<>();
        try {
            Optional<User> optUser = userRepository.findById(Integer.parseInt(idStr));
            if (optUser.isPresent()) {
                responseJson.put("result", "success");
                responseJson.put("user", optUser.get());
                return ResponseEntity.ok(responseJson);
            } else {
                responseJson.put("msg", "Usuario no encontrado");
            }
        } catch (NumberFormatException e) {
            responseJson.put("msg", "El ID debe ser un número entero positivo");
        }

        responseJson.put("result", "failure");
        return ResponseEntity.badRequest().body(responseJson);
    }

    // CREAR
    @PostMapping(value = {"", "/"})
    public ResponseEntity<HashMap<String, Object>> createUser(@RequestBody User user) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            if (user == null || user.getUsername() == null || user.getPassword() == null) {
                throw new IllegalArgumentException("El cuerpo de la solicitud es inválido o incompleto");
            }
            userRepository.save(user);
            response.put("result", "success");
            response.put("id", user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("result", "failure");
            response.put("msg", "Error al crear el usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("result", "failure");
            response.put("msg", "Error interno al crear el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    // ACTUALIZAR
    @PutMapping(value = {"", "/"})
    public ResponseEntity<HashMap<String, Object>> updateUser(@RequestBody User user) {
        HashMap<String, Object> response = new HashMap<>();
        if (user.getId() != null && user.getId() > 0) {
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (existingUser.isPresent()) {
                userRepository.save(user);
                response.put("result", "success");
                return ResponseEntity.ok(response);
            } else {
                response.put("result", "failure");
                response.put("msg", "User not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            response.put("result", "failure");
            response.put("msg", "Invalid ID");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ELIMINAR
    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<HashMap<String, Object>> deleteUser(@PathVariable("id") Integer id) {
        HashMap<String, Object> response = new HashMap<>();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            response.put("result", "success");
            return ResponseEntity.ok(response);
        } else {
            response.put("result", "failure");
            response.put("msg", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, String>> handleException(HttpServletRequest request, HttpMessageNotReadableException ex) {
        HashMap<String, String> response = new HashMap<>();
        response.put("result", "failure");
        response.put("msg", "Invalid input");
        return ResponseEntity.badRequest().body(response);
    }
}
