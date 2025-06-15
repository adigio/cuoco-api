package com.cuoco.application.port.out;

import com.cuoco.application.usecase.model.Ingredient;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface GetIngredientsFromImageRepository {


    List<Ingredient> execute(List<MultipartFile> files);

    /**
     * Extract ingredients from uploaded files (images) maintaining separation by filename
     *
     * @param files The files to analyze for ingredients
     * @return Map with filename as key and list of ingredients as value
     */
    Map<String, List<Ingredient>> executeWithSeparation(List<MultipartFile> files);
}