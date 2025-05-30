package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.GetIngredientsFromFileCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class UploadControllerAdapter {

    private GetIngredientsFromFileCommand getIngredientsFromFileCommand;

    public UploadControllerAdapter(GetIngredientsFromFileCommand getIngredientsFromFileCommand) {
        this.getIngredientsFromFileCommand = getIngredientsFromFileCommand;
    }

    @PostMapping("/")
    public ResponseEntity<?> getIngredients(@RequestBody List<MultipartFile> files) {
        try {
            List<Ingredient> ingredientsReponse = getIngredientsFromFileCommand.execute(buildIngredientsFromFileCommand(files));

            // TODO esto debe ser convertido a un DTO correspondiente con la capa de adapter IngredientsResponse
            return ResponseEntity.ok(ingredientsReponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    private GetIngredientsFromFileCommand.Command buildIngredientsFromFileCommand(List<MultipartFile> files) {
        return new GetIngredientsFromFileCommand.Command(files);
    }
}

/*
        if (files == null || files.size() == 0) {
            return ResponseEntity.badRequest().body("Error: imagen vacía o no enviada");
        }
        List<IngredientRequest> ingredientes = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                List<String> nombres = geminiService.detectarIngredientesDesdeUnaImagen(file);

                for (String nombre : nombres) {
                    ingredientes.add(new IngredientRequest(nombre, "imagen", false));
                }
            }

            return ResponseEntity.ok(ingredientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la imagen: " + e.getMessage());
        }
 */

/*    @PostMapping("/detectar-y-receta")
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
*/


