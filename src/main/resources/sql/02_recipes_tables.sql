CREATE TABLE `category`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `difficulty`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `recipe`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `description`    varchar(255) DEFAULT NULL,
    `difficulty`     varchar(255) DEFAULT NULL,
    `estimated_time` int          DEFAULT NULL,
    `image_url`      varchar(255) DEFAULT NULL,
    `steps`          varchar(255) DEFAULT NULL,
    `title`          varchar(255) DEFAULT NULL,
    `difficulty_id`  int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_recipe_difficulty_id` FOREIGN KEY (`difficulty_id`) REFERENCES `difficulty` (`id`)
);

CREATE TABLE `unit`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(100) DEFAULT NULL,
    `symbol`      varchar(10)  DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `ingredient`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(150) DEFAULT NULL,
    `category_id` int          DEFAULT NULL,
    `unit_id`     int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_ingredient_category_id` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
    CONSTRAINT `FK_ingredient_unit_id` FOREIGN KEY (`unit_id`) REFERENCES `unit` (`id`)
);

CREATE TABLE `recipe_ingredients`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `quantity`      double DEFAULT NULL,
    `ingredient_id` bigint DEFAULT NULL,
    `recipe_id`     bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_recipe_ingredients_ingredient_id` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient` (`id`),
    CONSTRAINT `FK_recipe_ingredients_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`id`)
);

CREATE TABLE `user_recipes`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `favorite`  bit(1) DEFAULT NULL,
    `recipe_id` bigint DEFAULT NULL,
    `user_id`   bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_recipes_recipe_id` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`id`),
    CONSTRAINT `FK_user_recipes_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);





