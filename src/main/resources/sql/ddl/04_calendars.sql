CREATE TABLE `user_calendar_recipes`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `user_calendar_id` bigint DEFAULT NULL,
    `recipe_id`        bigint DEFAULT NULL,
    `meal_type_id`     int    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY uq_calendar_recipe_mealtype (user_calendar_id, recipe_id, meal_type_id),
    KEY `FK_user_calendar_recipes_calendar_id` (`user_calendar_id`),
    KEY `FK_user_calendar_meal_type_id` (`meal_type_id`),
    KEY `FK_user_calendar_recipe_id` (`recipe_id`),
    CONSTRAINT `FK_user_calendar_recipes_calendar_id` FOREIGN KEY (`user_calendar_id`) REFERENCES `user_calendars` (`id`),
    CONSTRAINT `FK_user_calendar_meal_type_id` FOREIGN KEY (`meal_type_id`) REFERENCES `meal_types` (`id`),
    CONSTRAINT `FK_user_calendar_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);

CREATE TABLE `user_calendars`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `planned_date` date   DEFAULT NULL,
    `user_id`      bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_user_calendars_user_id` (`user_id`),
    CONSTRAINT `FK_user_calendars_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);
