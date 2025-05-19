package com.cuoco.service.impl;

import com.cuoco.infrastructure.repository.hibernate.model.IngredientHibernateModel;
import com.cuoco.infrastructure.repository.hibernate.IngredienteHibernateRepository;
import com.cuoco.infrastructure.repository.hibernate.RecetaHibernateRepository;
import com.cuoco.service.GeminiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service("gemini")
public class GeminiServiceImpl implements GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Autowired
    private RecetaHibernateRepository recetaRepository;

    @Autowired
    private IngredienteHibernateRepository ingredienteRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> detectarIngredientesDesdeUnaImagen(MultipartFile archivo) throws IOException, InterruptedException {
        String imagenBase64 = Base64.getEncoder().encodeToString(archivo.getBytes());
        String cuerpoSolicitud = construirCuerpoSolicitud(imagenBase64);
        HttpResponse<String> respuesta = enviarSolicitudAGemini(cuerpoSolicitud);
        String texto = extraerTextoDeRespuesta(respuesta.body());
        return procesarTextoALista(texto);
    }

    private String construirCuerpoSolicitud(String imagenBase64) {
        return """
                {
                  "contents": [{
                    "parts": [{
                      "inline_data": {
                        "mime_type": "image/jpeg",
                        "data": "%s"
                      }
                    }, {
                      "text": "Observá atentamente la imagen de comida y listá todos los ingredientes visibles que puedan reconocerse. Respondé solo con los nombres de los ingredientes principales en español, separados por coma. No des explicaciones ni contexto. Si no ves ingredientes claros, igual intentá adivinar ingredientes comunes en este tipo de comida."
                    }]
                  }],
                  "generation_config": {
                    "temperature": 0.4
                  }
                }
                """.formatted(imagenBase64);
    }

    private HttpResponse<String> enviarSolicitudAGemini(String cuerpo) throws IOException, InterruptedException {
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo))
                .build();

        HttpClient cliente = HttpClient.newHttpClient();
        return cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
    }

    private String extraerTextoDeRespuesta(String json) throws IOException {
        JsonNode root = objectMapper.readTree(json);

        JsonNode geminiCandidatos = root.path("candidates");
        if (!geminiCandidatos.isArray() || geminiCandidatos.isEmpty()) return "";

        JsonNode firstCandidate = geminiCandidatos.get(0);
        JsonNode content = firstCandidate.path("content");
        JsonNode parts = content.path("parts");
        if (!parts.isArray() || parts.isEmpty()) return "";

        JsonNode textNode = parts.get(0).path("text");
        return textNode.asText("").replace("\\n", "\n").toLowerCase();
    }

    private List<String> procesarTextoALista(String texto) {
        return Arrays.stream(texto.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }


    /*

    @Override
        public List<String> detectarYGuardarIngredientes(MultipartFile archivo) throws IOException, InterruptedException {
            List<String> ingredientes = detectarIngredientesDesdeUnaImagen(archivo);

            if (ingredientes.isEmpty()) {
                throw new IngredienteNoDetectadoException("No se detectaron ingredientes en la imagen");
            }

            guardarIngredientesDetectados(ingredientes);
            return ingredientes;
        }

        @Override
        public void guardarIngredientesDetectados(List<String> ingredientes) {
            for (String nombreIngrediente : ingredientes) {
                Optional<Ingrediente> ingredienteExistente = ingredienteRepository.findByNombre(nombreIngrediente);

                if (ingredienteExistente.isEmpty()) {
                    Ingrediente nuevoIngrediente = new Ingrediente();
                    nuevoIngrediente.setNombre(nombreIngrediente);
                    ingredienteRepository.save(nuevoIngrediente);
                }
            }
        }
*/

    @Override
    public String generarRecetaDesdeIngredientes(List<String> ingredientes) throws IOException, InterruptedException {
        // Convertir la lista de ingredientes a texto
        String ingredientesTexto = String.join(", ", ingredientes);

        // Construir el cuerpo de la solicitud
        String cuerpoSolicitud = """
            {
              "contents": [{
                "parts": [{
                  "text": "Con los siguientes ingredientes que te vamos a pasar de acá: %s. Generá unas 6 recetas en español, claras, paso a paso, ideal para cocinar en casa. Solo recetas que incluyan los ingredientes mencionados, de no haber ir agregandole ingredientes comunes de mesa. No des contexto, solo las recetas."
                }]
              }],
              "generation_config": {
                "temperature": 0.4
              }
            }
            """.formatted(ingredientesTexto);

        // Enviar la solicitud a Gemini
        HttpResponse<String> respuesta = enviarSolicitudAGemini(cuerpoSolicitud);

        // Extraer y devolver la receta generada
        return extraerTextoDeRespuesta(respuesta.body());
    }
    
/*    @Override
    public Receta generarYGuardarReceta(List<String> ingredientes) throws IOException, InterruptedException {
        String contenidoReceta = generarRecetaDesdeIngredientes(ingredientes);
        
        // Generar un título para la receta usando Gemini
        String titulo = generarTituloReceta(ingredientes);
        
        // Crear y guardar la receta
        Receta nuevaReceta = new Receta();
        nuevaReceta.setTitulo(titulo);
        nuevaReceta.setContenido(contenidoReceta);
        nuevaReceta.setIngredientes(convertirAEntityList(ingredientes));
        nuevaReceta.setFechaCreacion(new Date());
        
        return recetaRepository.save(nuevaReceta);
    }*/
    
/*    private String generarTituloReceta(List<String> ingredientes) throws IOException, InterruptedException {
        String ingredientesTexto = String.join(", ", ingredientes);
        
        String cuerpoSolicitud = """
            {
              "contents": [{
                "parts": [{
                  "text": "Genera un título corto y atractivo para una receta que usa estos ingredientes: %s. El título debe ser de máximo 6 palabras y no incluir la palabra 'receta'. Solo responde con el título, sin comillas ni explicaciones."
                }]
              }],
              "generation_config": {
                "temperature": 0.7
              }
            }
            """.formatted(ingredientesTexto);

        HttpResponse<String> respuesta = enviarSolicitudAGemini(cuerpoSolicitud);
        return extraerTextoDeRespuesta(respuesta.body()).trim();
    }*/

    private List<IngredientHibernateModel> convertirAEntityList(List<String> nombresIngredientes) {
        List<IngredientHibernateModel> ingredientes = new ArrayList<>();
        
        for (String nombre : nombresIngredientes) {
            // Buscar el ingrediente en la base de datos
            Optional<IngredientHibernateModel> ingredienteOpt = ingredienteRepository.findByNombre(nombre);
            
            // Si existe, añadirlo a la lista. Si no existe, crearlo
            if (ingredienteOpt.isPresent()) {
                ingredientes.add(ingredienteOpt.get());
            } else {
                IngredientHibernateModel nuevoIngrediente = new IngredientHibernateModel();
                nuevoIngrediente.setNombre(nombre);
                ingredientes.add(ingredienteRepository.save(nuevoIngrediente));
            }
        }
        
        return ingredientes;
    }
}
