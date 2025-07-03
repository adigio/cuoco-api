package com.cuoco.adapter.out.rest.gemini.utils;

import autovalue.shaded.kotlin.Pair;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.CandidateGeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.ContentGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GeminiResponseModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.GenerationConfigurationGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.InlineDataGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PartGeminiRequestModel;
import com.cuoco.adapter.out.rest.gemini.model.wrapper.PromptBodyGeminiRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class ImageUtils {

    @Value("${shared.recipes.images.base-path}")
    private String BASE_PATH;

    public boolean imageExists(Long recipeId, String imageName) {
        Path recipeDir = Paths.get(BASE_PATH, recipeId.toString());

        if (!Files.exists(recipeDir) || !Files.isDirectory(recipeDir)) {
            return false;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(recipeDir, imageName + ".*")) {
            return stream.iterator().hasNext();
        } catch (IOException e) {
            log.error("Error checking for main image in {}: {}", recipeDir, e.getMessage(), e);
            return false;
        }
    }

    public void saveImageFile(byte[] imageData, Long recipeId, String fileName) {
        Path recipeDir = Paths.get(BASE_PATH, recipeId.toString());
        Path imagePath = recipeDir.resolve(fileName);

        try {
            Files.createDirectories(recipeDir);
            Files.write(imagePath, imageData);
            log.info("Image saved: {}", imagePath);
        } catch (IOException e) {
            log.error("Error saving created image for recipe {}: {}", recipeId, e.getMessage(), e);
        }
    }

    public Pair<String, byte[]> extractImageFromResponse(GeminiResponseModel response) {

        if(response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            CandidateGeminiResponseModel candidate = response.getCandidates().get(0);

            if(candidate.getContent() != null && candidate.getContent().getParts() != null) {

                for (PartGeminiRequestModel part : candidate.getContent().getParts()) {
                    InlineDataGeminiRequestModel inlineData = part.getInlineData();

                    if (inlineData != null) {
                        String base64Data = inlineData.getData();

                        if (base64Data != null && !base64Data.trim().isEmpty()) {
                            return new Pair<>(inlineData.getMimeType(), Base64.getDecoder().decode(base64Data));
                        }
                    }
                }
            }
        }

        return null;
    }

    public PromptBodyGeminiRequestModel buildPromptBody(String prompt) {
        return PromptBodyGeminiRequestModel.builder()
                .contents(List.of(ContentGeminiRequestModel.builder().parts(buildPartsRequest(prompt)).build()))
                .generationConfig(GenerationConfigurationGeminiRequestModel.builder().responseModalities(List.of("TEXT","IMAGE")).build())
                .build();
    }

    private List<PartGeminiRequestModel> buildPartsRequest(String prompt) {
        return List.of(PartGeminiRequestModel.builder().text(prompt).build());
    }
}
