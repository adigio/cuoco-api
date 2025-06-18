SELECT r.*
FROM recipe r
WHERE r.id IN (
    SELECT ri.recipe_id
    FROM recipe_ingredients ri
             JOIN ingredient i ON ri.ingredient_id = i.id
    WHERE LOWER(i.name) IN (:INGREDIENT_NAMES)
    GROUP BY ri.recipe_id
    HAVING COUNT(DISTINCT LOWER(i.name)) = :INGREDIENT_COUNT
)
  AND (:COOK_LEVEL_ID IS NULL OR r.cook_level_id = :COOK_LEVEL_ID)
  AND (:MAX_PREPARATION_TIME IS NULL OR r.preparation_time <= :MAX_PREPARATION_TIME)
LIMIT :MAX_RECIPES