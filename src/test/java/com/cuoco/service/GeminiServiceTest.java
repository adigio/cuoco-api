package com.cuoco.service;

import com.cuoco.CuocoApplication;
import com.cuoco.service.impl.GeminiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class GeminiServiceTest {

    @Autowired
    private GeminiServiceImpl geminiService;

    private void imprimirEncabezado(String nombreTest) {
        System.out.println("\n===========================================================");
        System.out.println("EJECUTANDO TEST: " + nombreTest);
        System.out.println("===========================================================");
    }

    private void imprimirResultado(String resultado) {
        System.out.println("\n→ RESULTADO ESPERADO: " + resultado);
        System.out.println("-----------------------------------------------------------");
    }

    private void imprimirDeteccionGemini(List<String> ingredientes) {
        System.out.println("\n🔍 GEMINI DETECTÓ EN ESTA IMAGEN: ");
        System.out.println("-----------------------------------------------------------");
        for (String ingrediente : ingredientes) {
            System.out.println("• " + ingrediente);
        }
        System.out.println("-----------------------------------------------------------");
    }

    @BeforeEach
    public void setup() {
        System.out.println("\n\n===== INICIANDO NUEVO TEST DE GEMINI SERVICE =====");
        assertNotNull(geminiService, "El servicio GeminiServiceImpl debería estar disponible");
    }

    @Test
    public void test1_dadaUnaImagenDeLimon_cuandoSeProcesa_detectaLimon() throws Exception {
        imprimirEncabezado("TEST 1 - DETECCIÓN DE LIMÓN");

        // Given
        var resource = new ClassPathResource("imagenes/Limon.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "Limon.jpg", "image/jpeg", resource.getInputStream());

        // When
        List<String> ingredientesDetectados = geminiService.detectarIngredientesDesdeUnaImagen(imagen);
        imprimirDeteccionGemini(ingredientesDetectados);

        // Then
        imprimirResultado("Debería detectar limón o cítrico");
        assertTrue(ingredientesDetectados.stream().anyMatch(i ->
                i.toLowerCase().contains("limón") ||
                        i.toLowerCase().contains("limon") ||
                        i.toLowerCase().contains("citrico")));
    }

    @Test
    public void test2_dadaUnaImagenDeComida_cuandoSeProcesa_detectaMultiplesIngredientes() throws Exception {
        imprimirEncabezado("TEST 2 - DETECCIÓN DE MÚLTIPLES INGREDIENTES EN COMIDA");

        // Given
        var resource = new ClassPathResource("imagenes/Comida.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "Comida.jpg", "image/jpeg", resource.getInputStream());

        // When
        List<String> ingredientes = geminiService.detectarIngredientesDesdeUnaImagen(imagen);
        imprimirDeteccionGemini(ingredientes);

        // Then
        imprimirResultado("Debería detectar al menos 3 ingredientes y uno de los esperados");
        assertTrue(ingredientes.size() >= 3, "Debería detectar al menos 3 ingredientes");

        String[] ingredientesEsperados = {"avocado", "palta", "brócoli", "brocoli",
                "pan", "cereal", "banana", "naranja"};

        for (String esperado : ingredientesEsperados) {
            if (ingredientes.stream().anyMatch(i -> i.toLowerCase().contains(esperado.toLowerCase()))) {
                System.out.println("✓ Detectó correctamente: " + esperado);
            }
        }

        assertTrue(ingredientes.stream().anyMatch(i ->
                        java.util.Arrays.stream(ingredientesEsperados)
                                .anyMatch(esperado -> i.toLowerCase().contains(esperado.toLowerCase()))),
                "Debería detectar al menos uno de los ingredientes esperados");
    }

    @Test
    public void test3_dadaUnaImagenDeHeladera_cuandoSeProcesa_detectaIngredientesRefrigerados() throws Exception {
        imprimirEncabezado("TEST 3 - DETECCIÓN DE ALIMENTOS EN HELADERA");

        // Given
        var resource = new ClassPathResource("imagenes/Heladera llena.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "Heladera llena.jpg", "image/jpeg", resource.getInputStream());

        // When
        List<String> ingredientes = geminiService.detectarIngredientesDesdeUnaImagen(imagen);
        imprimirDeteccionGemini(ingredientes);

        // Then
        System.out.println("\n🔎 EN ESTA HELADERA VEO: sandía, aceituna, zanahoria");
        System.out.println("-----------------------------------------------------------");

        imprimirResultado("Debería detectar ingredientes en la heladera");
        assertFalse(ingredientes.isEmpty(), "Debería detectar ingredientes en la heladera");

        String[] ingredientesEsperados = {"tomate", "zanahoria", "lechuga", "naranja", "agua", "leche",
                "jugo", "sandia"};

        imprimirResultado("Debería detectar al menos un ingrediente típico de heladera");
        assertTrue(ingredientes.stream().anyMatch(i ->
                        java.util.Arrays.stream(ingredientesEsperados)
                                .anyMatch(esperado -> i.toLowerCase().contains(esperado.toLowerCase()))),
                "Debería detectar al menos un ingrediente típico de heladera");
    }

    @Test
    public void test4_dadaUnaImagenDeHeladeraConTuppers_cuandoSeProcesa_detectaIngredientesAlmacenados() throws Exception {
        imprimirEncabezado("TEST 4 - DETECCIÓN DE ALIMENTOS GUARDADOS EN TUPPERS");

        // Given
        var resource = new ClassPathResource("imagenes/Heladera con tuppers y bolsas.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "Heladera con tuppers y bolsas.jpg", "image/jpeg", resource.getInputStream());

        // When
        List<String> ingredientes = geminiService.detectarIngredientesDesdeUnaImagen(imagen);
        imprimirDeteccionGemini(ingredientes);

        // Then
        System.out.println("\n🔎 EN ESTA HELADERA VEO: Coca-Cola, botellas, tuppers");
        System.out.println("-----------------------------------------------------------");

        imprimirResultado("Debería detectar al menos algunos elementos");
        assertFalse(ingredientes.isEmpty(), "Debería detectar al menos algunos elementos");

        imprimirResultado("Debería detectar contenedores o bebidas");
        assertTrue(ingredientes.stream().anyMatch(i ->
                        i.toLowerCase().contains("coca") ||
                                i.toLowerCase().contains("cola") ||
                                i.toLowerCase().contains("botella") ||
                                i.toLowerCase().contains("recipiente") ||
                                i.toLowerCase().contains("tupper") ||
                                i.toLowerCase().contains("bebida")),
                "Debería detectar Coca-Cola, botellas o recipientes");
    }
}
/*
    @Test
    public void test5_generarRecetaDesdeIngredientesDetectados() throws Exception {
        imprimirEncabezado("TEST 5 - GENERACIÓN DE RECETA DESDE INGREDIENTES DETECTADOS");

        // Given
        var resource = new ClassPathResource("imagenes/Comida.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "Comida.jpg", "image/jpeg", resource.getInputStream());

        // When - Primera parte: detectar ingredientes
        List<String> ingredientes = geminiService.detectarIngredientesDesdeUnaImagen(imagen);
        imprimirDeteccionGemini(ingredientes);

        // When - Segunda parte: generar receta
        String receta = geminiService.generarRecetaDesdeIngredientes(ingredientes);
        System.out.println("\n📝 RECETA GENERADA POR GEMINI:");
        System.out.println("-----------------------------------------------------------");
        System.out.println(receta);
        System.out.println("-----------------------------------------------------------");

        // Then
        imprimirResultado("Receta no debería estar vacía y debería contener algún ingrediente");
        assertFalse(receta.isEmpty(), "La receta no debe estar vacía");
        assertTrue(ingredientes.stream()
                        .anyMatch(ingrediente -> receta.toLowerCase().contains(ingrediente.toLowerCase())),
                "La receta debería mencionar al menos uno de los ingredientes detectados");
    }

    @Test
    public void test6_generarYGuardarRecetaDesdeIngredientesDetectados() throws Exception {
        imprimirEncabezado("TEST 6 - GUARDAR RECETA GENERADA EN BASE DE DATOS");

        // Given
        var resource = new ClassPathResource("imagenes/Heladera llena.jpg");
        MockMultipartFile imagen = new MockMultipartFile(
                "imagen", "Heladera llena.jpg", "image/jpeg", resource.getInputStream());

        // When - detectar, generar y guardar
        List<String> ingredientes = geminiService.detectarIngredientesDesdeUnaImagen(imagen);
        imprimirDeteccionGemini(ingredientes);

        Receta receta = geminiService.generarYGuardarReceta(ingredientes);

        // Then
        System.out.println("\n💾 RECETA GUARDADA EN BASE DE DATOS:");
        System.out.println("-----------------------------------------------------------");
        System.out.println("ID: " + receta.getId());
        System.out.println("Título: " + receta.getTitulo());
        System.out.println("Contenido:\n" + receta.getContenido());
        System.out.println("Ingredientes: " + receta.getIngredientes().toString());
        System.out.println("-----------------------------------------------------------");

        imprimirResultado("La receta debería guardarse correctamente con todos sus campos");
        assertNotNull(receta.getId(), "El ID de la receta no debe ser nulo");
        assertNotNull(receta.getTitulo(), "El título de la receta no debe ser nulo");
        assertFalse(receta.getTitulo().isEmpty(), "El título de la receta no debe estar vacío");
        assertFalse(receta.getContenido().isEmpty(), "El contenido de la receta no debe estar vacío");
        assertFalse(receta.getIngredientes().isEmpty(), "La lista de ingredientes no debe estar vacía");

        // Formato JSON para el frontend
        System.out.println("\n📊 FORMATO JSON PARA EL FRONTEND:");
        System.out.println("-----------------------------------------------------------");
        System.out.println("{\n" +
                "  \"id\": " + receta.getId() + ",\n" +
                "  \"titulo\": \"" + receta.getTitulo() + "\",\n" +
                "  \"contenido\": \"" + receta.getContenido().replace("\n", "\\n") + "\",\n" +
                "  \"ingredientes\": " + receta.getIngredientes().toString().replace("[", "[\"").replace("]", "\"]").replace(", ", "\", \"") + "\n" +
                "}");
        System.out.println("-----------------------------------------------------------");
    }
}

*/
