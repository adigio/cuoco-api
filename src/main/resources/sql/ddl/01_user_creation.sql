CREATE TABLE allergy
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE cook_level
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE diet
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE plan
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE dietary_need
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE user
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `name`       varchar(255) DEFAULT NULL,
    `email`      varchar(255) DEFAULT NULL,
    `password`   varchar(255) DEFAULT NULL,
    `active`     bit(1)       DEFAULT NULL,
    `plan_id`    int          DEFAULT NULL,
    `created_at` datetime(6)  DEFAULT NULL,
    `updated_at` datetime(6)  DEFAULT NULL,
    `deleted_at` datetime(6)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plan_id` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`)
);

CREATE TABLE user_preferences
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `user_id`       bigint NOT NULL,
    `diet_id`       int DEFAULT NULL,
    `cook_level_id` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_preference_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `FK_user_preference_diet_id` FOREIGN KEY (`diet_id`) REFERENCES `diet` (`id`),
    CONSTRAINT `FK_user_preference_cook_level_id` FOREIGN KEY (`cook_level_id`) REFERENCES `cook_level` (`id`)
);

CREATE TABLE user_allergies
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `user_id`    bigint DEFAULT NULL,
    `allergy_id` int    DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_allergies_allergy_id` FOREIGN KEY (`allergy_id`) REFERENCES `allergy` (`id`),
    CONSTRAINT `FK_user_allergies_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE user_dietary_needs
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `user_id`         bigint DEFAULT NULL,
    `dietary_need_id` int    DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dietary_needs_dietary_need_id` FOREIGN KEY (`dietary_need_id`) REFERENCES `dietary_need` (`id`),
    CONSTRAINT `FK_user_dietary_needs_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

INSERT INTO plan (id, description)
VALUES (1, 'Free'),
       (2, 'Pro');

INSERT INTO cook_level (id, description)
VALUES (1, 'Bajo'),
       (2, 'Medio'),
       (3, 'Alto');

INSERT INTO diet (id, description)
VALUES (1, 'Omnivoro'),
       (2, 'Vegetariano'),
       (3, 'Vegano'),
       (4, 'Otro');

INSERT INTO dietary_need (id, description)
VALUES (1, 'Sin gluten'),
       (2, 'Sin lactosa'),
       (3, 'Alta en proteinas'),
       (4, 'Ninguna en particular');

INSERT INTO allergy (id, description)
VALUES (1, 'Leche'),
       (2, 'Frutos secos'),
       (3, 'Soja'),
       (4, 'Crustáceos'),
       (5, 'Huevo'),
       (6, 'Pescados'),
       (7, 'Cereales'),
       (8, 'Maní'),
       (9, 'Otro'),
       (10, 'Ninguno en particular');