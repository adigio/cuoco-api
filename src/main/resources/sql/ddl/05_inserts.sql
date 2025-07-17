INSERT INTO plans (id, description)
VALUES (1, 'Free'),
       (2, 'Pro');

INSERT INTO plan_configuration (id, title, description, quantity, price, currency)
VALUES (1, 'Cuoco Pro - Plan Premium', 'Actualiza a Pro: Recetas ilimitadas, filtros avanzados, meal preps y mucho más', 1, 500.00, 'ARS');

INSERT INTO payment_status (id, description)
VALUES (1, 'pending'),
       (2, 'approved'),
       (3, 'in_process'),
       (4, 'rejected'),
       (5, 'cancelled'),
       (6, 'refunded'),
       (7, 'charged_back');

INSERT INTO cook_levels (id, description)
VALUES (1, 'Bajo'),
       (2, 'Medio'),
       (3, 'Alto');

INSERT INTO diets (id, description)
VALUES (1, 'Omnivoro'),
       (2, 'Vegetariano'),
       (3, 'Vegano');

INSERT INTO dietary_needs (id, description)
VALUES (1, 'Sin gluten'),
       (2, 'Sin lactosa'),
       (3, 'Alta en proteinas');

INSERT INTO meal_types (id, description)
VALUES (1, 'Desayuno'),
       (2, 'Almuerzo'),
       (3, 'Merienda'),
       (4, 'Cena'),
       (5, 'Postre'),
       (6, 'Snack');

INSERT INTO preparation_times (id, description)
VALUES (1, '20 min'),
       (2, '40 min'),
       (3, '1 h'),
       (4, '1:30 h');

INSERT INTO allergies (id, description)
VALUES (1, 'Leche'),
       (2, 'Frutos secos'),
       (3, 'Soja'),
       (4, 'Crustáceos'),
       (5, 'Huevo'),
       (6, 'Pescados'),
       (7, 'Cereales'),
       (8, 'Maní');

INSERT INTO categories (id, description)
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

INSERT INTO units (id, description, symbol)
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