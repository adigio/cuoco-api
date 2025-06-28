package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GetRecipesIdsByIngredientsHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query(value = """
        SELECT r.id
        FROM recipes r
        JOIN recipe_ingredients ri ON ri.recipe_id = r.id
        JOIN ingredients i ON i.id = ri.ingredient_id
        WHERE LOWER(i.name) IN :ingredientNames
        GROUP BY r.id
        HAVING COUNT(DISTINCT LOWER(i.name)) = :ingredientCount
    """, nativeQuery = true)
    List<Long> execute(
            @Param("ingredientNames") List<String> ingredientNames,
            @Param("ingredientCount") Integer ingredientCount
    );
}
