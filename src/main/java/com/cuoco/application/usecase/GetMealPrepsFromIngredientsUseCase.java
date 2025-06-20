package com.cuoco.application.usecase;

import com.cuoco.application.port.in.GetMealPrepFromIngredientsCommand;
import com.cuoco.application.port.out.GetMealPrepsFromIngredientsRepository;
import com.cuoco.application.usecase.model.MealPrep;
import com.cuoco.application.usecase.model.RecipeFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class GetMealPrepsFromIngredientsUseCase implements GetMealPrepFromIngredientsCommand {

    private final GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider;

    public GetMealPrepsFromIngredientsUseCase(@Qualifier("provider") GetMealPrepsFromIngredientsRepository getMealPrepsFromIngredientsProvider) {
        this.getMealPrepsFromIngredientsProvider = getMealPrepsFromIngredientsProvider;
    }

    public List<MealPrep> execute(Command command) {
        log.info("Executing get recipes from ingredients and filters use case with command {}", command);

        MealPrep mealPrepToGenerate = builMealPrep(command);

        List<MealPrep> foundedMealPreps = getMealPrepsFromIngredientsProvider.execute(mealPrepToGenerate);

        return foundedMealPreps.stream().limit(1).toList();
    }

    private MealPrep builMealPrep(Command command) {
        return MealPrep.builder()
                .ingredients(command.getIngredients())
                .filters(buildFilters(command.getFilters()))
                .build();
    }

    private RecipeFilter buildFilters(RecipeFilter filter) {
        return RecipeFilter.builder()
                .time(filter.getTime() != null ? filter.getTime() : null)
                .difficulty(filter.getDifficulty() != null ? filter.getDifficulty() : null)
                .types(filter.getTypes() != null ? filter.getTypes() : Collections.emptyList())
                .diet(filter.getDiet() != null ? filter.getDiet() : null)
                .quantity(filter.getQuantity() != null ? filter.getQuantity() : null)
                .build();
    }
}
