package com.cuoco.service;

import com.cuoco.model.Receta;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GeminiService {
    List<String> detectarIngredientesDesdeUnaImagen(MultipartFile archivo) throws IOException, InterruptedException;
    
    List<String> detectarYGuardarIngredientes(MultipartFile archivo) throws IOException, InterruptedException;
    
    String generarRecetaDesdeIngredientes(List<String> ingredientes) throws IOException, InterruptedException;
    
    Receta generarYGuardarReceta(List<String> ingredientes) throws IOException, InterruptedException;
    
    void guardarIngredientesDetectados(List<String> ingredientes);
}
