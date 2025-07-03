CREATE TABLE `meal_preps`
(
    `id`                     bigint NOT NULL AUTO_INCREMENT,
    `title`                  varchar(255) DEFAULT NULL,
    `estimated_cooking_time` varchar(255) DEFAULT NULL,
    `freeze`                 bit(1)       DEFAULT NULL,
    `servings`               int          DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `meal_prep_steps`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `title`        varchar(255) DEFAULT NULL,
    `number`       int          DEFAULT NULL,
    `description`  text,
    `time`         varchar(255) DEFAULT NULL,
    `image_name`   varchar(255) DEFAULT NULL,
    `meal_prep_id` bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_meal_prep_id` (`meal_prep_id`),
    CONSTRAINT `FK_meal_prep_steps_meal_prep_id` FOREIGN KEY (`meal_prep_id`) REFERENCES `meal_preps` (`id`)
);

CREATE TABLE `meal_prep_ingredients`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `meal_prep_id`  bigint DEFAULT NULL,
    `ingredient_id` bigint DEFAULT NULL,
    `quantity`      double DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_ingredient_id` (`ingredient_id`),
    KEY `FK_meal_prep_id` (`meal_prep_id`),
    CONSTRAINT `FK_meal_prep_ingredients_meal_prep_id` FOREIGN KEY (`meal_prep_id`) REFERENCES `meal_preps` (`id`),
    CONSTRAINT `FK_meal_prep_ingredients_ingredient_id` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`)
);

CREATE TABLE `meal_prep_recipes`
(
    `meal_prep_id` bigint NOT NULL,
    `recipe_id`    bigint NOT NULL,
    KEY `FK_meal_prep_recipes_recipe_id` (`recipe_id`),
    KEY `FK_meal_prep_recipe_meal_prep_id` (`meal_prep_id`),
    CONSTRAINT `FK_meal_prep_recipes_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`),
    CONSTRAINT `FK_meal_prep_recipe_meal_prep_id` FOREIGN KEY (`meal_prep_id`) REFERENCES `meal_preps` (`id`)
);

CREATE TABLE `user_meal_preps`
(
    `user_id`      bigint NOT NULL,
    `meal_prep_id` bigint NOT NULL,
    KEY `FK_user_meal_prep_meal_prep_id` (`meal_prep_id`),
    KEY `FK_user_meal_prep_user_id` (`user_id`),
    CONSTRAINT `FK_user_meal_prep_user_id` FOREIGN KEY (`meal_prep_id`) REFERENCES `meal_preps` (`id`),
    CONSTRAINT `FK_user_meal_prep_meal_prep_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);