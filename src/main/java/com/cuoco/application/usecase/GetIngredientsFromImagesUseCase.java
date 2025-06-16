package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromImagesCommand;
import com.cuoco.application.port.out.GetIngredientsFromImageRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetIngredientsFromImagesUseCase implements GetIngredientsFromImagesCommand {

    private final GetIngredientsFromImageRepository getIngredientsFromImageRepository;
    private final FileDomainService fileDomainService;

    public GetIngredientsFromImagesUseCase(GetIngredientsFromImageRepository getIngredientsFromImageRepository, FileDomainService fileDomainService) {
        this.getIngredientsFromImageRepository = getIngredientsFromImageRepository;
        this.fileDomainService = fileDomainService;
    }

    @Override
    public List<Ingredient> execute(Command command) {
        log.info("Executing get ingredients from file use case with {} files", command.getImages().size());

        List<File> images = command.getImages().stream().map(image -> {

            String filename = fileDomainService.getFileName(image);
            String imageBase64 = fileDomainService.convertToBase64(image);
            String mimeType = fileDomainService.getMimeType(image);

            return File.builder()
                    .fileName(filename)
                    .mimeType(mimeType)
                    .fileBase64(imageBase64)
                    .build();

        }).toList();

        List<Ingredient> ingredients = getIngredientsFromImageRepository.execute(images);

        log.info("Successfully extracted {} ingredients from images", ingredients.size());

        return ingredients;
    }
}
