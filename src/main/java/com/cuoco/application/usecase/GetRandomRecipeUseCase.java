package com.cuoco.application.usecase;

import com.cuoco.application.port.out.GetAllRecipesByIdsRepository;
import com.cuoco.application.port.out.GetRecipeByIdRepository;
import com.cuoco.application.port.out.GetRecipeTableSizeRepository;
import com.cuoco.application.usecase.model.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.cuoco.application.port.in.GetRandomRecipeCommand;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class GetRandomRecipeUseCase implements GetRandomRecipeCommand {

    private GetAllRecipesByIdsRepository getAllRecipesByIdsRepository;
    private GetRecipeTableSizeRepository getRecipeTableSizeRepository;

    public GetRandomRecipeUseCase(GetAllRecipesByIdsRepository getAllRecipesByIdsRepository,
                                  GetRecipeTableSizeRepository getRecipeTableSizeRepository) {
        this.getAllRecipesByIdsRepository = getAllRecipesByIdsRepository;
        this.getRecipeTableSizeRepository = getRecipeTableSizeRepository;
    }

    @Override
    public List<Recipe> execute() {
        Long recipeTableSize = getRecipeTableSizeRepository.execute();
        int recipesAmount = 3;
        Set<Long> recipeIdSet = new HashSet<>();

        if (recipeTableSize >= recipesAmount) {
            while (recipeIdSet.size() < recipesAmount) {
                Long randomRecipeId = (long) (Math.random() * recipeTableSize);
                log.info("Executing get random recipe use case with ID: {}", randomRecipeId);
                recipeIdSet.add(randomRecipeId);
            }
        }
        List<Long> recipeIds = new ArrayList<>(recipeIdSet);
        List<Recipe> recipes = getAllRecipesByIdsRepository.execute(recipeIds);
        return recipes;
    }
}