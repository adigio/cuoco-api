package com.cuoco.controller;

import com.cuoco.service.impl.GeminiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ingredientes")
public class UploadController {

    @Autowired
    private GeminiServiceImpl geminiService;
    
    // Constructor para facilitar testing
    public UploadController() {
        // Constructor vacío para Spring
    }
    
    // Constructor para inyección manual en tests
    public UploadController(GeminiServiceImpl geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/detectar")
    public ResponseEntity<?> detectarIngredientes(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: imagen vacía o no enviada");
        }

        try {
            List<String> ingredientes = geminiService.detectarYGuardarIngredientes(file);
            return ResponseEntity.ok(ingredientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la imagen: " + e.getMessage());
        }
    }
    
    @PostMapping("/detectar-y-receta")
    public ResponseEntity<?> detectarYGenerarReceta(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: imagen vacía o no enviada");
        }

        try {
            List<String> ingredientes = geminiService.detectarYGuardarIngredientes(file);
            String receta = geminiService.generarRecetaDesdeIngredientes(ingredientes);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("ingredientes", ingredientes);
            respuesta.put("receta", receta);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la imagen y generar receta: " + e.getMessage());
        }
    }
    
    @PostMapping("/generar-receta")
    public ResponseEntity<?> generarReceta(@RequestBody List<String> ingredientes) {
        if (ingredientes == null || ingredientes.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: no se proporcionaron ingredientes");
        }

        try {
            String receta = geminiService.generarRecetaDesdeIngredientes(ingredientes);
            return ResponseEntity.ok(receta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al generar la receta: " + e.getMessage());
        }
    }
}
