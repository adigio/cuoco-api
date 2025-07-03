SELECT r.id
FROM recipes r
    JOIN recipe_ingredients ri ON ri.recipe_id = r.id
    JOIN ingredients i ON i.id = ri.ingredient_id
    LEFT JOIN recipe_meal_types rmt ON rmt.recipe_id = r.id
WHERE LOWER(i.name) IN (:INGREDIENT_NAMES)
  AND (:NOT_INCLUDE_IDS_IS_EMPTY OR r.id NOT IN (:NOT_INCLUDE_IDS))
  AND (:PREPARATION_TIME_ID IS NULL OR r.preparation_time_id = :PREPARATION_TIME_ID)
  AND (:COOK_LEVEL_ID IS NULL OR r.cook_level_id = :COOK_LEVEL_ID)
  AND (:DIET_ID IS NULL OR r.diet_id = :DIET_ID)
  AND (:MEAL_TYPES_IDS_IS_EMPTY OR rmt.meal_type_id IN (:MEAL_TYPES_IDS))
  AND (:DIETARY_NEEDS_IDS_IS_EMPTY OR (
            SELECT COUNT(DISTINCT rdn.dietary_need_id)
            FROM recipe_dietary_needs rdn
            WHERE rdn.recipe_id = r.id
            AND rdn.dietary_need_id IN (:DIETARY_NEEDS_IDS)
        ) = :DIETARY_NEEDS_COUNT)
  AND (:ALLERGY_IDS_IS_EMPTY OR NOT EXISTS (
            SELECT 1 FROM recipe_allergies ra
            WHERE ra.recipe_id = r.id
            AND ra.allergy_id IN (:ALLERGY_IDS))
      )
GROUP BY r.id
HAVING COUNT(DISTINCT LOWER(i.name)) = :INGREDIENT_COUNT
LIMIT :RESULT_SIZE;
