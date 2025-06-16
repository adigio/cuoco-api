package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsFromImagesGroupedCommand;
import com.cuoco.application.port.out.GetIngredientsFromImagesGroupedRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GetIngredientsFromImagesGroupedUseCase implements GetIngredientsFromImagesGroupedCommand {

    private final GetIngredientsFromImagesGroupedRepository getIngredientsFromImagesGroupedRepository;
    private final FileDomainService fileDomainService;

    public GetIngredientsFromImagesGroupedUseCase(GetIngredientsFromImagesGroupedRepository getIngredientsFromImagesGroupedRepository, FileDomainService fileDomainService) {
        this.getIngredientsFromImagesGroupedRepository = getIngredientsFromImagesGroupedRepository;
        this.fileDomainService = fileDomainService;
    }

    @Override
    public Map<String, List<Ingredient>> execute(GetIngredientsFromImagesGroupedCommand.Command command) {
        log.info("Executing get all ingredients grouped by image use case for {} files", command.getImages().size());

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

        Map<String, List<Ingredient>> ingredientsByImage = getIngredientsFromImagesGroupedRepository.execute(images);

        log.info("Successfully extracted ingredients grouped by images");

        return ingredientsByImage;
    }



}