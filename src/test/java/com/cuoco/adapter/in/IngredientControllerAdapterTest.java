package com.cuoco.adapter.in;

import com.cuoco.adapter.in.controller.IngredientControllerAdapter;
import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetIngredientsFromAudioCommand;
import com.cuoco.application.port.in.GetIngredientsFromImagesGroupedCommand;
import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.usecase.model.Ingredient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = IngredientControllerAdapter.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class IngredientControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetIngredientsFromAudioCommand getIngredientsFromAudioCommand;

    @MockitoBean
    private GetIngredientsFromImagesGroupedCommand getIngredientsFromImagesGroupedCommand;

    @MockitoBean
    private GetIngredientsFromTextCommand getIngredientsFromTextCommand;

    @MockitoBean
    private AuthenticateUserCommand authenticateUserCommand;

    @Test
    void GIVEN_audio_file_WHEN_postAudio_THEN_return_ingredient_response() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Tomate")
                .quantity(2.0)
                .unit("pcs")
                .confirmed(true)
                .source("audio")
                .build();

        when(getIngredientsFromAudioCommand.execute(any())).thenReturn(List.of(ingredient));

        MockMultipartFile audioFile = new MockMultipartFile(
                "audio",
                "audio.wav",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "dummy audio content".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/ingredients/audio")
                        .file(audioFile)
                        .param("language", "es-ES")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Tomate"))
                .andExpect(jsonPath("$[0].quantity").value(2.0))
                .andExpect(jsonPath("$[0].unit").value("pcs"))
                .andExpect(jsonPath("$[0].confirmed").value(true))
                .andExpect(jsonPath("$[0].source").value("audio"));
    }

    @Test
    void GIVEN_image_files_WHEN_postImage_THEN_return_grouped_ingredients() throws Exception {
        Ingredient ingredient1 = Ingredient.builder().name("Sal").quantity(1.0).unit("tsp").confirmed(true).source("image").build();
        Ingredient ingredient2 = Ingredient.builder().name("Pimienta").quantity(0.5).unit("tsp").confirmed(false).source("image").build();

        when(getIngredientsFromImagesGroupedCommand.execute(any())).thenReturn(
                Map.of("image1.jpg", List.of(ingredient1), "image2.jpg", List.of(ingredient2))
        );

        MockMultipartFile image1 = new MockMultipartFile(
                "image",
                "image1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content 1".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile image2 = new MockMultipartFile(
                "image",
                "image2.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content 2".getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/ingredients/image")
                        .file(image1)
                        .file(image2)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].filename").value("image1.jpg"))
                .andExpect(jsonPath("$[0].ingredients[0].name").value("Sal"))
                .andExpect(jsonPath("$[1].filename").value("image2.jpg"))
                .andExpect(jsonPath("$[1].ingredients[0].name").value("Pimienta"));
    }

    @Test
    void GIVEN_text_request_WHEN_postText_THEN_return_ingredient_response() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Cebolla")
                .quantity(1.0)
                .unit("pc")
                .confirmed(true)
                .source("text")
                .build();

        when(getIngredientsFromTextCommand.execute(any())).thenReturn(List.of(ingredient));

        String jsonRequest = """
                {
                    "text": "Some ingredient text",
                    "source": "recipe"
                }
                """;

        mockMvc.perform(post("/ingredients/text")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Cebolla"))
                .andExpect(jsonPath("$[0].quantity").value(1.0))
                .andExpect(jsonPath("$[0].unit").value("pc"))
                .andExpect(jsonPath("$[0].confirmed").value(true))
                .andExpect(jsonPath("$[0].source").value("text"));
    }
}
