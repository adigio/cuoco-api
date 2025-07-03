package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetIngredientsGroupedFromImagesCommand;
import com.cuoco.application.port.out.GetAllUnitsRepository;
import com.cuoco.application.port.out.GetIngredientsGroupedFromImagesRepository;
import com.cuoco.application.usecase.domainservice.FileDomainService;
import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;
import com.cuoco.application.usecase.model.ParametricData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GetIngredientsGroupedFromImagesUseCase implements GetIngredientsGroupedFromImagesCommand {

    private final GetIngredientsGroupedFromImagesRepository getIngredientsGroupedFromImagesRepository;
    private final GetAllUnitsRepository getAllUnitsRepository;
    private final FileDomainService fileDomainService;

    public GetIngredientsGroupedFromImagesUseCase(
            GetIngredientsGroupedFromImagesRepository getIngredientsGroupedFromImagesRepository,
            GetAllUnitsRepository getAllUnitsRepository,
            FileDomainService fileDomainService
    ) {
        this.getIngredientsGroupedFromImagesRepository = getIngredientsGroupedFromImagesRepository;
        this.getAllUnitsRepository = getAllUnitsRepository;
        this.fileDomainService = fileDomainService;
    }

    @Override
    public Map<String, List<Ingredient>> execute(GetIngredientsGroupedFromImagesCommand.Command command) {
        log.info("Executing get all ingredients grouped by image use case for {} files", command.getImages().size());

        ParametricData parametricData = buildParametricData();

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

        Map<String, List<Ingredient>> ingredientsByImage = getIngredientsGroupedFromImagesRepository.execute(images, parametricData);

        log.info("Successfully extracted ingredients grouped by images");

        return ingredientsByImage;
    }

    private ParametricData buildParametricData() {
        return ParametricData.builder().units(getAllUnitsRepository.execute()).build();
    }
}