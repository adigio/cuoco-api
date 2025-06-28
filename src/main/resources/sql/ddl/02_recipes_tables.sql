CREATE TABLE `categories`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `meal_types`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `preparation_times`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `units`
(
    `id`          int         NOT NULL AUTO_INCREMENT,
    `description` varchar(100) DEFAULT NULL,
    `symbol`      varchar(10) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `ingredients`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(150) DEFAULT NULL,
    `category_id` int          DEFAULT NULL,
    `unit_id`     int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_ingredient_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
    CONSTRAINT `FK_ingredient_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`)
);

CREATE TABLE `recipes`
(
    `id`                  bigint NOT NULL AUTO_INCREMENT,
    `name`                varchar(255) DEFAULT NULL,
    `subtitle`            varchar(255) DEFAULT NULL,
    `description`         varchar(255) DEFAULT NULL,
    `image_url`           varchar(255) DEFAULT NULL,
    `instructions`        text,
    `cook_level_id`       int          DEFAULT NULL,
    `diet_id`             int          DEFAULT NULL,
    `preparation_time_id` int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_recipe_cook_level_id` (`cook_level_id`),
    KEY `FK_recipe_diet_id` (`diet_id`),
    KEY `FK_recipe_preparation_time_id` (`preparation_time_id`),
    CONSTRAINT `FK_recipe_cook_level_id` FOREIGN KEY (`cook_level_id`) REFERENCES `cook_levels` (`id`),
    CONSTRAINT `FK_recipe_diet_id` FOREIGN KEY (`diet_id`) REFERENCES `diets` (`id`),
    CONSTRAINT `FK_recipe_preparation_time_id` FOREIGN KEY (`preparation_time_id`) REFERENCES `preparation_times` (`id`)
);

CREATE TABLE `recipe_ingredients`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `recipe_id`     bigint DEFAULT NULL,
    `ingredient_id` bigint DEFAULT NULL,
    `quantity`      double DEFAULT NULL,
    `optional`      bit(1) DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_recipe_ingredients_ingredient_id` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`id`),
    CONSTRAINT `FK_recipe_ingredients_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);

CREATE TABLE `recipe_allergies`
(
    `allergy_id` int    NOT NULL,
    `recipe_id`  bigint NOT NULL,
    CONSTRAINT `FK_recipe_allergies_allergy_id` FOREIGN KEY (`allergy_id`) REFERENCES `allergies` (`id`),
    CONSTRAINT `FK_recipe_allergies_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);

CREATE TABLE `recipe_dietary_needs`
(
    `dietary_need_id` int    NOT NULL,
    `recipe_id`       bigint NOT NULL,
    CONSTRAINT `FK_recipe_dietary_needs_dietary_need_id` FOREIGN KEY (`dietary_need_id`) REFERENCES `dietary_needs` (`id`),
    CONSTRAINT `FK_recipe_dietary_needs_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);

CREATE TABLE `recipe_meal_types`
(
    `meal_type_id` int    NOT NULL,
    `recipe_id`    bigint NOT NULL,
    CONSTRAINT `FK_recipe_meal_types_meal_type_id` FOREIGN KEY (`meal_type_id`) REFERENCES `meal_types` (`id`),
    CONSTRAINT `FK_recipe_meal_types_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);

CREATE TABLE `recipe_steps`
(
    `id`                bigint NOT NULL AUTO_INCREMENT,
    `recipe_id`         bigint NOT NULL,
    `image_name`        varchar(100),
    `image_type`        varchar(10),
    `step_number`       int,
    `step_description`  text,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_recipe_steps__recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);

CREATE TABLE `user_recipes`
(
    `user_id`   bigint DEFAULT NULL,
    `recipe_id` bigint DEFAULT NULL,
    KEY `FK_user_recipes_user_id` (`user_id`),
    KEY `FK_user_recipes_recipe_id` (`recipe_id`),
    CONSTRAINT `FK_user_recipes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_recipes_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`)
);
