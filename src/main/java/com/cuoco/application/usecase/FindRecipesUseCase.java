package com.cuoco.application.usecase;

import com.cuoco.application.port.in.FindRecipesCommand;
import com.cuoco.application.port.out.FindRecipesByFiltersRepository;
import com.cuoco.application.usecase.model.Recipe;
import com.cuoco.application.usecase.model.SearchFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FindRecipesUseCase implements FindRecipesCommand {

    private static Integer SEARCH_DEFAULT_SIZE = 5;

    private FindRecipesByFiltersRepository findRecipesByFiltersRepository;

    public FindRecipesUseCase(FindRecipesByFiltersRepository findRecipesByFiltersRepository) {
        this.findRecipesByFiltersRepository = findRecipesByFiltersRepository;
    }

    @Override
    public List<Recipe> execute(Command command) {
        log.info("Executing find recipes by filters use case with command {}", command);

        List<Recipe> recipes = findRecipesByFiltersRepository.execute(buildSearchFilters(command));

        return recipes;
    }

    private SearchFilters buildSearchFilters(Command command) {
        return SearchFilters.builder()
                .size(command.getSize() != null ? command.getSize() : SEARCH_DEFAULT_SIZE)
                .random(command.getRandom() != null ? command.getRandom() : false)
                .build();
    }
}
