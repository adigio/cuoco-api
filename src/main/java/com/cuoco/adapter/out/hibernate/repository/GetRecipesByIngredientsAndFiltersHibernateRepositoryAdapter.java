package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query("""
        SELECT DISTINCT r FROM recipes r
        JOIN FETCH r.recipeIngredients ri
        JOIN FETCH ri.ingredient i
        WHERE LOWER(i.name) IN :ingredientNames
        AND (:cookLevelId IS NULL OR r.cookLevel.id = :cookLevelId)
        AND (:maxPreparationTime IS NULL OR r.preparationTime <= :maxPreparationTime)
    """)
    List<RecipeHibernateModel> execute(
            @Param("ingredientNames") List<String> ingredientNames,
            @Param("cookLevelId") Integer cookLevelId,
            @Param("maxPreparationTime") String maxPreparationTime
    );
}
