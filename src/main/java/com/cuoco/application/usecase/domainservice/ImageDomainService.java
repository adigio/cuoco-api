package com.cuoco.application.usecase.domainservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Component
public class ImageDomainService {

    private static final String BASE_IMAGES_PATH = "src/main/resources/imagenes";
    private static final String RECIPES_SUBDIR = "recetas";
    private static final String STEPS_SUBDIR = "pasos";

    public String buildMainImagePath(String sanitizedRecipeName) {
        return BASE_IMAGES_PATH + "/" + sanitizedRecipeName + "/" + RECIPES_SUBDIR;
    }

    public String buildStepImagePath(String sanitizedRecipeName) {
        return BASE_IMAGES_PATH + "/" + sanitizedRecipeName + "/" + STEPS_SUBDIR;
    }

    public String buildMainImageName(String sanitizedRecipeName) {
        return sanitizedRecipeName + "_main.jpg";
    }

    public String buildStepImageName(String sanitizedRecipeName, int stepNumber) {
        return sanitizedRecipeName + "_step_" + stepNumber + ".jpg";
    }

    public String buildMainImageUrl(String sanitizedRecipeName, String imageName) {
        return "/api/images/" + sanitizedRecipeName + "/recetas/" + imageName;
    }

    public String buildStepImageUrl(String sanitizedRecipeName, String imageName) {
        return "/api/images/" + sanitizedRecipeName + "/pasos/" + imageName;
    }

    public String sanitizeRecipeName(String recipeName) {
        if (recipeName == null || recipeName.trim().isEmpty()) {
            return "recipe";
        }
        return recipeName.replaceAll("[^a-zA-Z0-9\\s]", "")
                         .replaceAll("\\s+", "_")
                         .toLowerCase()
                         .trim();
    }

    public void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("Created directory: {}", directoryPath);
            } else {
                log.warn("Failed to create directory: {}", directoryPath);
            }
        }
    }

    public String saveImageToFile(String directoryPath, String imageName, byte[] imageData) throws IOException {
        createDirectoryIfNotExists(directoryPath);
        
        String fullPath = directoryPath + "/" + imageName;
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(imageData);
            log.info("Saved realistic image: {}", fullPath);
        }
        
        return fullPath;
    }
} 