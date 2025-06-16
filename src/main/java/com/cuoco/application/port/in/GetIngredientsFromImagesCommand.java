package com.cuoco.application.port.in;

import com.cuoco.application.usecase.model.Ingredient;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GetIngredientsFromImagesCommand {

    List<Ingredient> execute(Command command);

    @Data
    @Builder
    class Command {
        private final List<MultipartFile> images;
    }
}