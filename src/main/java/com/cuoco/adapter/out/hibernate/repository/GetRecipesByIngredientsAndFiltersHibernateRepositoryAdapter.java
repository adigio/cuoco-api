package com.cuoco.adapter.out.hibernate.repository;

import com.cuoco.adapter.out.hibernate.model.RecipeHibernateModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetRecipesByIngredientsAndFiltersHibernateRepositoryAdapter extends JpaRepository<RecipeHibernateModel, Long> {

    @Query("""
        SELECT DISTINCT r FROM recipe r
        LEFT JOIN r.mealTypes mt
        LEFT JOIN r.dietaryNeeds dn
        WHERE r.id IN :recipeIds
            AND (:preparationTimeId IS NULL OR r.preparationTime.id = :preparationTimeId)
            AND (:cookLevelId IS NULL OR r.cookLevel.id = :cookLevelId)
            AND (:mealTypesIds IS NULL OR mt.id IN :mealTypesIds)
            AND (:dietId IS NULL OR r.diet.id = :dietId)
            AND (:dietaryNeedIds IS NULL OR (
                SELECT COUNT(dn2) FROM recipe r2
                JOIN r2.dietaryNeeds dn2
                WHERE r2.id = r.id AND dn2.id IN :dietaryNeedIds
            ) = :#{#dietaryNeedIds == null ? 0 : #dietaryNeedIds.size()})
            AND (:allergyIds IS NULL OR NOT EXISTS (
                SELECT 1 FROM recipe r2
                JOIN r2.allergies a2
                WHERE r2.id = r.id AND a2.id IN :allergyIds
            ))
    """)
    List<RecipeHibernateModel> execute(
            @Param("recipeIds") List<Long> recipeIds,
            @Param("preparationTimeId") Integer preparationTimeId,
            @Param("cookLevelId") Integer cookLevelId,
            @Param("dietId") Integer dietId,
            @Param("mealTypesIds") List<Integer> mealTypesIds,
            @Param("allergyIds") List<Integer> allergyIds,
            @Param("dietaryNeedIds") List<Integer> dietaryNeedIds,
            Pageable pageable
    );
}
