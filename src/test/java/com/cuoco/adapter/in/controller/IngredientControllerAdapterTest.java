package com.cuoco.adapter.in.controller;

import com.cuoco.application.port.in.AuthenticateUserCommand;
import com.cuoco.application.port.in.GetIngredientsFromAudioCommand;
import com.cuoco.application.port.in.GetIngredientsFromTextCommand;
import com.cuoco.application.port.in.GetIngredientsGroupedFromImagesCommand;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.factory.domain.IngredientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class IngredientControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private GetIngredientsFromAudioCommand getIngredientsFromAudioCommand;

    @Mock
    private GetIngredientsGroupedFromImagesCommand getIngredientsGroupedFromImagesCommand;

    @Mock
    private GetIngredientsFromTextCommand getIngredientsFromTextCommand;

    @Mock
    private AuthenticateUserCommand authenticateUserCommand;

    @InjectMocks
    private IngredientControllerAdapter ingredientControllerAdapter;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientControllerAdapter).build();
    }

    @Test
    void GIVEN_audio_file_WHEN_postAudio_THEN_return_ingredient_response() throws Exception {
        Ingredient ingredient = IngredientFactory.create();

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
                .andExpect(jsonPath("$[0].name").value(ingredient.getName()))
                .andExpect(jsonPath("$[0].quantity").value(ingredient.getQuantity()))
                .andExpect(jsonPath("$[0].unit.symbol").value(ingredient.getUnit().getSymbol()))
                .andExpect(jsonPath("$[0].confirmed").value(ingredient.getConfirmed()))
                .andExpect(jsonPath("$[0].source").value(ingredient.getSource()));
    }

    @Test
    void GIVEN_image_files_WHEN_postImage_THEN_return_grouped_ingredients() throws Exception {
        String filenameA = "image1.jpg";
        String filenameB = "image2.jpg";

        Ingredient ingredientA = IngredientFactory.create("Sal", 1.0, "gr");
        Ingredient ingredientB = IngredientFactory.create("Pimienta", 1.0, "ud");

        MockMultipartFile imageA = new MockMultipartFile(
                "image",
                filenameA,
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content 1".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile imageB = new MockMultipartFile(
                "image",
                filenameB,
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content 2".getBytes(StandardCharsets.UTF_8)
        );

        Map<String, List<Ingredient>> ingredientsByImage = new LinkedHashMap<>();
        ingredientsByImage.put(filenameA, List.of(ingredientA));
        ingredientsByImage.put(filenameB, List.of(ingredientB));

        when(getIngredientsGroupedFromImagesCommand.execute(any())).thenReturn(ingredientsByImage);

        mockMvc.perform(multipart("/ingredients/image")
                        .file(imageA)
                        .file(imageB)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].filename").value(filenameA))
                .andExpect(jsonPath("$[0].ingredients[0].name").value(ingredientA.getName()))
                .andExpect(jsonPath("$[1].filename").value(filenameB))
                .andExpect(jsonPath("$[1].ingredients[0].name").value(ingredientB.getName()));
    }

    @Test
    void GIVEN_text_request_WHEN_postText_THEN_return_ingredient_response() throws Exception {
        Ingredient ingredient = IngredientFactory.create();

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
                .andExpect(jsonPath("$[0].name").value(ingredient.getName()))
                .andExpect(jsonPath("$[0].quantity").value(ingredient.getQuantity()))
                .andExpect(jsonPath("$[0].unit.symbol").value(ingredient.getUnit().getSymbol()))
                .andExpect(jsonPath("$[0].confirmed").value(ingredient.getConfirmed()))
                .andExpect(jsonPath("$[0].source").value(ingredient.getSource()));
    }
}
