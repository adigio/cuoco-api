package com.cuoco.controller;

import com.cuoco.DTO.IngredientDTO;
import com.cuoco.service.impl.GeminiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UploadControllerTest {

    private UploadController uploadController;
    private GeminiServiceImpl geminiServiceMock;
    private MockMultipartFile archivoValido;
    private MockMultipartFile archivoVacio;
    private List<String> ingredientesMock;

    @BeforeEach
    public void setup() {
        geminiServiceMock = mock(GeminiServiceImpl.class);
        uploadController = new UploadController(geminiServiceMock);

        archivoValido = new MockMultipartFile("file", "test.jpg", "image/jpeg", "contenido".getBytes());
        archivoVacio = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);
        ingredientesMock = Arrays.asList("tomate", "cebolla", "ajo");
    }

    @Test
    public void test1_detectarIngredientePorMedioDeIngredienteDTO() throws Exception {
        when(geminiServiceMock.detectarIngredientesDesdeUnaImagen(archivoValido)).thenReturn(ingredientesMock);

        ResponseEntity<?> respuesta = uploadController.detectarIngredientes(archivoValido);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        List<IngredientDTO> expected = Arrays.asList(
                new IngredientDTO("tomate", "imagen", false),
                new IngredientDTO("cebolla", "imagen", false),
                new IngredientDTO("ajo", "imagen", false)
        );

        assertEquals(expected, respuesta.getBody());
    }

    @Test
    public void test2_detectarIngredientes_conArchivoVacio() {
        ResponseEntity<?> respuesta = uploadController.detectarIngredientes(archivoVacio);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().toString().contains("Error: imagen vacía"));
    }



}
/*    @Test
    public void test1_detectarIngredientes_conArchivoValido() throws Exception {
        when(geminiServiceMock.detectarYGuardarIngredientes(any())).thenReturn(ingredientesMock);

        ResponseEntity<?> respuesta = uploadController.detectarIngredientes(archivoValido);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(ingredientesMock, respuesta.getBody());
    }


    @Test
    public void test3_detectarIngredientes_conErrorEnServicio() throws Exception {
        when(geminiServiceMock.detectarYGuardarIngredientes(any()))
                .thenThrow(new RuntimeException("Error de prueba"));

        ResponseEntity<?> respuesta = uploadController.detectarIngredientes(archivoValido);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().toString().contains("Error al procesar la imagen"));
    }

    @Test
    public void test4_detectarYGenerarReceta_conArchivoValido() throws Exception {
        String recetaMock = "Receta de ensalada";
        when(geminiServiceMock.detectarYGuardarIngredientes(any())).thenReturn(ingredientesMock);
        when(geminiServiceMock.generarRecetaDesdeIngredientes(ingredientesMock)).thenReturn(recetaMock);

        ResponseEntity<?> respuesta = uploadController.detectarYGenerarReceta(archivoValido);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());

        @SuppressWarnings("unchecked")
        Map<String, Object> cuerpo = (Map<String, Object>) respuesta.getBody();
        assertEquals(ingredientesMock, cuerpo.get("ingredientes"));
        assertEquals(recetaMock, cuerpo.get("receta"));
    }

    @Test
    public void test5_generarReceta_conIngredientesValidos() throws Exception {
        List<String> ingredientes = Arrays.asList("huevo", "leche");
        String recetaEsperada = "Tortilla francesa";
        when(geminiServiceMock.generarRecetaDesdeIngredientes(ingredientes)).thenReturn(recetaEsperada);

        ResponseEntity<?> respuesta = uploadController.generarReceta(ingredientes);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(recetaEsperada, respuesta.getBody());
    }

    @Test
    public void test6_generarReceta_conIngredientesVacios() {
        List<String> listaVacia = Arrays.asList();

        ResponseEntity<?> respuesta = uploadController.generarReceta(listaVacia);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().toString().contains("no se proporcionaron ingredientes"));
    }
}

 */