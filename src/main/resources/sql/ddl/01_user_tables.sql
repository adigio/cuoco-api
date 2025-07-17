CREATE TABLE allergies
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE cook_levels
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE diets
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE plans
(
    `id`                int NOT NULL AUTO_INCREMENT,
    `description`       varchar(255) DEFAULT NULL,
    `configuration_id`  int DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_plan_configuration_id` FOREIGN KEY (`configuration_id`) REFERENCES `plans` (`id`)
);

CREATE TABLE plan_configuration
(
    `id`            int NOT NULL AUTO_INCREMENT,
    `title`         varchar(255) DEFAULT NULL,
    `description`   varchar(255) DEFAULT NULL,
    `quantity`      int DEFAULT NULL,
    `price`         DECIMAL(10,2) DEFAULT NULL,
    `currency`      varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE payment_status
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE dietary_needs
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE users
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
    CONSTRAINT `FK_user_plan_id` FOREIGN KEY (`plan_id`) REFERENCES `plans` (`id`)
);

CREATE TABLE user_allergies
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `user_id`    bigint DEFAULT NULL,
    `allergy_id` int    DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_allergies_allergy_id` FOREIGN KEY (`allergy_id`) REFERENCES `allergies` (`id`),
    CONSTRAINT `FK_user_allergies_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE user_dietary_needs
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `user_id`         bigint DEFAULT NULL,
    `dietary_need_id` int    DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dietary_needs_dietary_need_id` FOREIGN KEY (`dietary_need_id`) REFERENCES `dietary_needs` (`id`),
    CONSTRAINT `FK_user_dietary_needs_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE user_preferences
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `user_id`       bigint NOT NULL,
    `diet_id`       int DEFAULT NULL,
    `cook_level_id` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_preferences_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_preferences_diet_id` FOREIGN KEY (`diet_id`) REFERENCES `diets` (`id`),
    CONSTRAINT `FK_user_preferences_cook_level_id` FOREIGN KEY (`cook_level_id`) REFERENCES `cook_levels` (`id`)
);

CREATE TABLE user_payments
(
    `id`                    bigint NOT NULL AUTO_INCREMENT,
    `user_id`               bigint NOT NULL,
    `to_plan_id`            int DEFAULT NULL,
    `status_id`             int DEFAULT NULL,
    `external_id`           varchar(255) DEFAULT NULL,
    `external_reference`    varchar(255) DEFAULT NULL,
    `checkout_url`          varchar(255) DEFAULT NULL,
    `created_at`            datetime(6)  DEFAULT NULL,
    `updated_at`            datetime(6)  DEFAULT NULL,
    `deleted_at`            datetime(6)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_payments_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_payments_plan_id` FOREIGN KEY (`to_plan_id`) REFERENCES `plans` (`id`),
    CONSTRAINT `FK_user_payments_status_id` FOREIGN KEY (`status_id`) REFERENCES `payment_status` (`id`)
);
