CREATE TABLE `category`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `unit`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(100) DEFAULT NULL,
    `symbol`      varchar(10)  NOT NULL,
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

CREATE TABLE `recipe`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `name`             varchar(255) DEFAULT NULL,
    `subtitle`         varchar(255) DEFAULT NULL,
    `description`      varchar(255) DEFAULT NULL,
    `preparation_time` varchar(255) DEFAULT NULL,
    `image_url`        varchar(255) DEFAULT NULL,
    `instructions`     text,
    `cook_level_id`    int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_recipe_cook_level_id` FOREIGN KEY (`cook_level_id`) REFERENCES `cook_level` (`id`)
);

CREATE TABLE `recipe_ingredients`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `recipe_id`     bigint DEFAULT NULL,
    `ingredient_id` bigint DEFAULT NULL,
    `quantity`      double DEFAULT NULL,
    `optional`      bit(1) DEFAULT NULL,
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

INSERT INTO category(id, description)
VALUES (1, 'Verduras'),
       (2, 'Frutas'),
       (3, 'Carnes'),
       (4, 'Pescados y mariscos'),
       (5, 'Lácteos'),
       (6, 'Huevos'),
       (7, 'Cereales y granos'),
       (8, 'Legumbres'),
       (9, 'Aceites y grasas'),
       (10, 'Especias y condimentos'),
       (11, 'Hierbas frescas'),
       (12, 'Bebidas'),
       (13, 'Panadería y pastelería'),
       (14, 'Frutos secos y semillas'),
       (15, 'Congelados'),
       (16, 'Alimentos en conserva'),
       (17, 'Salsas y aderezos'),
       (18, 'Snacks y golosinas'),
       (19, 'Productos veganos'),
       (20, 'Otros');

INSERT INTO unit (id, description, symbol)
VALUES (1, 'Mililitro', 'ml'),
       (2, 'Gramo', 'gr'),
       (3, 'Kilogramo', 'kg'),
       (4, 'Litro', 'l'),
       (5, 'Cucharada', 'cda'),
       (6, 'Cucharadita', 'cdta'),
       (7, 'Unidad', 'ud'),
       (8, 'Taza', 'tz'),
       (9, 'Pizca', 'pizca'),
       (10, 'Diente', 'diente'),
       (11, 'Lata', 'lata'),
       (12, 'Botella', 'botella'),
       (13, 'Sobre', 'sobre'),
       (14, 'Rodaja', 'rodaja'),
       (15, 'Rebanada', 'rebanada'),
       (16, 'Puñado', 'puñado'),
       (17, 'Onza', 'oz'),
       (18, 'Libra', 'lb'),
       (19, 'Miligramo', 'mg'),
       (20, 'Centilitro', 'cl'),
       (21, 'Copa', 'copa'),
       (22, 'Cucharón', 'cucharon');
