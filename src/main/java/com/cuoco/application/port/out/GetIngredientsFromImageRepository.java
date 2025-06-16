package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.File;
import com.cuoco.application.usecase.model.Ingredient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface GetIngredientsFromImageRepository {
    List<Ingredient> execute(List<File> files);
}