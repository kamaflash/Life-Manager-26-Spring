-- ============================================
-- LIMPIAR DATOS EXISTENTES
-- ============================================
TRUNCATE TABLE product_instant_effects CASCADE;
TRUNCATE TABLE product_passive_effects CASCADE;
TRUNCATE TABLE character_inventory CASCADE;
TRUNCATE TABLE product_shop_offers CASCADE;
TRUNCATE TABLE product_recipes CASCADE;
TRUNCATE TABLE recipe_ingredients CASCADE;
TRUNCATE TABLE products CASCADE;

-- Reiniciar secuencias
ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE product_instant_effects_id_seq RESTART WITH 1;
ALTER SEQUENCE product_passive_effects_id_seq RESTART WITH 1;
ALTER SEQUENCE character_inventory_id_seq RESTART WITH 1;
ALTER SEQUENCE product_shop_offers_id_seq RESTART WITH 1;
ALTER SEQUENCE product_recipes_id_seq RESTART WITH 1;
ALTER SEQUENCE recipe_ingredients_id_seq RESTART WITH 1;

-- ============================================
-- 1. PRODUCTOS
-- ============================================
-- ============================================
-- 1. PRODUCTOS EXISTENTES (CON PA = 2)
-- ============================================
INSERT INTO products (id, name, description, img, icon, price, pa, discount_price, discount_start_date, discount_end_date, category, usage_type, active, rarity, required_level, slot, is_consumable, stackable, sellable, cooldown_hours, duration_hours, durability, season, is_event_item) VALUES
-- TRANSPORTE
(1, 'Bicicleta Urbana', 'Bicicleta ecológica para desplazamientos cortos. Reduce tiempo de viaje un 20%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180253/adlplo1cudastgisjwcj.png', 'directions_bike', 300.00, 2, NULL, NULL, NULL, 'TRANSPORTE', 'REUSABLE', true, 'COMMON', 1, 'VEHICLE', false, false, true, 0, 0, 1000, NULL, false),
(2, 'Abono Transporte', 'Billete mensual para metro, bus y tranvía. Reduce tiempo de viaje un 30%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180252/bs5mr64duuiuakh8doss.png', 'directions_bus', 50.00, 2, NULL, NULL, NULL, 'TRANSPORTE', 'SUBSCRIPTION', true, 'UNCOMMON', 1, 'VEHICLE', false, false, false, 0, 720, NULL, NULL, false),
(3, 'Scooter Eléctrico', 'Scooter motorizado para moverte rápido. Reduce tiempo de viaje un 50%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180251/iqy1fwahjfettqjmtp89.png', 'electric_scooter', 1500.00, 2, 1200.00, '2026-04-01', '2026-05-01', 'TRANSPORTE', 'REUSABLE', true, 'RARE', 5, 'VEHICLE', false, false, true, 0, 0, 5000, NULL, false),
(4, 'Coche Compacto', 'Vehículo personal con buena eficiencia. Reduce tiempo de viaje un 60%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180249/gdw3pagtiqr5q7z2qboh.png', 'directions_car', 12000.00, 2, 10000.00, '2026-04-01', '2026-04-30', 'TRANSPORTE', 'REUSABLE', true, 'EPIC', 10, 'VEHICLE', false, false, true, 0, 0, 10000, NULL, false),
(5, 'Coche de Lujo', 'Vehículo premium que mejora estatus y reduce viajes un 70%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180247/xyxdxngacqq4ryqb8tdd.png', 'sports_car', 50000.00, 2, 45000.00, '2026-04-01', '2026-05-15', 'TRANSPORTE', 'REUSABLE', true, 'LEGENDARY', 20, 'VEHICLE', false, false, true, 0, 0, 20000, NULL, false),

-- SALUD
(6, 'Suscripción Gimnasio', 'Entrenamiento regular para mejorar condición física.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180264/mmyx9fgqp5lwvp5dluux.png', 'fitness_center', 50.00, 2, NULL, NULL, NULL, 'SALUD', 'SUBSCRIPTION', true, 'COMMON', 1, 'TOOL', false, false, false, 24, 720, NULL, NULL, false),
(7, 'Clases de Yoga', 'Mejora flexibilidad, reduce estrés y aumenta bienestar.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180262/ov3d6ondk2mvdwgcm9bz.png', 'self_improvement', 45.00, 2, 35.00, '2026-04-01', '2026-04-30', 'SALUD', 'SUBSCRIPTION', true, 'UNCOMMON', 2, 'TOOL', false, false, false, 12, 720, NULL, 'SPRING', false),
(8, 'Kit de Primeros Auxilios', 'Botiquín básico para emergencias. Restaura 20 de salud.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180242/vki4pa0kxlnfuwjsxhfs.png', 'medical_services', 25.00, 2, NULL, NULL, NULL, 'SALUD', 'CONSUMABLE', true, 'COMMON', 1, 'TOOL', true, true, false, 0, 0, 1, NULL, false),
(9, 'Terapia de Masajes', 'Alivio muscular y reducción de estrés. Restaura 15 de energía.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180260/g8mb9vscskiu0kcdfckl.png', 'spa', 70.00, 2, NULL, NULL, NULL, 'SALUD', 'CONSUMABLE', true, 'RARE', 3, 'TOOL', true, true, false, 24, 0, 1, NULL, false),
(10, 'Spa Relax', 'Sesión completa de spa. Reduce estrés y mejora felicidad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180260/nl5yvxqdz2drc8amlc1i.png', 'hot_tub', 120.00, 2, 100.00, '2026-04-01', '2026-04-30', 'SALUD', 'CONSUMABLE', true, 'EPIC', 5, 'TOOL', true, true, false, 48, 0, 1, NULL, false),

-- ALIMENTACIÓN
(11, 'Cena Restaurante', 'Comida preparada para disfrutar fuera de casa.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180237/fchzqhkx0e8wlsnbgtsw.png', 'restaurant', 25.00, 2, NULL, NULL, NULL, 'ALIMENTACION', 'CONSUMABLE', true, 'COMMON', 1, 'TOOL', true, true, false, 6, 0, 1, NULL, false),
(12, 'Comida Casera Saludable', 'Preparación de platos nutritivos en casa.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180237/e18pdhfdpul0sonpqav4.png', 'kitchen', 15.00, 2, NULL, NULL, NULL, 'ALIMENTACION', 'CONSUMABLE', true, 'COMMON', 1, 'TOOL', true, true, false, 3, 0, 1, NULL, false),

-- ENTRETENIMIENTO
(13, 'Cine VIP', 'Entrada para ver películas en sala premium.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180247/xyxdxngacqq4ryqb8tdd.png', 'movie', 15.00, 2, NULL, NULL, NULL, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', 1, 'NONE', true, true, false, 6, 0, 1, NULL, false),
(14, 'Concierto en Vivo', 'Asistencia a evento musical. Gran aumento de felicidad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180246/qutgdraqh4hgqrphfa7a.png', 'music_note', 40.00, 2, 35.00, '2026-04-15', '2026-04-25', 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', 3, 'NONE', true, true, false, 12, 0, 1, NULL, false),
(15, 'Parque de Atracciones', 'Entrada a parque temático para diversión.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180243/bbtaukshi4okco3q5ah6.png', 'roller_skating', 50.00, 2, NULL, NULL, NULL, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', 2, 'NONE', true, true, false, 24, 0, 1, 'SUMMER', true),
(16, 'Escape Room', 'Experiencia de puzzles y trabajo en equipo.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180243/bbtaukshi4okco3q5ah6.png', 'puzzle', 25.00, 2, NULL, NULL, NULL, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', 2, 'NONE', true, true, false, 24, 0, 1, NULL, false),
(17, 'Bolera', 'Partida de bolos con amigos.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180243/bbtaukshi4okco3q5ah6.png', 'sports_score', 20.00, 2, NULL, NULL, NULL, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', 1, 'NONE', true, true, false, 12, 0, 1, NULL, false),

-- TECNOLOGÍA
(18, 'Ordenador Portátil', 'Equipo personal para trabajo y estudio. Mejora productividad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180244/exx1cawdh0aa4vdzvpzw.png', 'laptop', 1200.00, 2, 999.00, '2026-04-01', '2026-04-30', 'TECNOLOGIA', 'REUSABLE', true, 'RARE', 3, 'TOOL', false, false, true, 0, 0, 3650, NULL, false),
(19, 'Tablet', 'Dispositivo ligero para tareas rápidas y lectura.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180242/vki4pa0kxlnfuwjsxhfs.png', 'tablet', 500.00, 2, 450.00, '2026-04-10', '2026-04-30', 'TECNOLOGIA', 'REUSABLE', true, 'COMMON', 1, 'TOOL', false, false, true, 0, 0, 1095, NULL, false),
(20, 'Smartphone Avanzado', 'Teléfono inteligente con cámara premium.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180241/ewj5eh5caul5mevjf6b1.png', 'smartphone', 1000.00, 2, NULL, NULL, NULL, 'TECNOLOGIA', 'REUSABLE', true, 'RARE', 2, 'TOOL', false, false, true, 0, 0, 1460, NULL, false),

-- SOCIAL
(21, 'Salir con Amigos', 'Reunión social para divertirse y fortalecer lazos.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180268/bwtgaik3qn4yzeojpq3u.png', 'group', 10.00, 2, NULL, NULL, NULL, 'SOCIAL', 'CONSUMABLE', true, 'COMMON', 1, 'NONE', true, true, false, 12, 0, 1, NULL, false),
(22, 'Cita Romántica', 'Encuentro especial para fortalecer relación.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180236/hsskx59nc5rt1g2hhd7s.png', 'favorite', 30.00, 2, NULL, NULL, NULL, 'SOCIAL', 'CONSUMABLE', true, 'RARE', 3, 'NONE', true, true, false, 24, 0, 1, NULL, false),

-- VIVIENDA
(23, 'Alquiler Estudio', 'Pequeño estudio en el centro de la ciudad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180267/xjwmah4igfv6ebdi3pca.png', 'home', 800.00, 2, NULL, NULL, NULL, 'VIVIENDA', 'SUBSCRIPTION', true, 'COMMON', 1, 'NONE', false, false, false, 24, 720, NULL, NULL, false),
(24, 'Casa en las Afueras', 'Vivienda espaciosa con jardín y tranquilidad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180267/xjwmah4igfv6ebdi3pca.png', 'house', 1500.00, 2, 1350.00, '2026-04-01', '2026-05-01', 'VIVIENDA', 'SUBSCRIPTION', true, 'RARE', 10, 'NONE', false, false, false, 24, 720, NULL, NULL, false);

-- ============================================
-- 2. NUEVOS PRODUCTOS REALISTAS
-- ============================================

-- 🚲 TRANSPORTE (más opciones)
INSERT INTO products (id, name, description, img, icon, price, pa, discount_price, discount_start_date, discount_end_date, category, usage_type, active, rarity, required_level, slot, is_consumable, stackable, sellable, cooldown_hours, duration_hours, durability, season, is_event_item) VALUES
(25, 'Patinete Eléctrico', 'Plegable y ligero, ideal para última milla. Reduce tiempo de viaje un 35%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180251/iqy1fwahjfettqjmtp89.png', 'electric_scooter', 600.00, 2, 550.00, '2026-04-01', '2026-04-30', 'TRANSPORTE', 'REUSABLE', true, 'UNCOMMON', 3, 'VEHICLE', false, false, true, 0, 0, 2000, NULL, false),
(26, 'Moto Deportiva', 'Alta velocidad y estilo. Reduce tiempo de viaje un 65%.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180249/gdw3pagtiqr5q7z2qboh.png', 'motorcycle', 8000.00, 2, 7200.00, '2026-04-15', '2026-05-15', 'TRANSPORTE', 'REUSABLE', true, 'EPIC', 15, 'VEHICLE', false, false, true, 0, 0, 15000, NULL, false),

-- 🏋️ SALUD (nuevos productos)
(27, 'Suplementos Deportivos', 'Vitaminas y proteínas para recuperación muscular.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180264/mmyx9fgqp5lwvp5dluux.png', 'medication', 35.00, 2, NULL, NULL, NULL, 'SALUD', 'CONSUMABLE', true, 'COMMON', 2, 'TOOL', true, true, false, 12, 0, 10, NULL, false),
(28, 'Sesión de Psicología', 'Terapia para mejorar salud mental y reducir estrés.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180260/g8mb9vscskiu0kcdfckl.png', 'psychology', 60.00, 2, NULL, NULL, NULL, 'SALUD', 'CONSUMABLE', true, 'RARE', 5, 'TOOL', true, false, false, 168, 0, 1, NULL, false),
(29, 'Chequeo Médico Anual', 'Revisión completa de salud.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180242/vki4pa0kxlnfuwjsxhfs.png', 'health_and_safety', 150.00, 2, 120.00, '2026-04-01', '2026-04-30', 'SALUD', 'CONSUMABLE', true, 'RARE', 8, 'TOOL', true, false, false, 720, 0, 1, NULL, false),

-- 🍽️ ALIMENTACIÓN (nuevos)
(30, 'Suscripción Comida Saludable', 'Plan semanal de comidas preparadas por nutricionistas.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180237/e18pdhfdpul0sonpqav4.png', 'lunch_dining', 120.00, 2, 100.00, '2026-04-01', '2026-05-01', 'ALIMENTACION', 'SUBSCRIPTION', true, 'RARE', 4, 'TOOL', false, false, false, 24, 720, NULL, NULL, false),
(31, 'Clase de Cocina', 'Aprende a preparar platos saludables y deliciosos.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180237/fchzqhkx0e8wlsnbgtsw.png', 'cooking', 40.00, 2, NULL, NULL, NULL, 'ALIMENTACION', 'CONSUMABLE', true, 'UNCOMMON', 2, 'TOOL', true, false, false, 24, 0, 1, NULL, false),

-- 🎬 ENTRETENIMIENTO (nuevos)
(32, 'Teatro', 'Función de teatro en vivo. Experiencia cultural única.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180246/qutgdraqh4hgqrphfa7a.png', 'theater_comedy', 35.00, 2, 28.00, '2026-04-10', '2026-04-20', 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', 2, 'NONE', true, true, false, 12, 0, 1, NULL, false),
(33, 'Museo', 'Entrada a museo de arte o historia.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180243/bbtaukshi4okco3q5ah6.png', 'museum', 15.00, 2, NULL, NULL, NULL, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', 1, 'NONE', true, true, false, 6, 0, 1, NULL, false),
(34, 'Deporte Extremo (Paracaidismo)', 'Aventura y adrenalina garantizada.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180243/bbtaukshi4okco3q5ah6.png', 'paragliding', 200.00, 2, 180.00, '2026-04-01', '2026-04-15', 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'EPIC', 10, 'NONE', true, true, false, 168, 0, 1, 'SUMMER', true),

-- 💻 TECNOLOGÍA (nuevos)
(35, 'Auriculares Premium', 'Cancelación de ruido y sonido envolvente.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180241/ewj5eh5caul5mevjf6b1.png', 'headphones', 250.00, 2, 200.00, '2026-04-01', '2026-04-30', 'TECNOLOGIA', 'REUSABLE', true, 'UNCOMMON', 2, 'TOOL', false, false, true, 0, 0, 730, NULL, false),
(36, 'Smartwatch', 'Monitoriza tu salud y actividad física.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180242/vki4pa0kxlnfuwjsxhfs.png', 'watch', 300.00, 2, NULL, NULL, NULL, 'TECNOLOGIA', 'REUSABLE', true, 'RARE', 3, 'TOOL', false, false, true, 0, 0, 1095, NULL, false),
(37, 'Realidad Virtual', 'Gafas VR para inmersión total.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180244/exx1cawdh0aa4vdzvpzw.png', 'vrpano', 500.00, 2, 450.00, '2026-04-15', '2026-05-15', 'TECNOLOGIA', 'REUSABLE', true, 'EPIC', 8, 'TOOL', false, false, true, 0, 0, 1460, NULL, false),

-- 👥 SOCIAL (nuevos)
(38, 'Fiesta Temática', 'Evento social para conocer gente nueva.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180268/bwtgaik3qn4yzeojpq3u.png', 'celebration', 25.00, 2, NULL, NULL, NULL, 'SOCIAL', 'CONSUMABLE', true, 'COMMON', 1, 'NONE', true, true, false, 24, 0, 1, NULL, false),
(39, 'Retiro Espiritual', 'Desconexión y meditación en la naturaleza.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180236/hsskx59nc5rt1g2hhd7s.png', 'spa', 300.00, 2, 250.00, '2026-04-01', '2026-04-30', 'SOCIAL', 'CONSUMABLE', true, 'EPIC', 12, 'NONE', true, false, false, 720, 24, 1, 'SUMMER', true),

-- 🏠 VIVIENDA (nuevos)
(40, 'Hab Compartida', 'Habitación en piso compartido. Ideal para estudiantes.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180267/xjwmah4igfv6ebdi3pca.png', 'bed', 400.00, 2, NULL, NULL, NULL, 'VIVIENDA', 'SUBSCRIPTION', true, 'COMMON', 1, 'NONE', false, false, false, 24, 720, NULL, NULL, false),
(41, 'Ático de Lujo', 'Vistas espectaculares y todas las comodidades.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180267/xjwmah4igfv6ebdi3pca.png', 'apartment', 2500.00, 2, 2200.00, '2026-04-01', '2026-05-01', 'VIVIENDA', 'SUBSCRIPTION', true, 'LEGENDARY', 25, 'NONE', false, false, false, 24, 720, NULL, NULL, false);-- ============================================
-- 2. EFECTOS INSTANTÁNEOS (product_instant_effects)
-- ============================================

-- Salud y energía
INSERT INTO product_instant_effects (product_id, type, amount, is_percentage, target, is_random, min_amount, max_amount, description) VALUES
(8, 'HEALTH_RESTORE', 20, false, NULL, false, NULL, NULL, 'Restaura 20 puntos de salud'),
(9, 'STRESS_REDUCTION', 15, false, NULL, false, NULL, NULL, 'Reduce 15 puntos de estrés'),
(9, 'ENERGY_RESTORE', 15, false, NULL, false, NULL, NULL, 'Restaura 15 puntos de energía'),
(10, 'STRESS_REDUCTION', 30, false, NULL, false, NULL, NULL, 'Reduce 30 puntos de estrés'),
(10, 'HAPPINESS_BOOST', 20, false, NULL, false, NULL, NULL, 'Aumenta 20 puntos de felicidad'),
(10, 'ENERGY_RESTORE', 10, false, NULL, false, NULL, NULL, 'Restaura 10 puntos de energía'),
(11, 'HAPPINESS_BOOST', 10, false, NULL, false, NULL, NULL, 'Aumenta 10 puntos de felicidad'),
(11, 'ENERGY_RESTORE', 15, false, NULL, false, NULL, NULL, 'Restaura 15 puntos de energía'),
(12, 'HEALTH_RESTORE', 5, false, NULL, false, NULL, NULL, 'Restaura 5 puntos de salud'),
(12, 'ENERGY_RESTORE', 10, false, NULL, false, NULL, NULL, 'Restaura 10 puntos de energía'),
(13, 'HAPPINESS_BOOST', 15, false, NULL, false, NULL, NULL, 'Aumenta 15 puntos de felicidad'),
(13, 'STRESS_REDUCTION', 10, false, NULL, false, NULL, NULL, 'Reduce 10 puntos de estrés'),
(14, 'HAPPINESS_BOOST', 25, false, NULL, false, NULL, NULL, 'Aumenta 25 puntos de felicidad'),
(14, 'STRESS_REDUCTION', 15, false, NULL, false, NULL, NULL, 'Reduce 15 puntos de estrés'),
(15, 'HAPPINESS_BOOST', 30, false, NULL, false, NULL, NULL, 'Aumenta 30 puntos de felicidad'),
(15, 'STRESS_REDUCTION', 20, false, NULL, false, NULL, NULL, 'Reduce 20 puntos de estrés'),
(16, 'INTELLIGENCE_BOOST', 5, false, 'puzzles', false, NULL, NULL, 'Mejora 5 puntos de inteligencia'),
(16, 'HAPPINESS_BOOST', 20, false, NULL, false, NULL, NULL, 'Aumenta 20 puntos de felicidad'),
(17, 'HAPPINESS_BOOST', 15, false, NULL, false, NULL, NULL, 'Aumenta 15 puntos de felicidad'),
(17, 'STRESS_REDUCTION', 10, false, NULL, false, NULL, NULL, 'Reduce 10 puntos de estrés'),
(18, 'XP_ACADEMY_BOOST', 50, false, NULL, false, NULL, NULL, 'Ganas 50 XP académico'),
(18, 'WORK_EFFICIENCY', 10, true, NULL, false, NULL, NULL, '+10% eficiencia en trabajo'),
(19, 'XP_ACADEMY_BOOST', 25, false, NULL, false, NULL, NULL, 'Ganas 25 XP académico'),
(20, 'CHARISMA_BOOST', 5, false, NULL, false, NULL, NULL, 'Mejora 5 puntos de carisma'),
(20, 'XP_JOB_BOOST', 30, false, NULL, false, NULL, NULL, 'Ganas 30 XP laboral'),
(21, 'HAPPINESS_BOOST', 15, false, NULL, false, NULL, NULL, 'Aumenta 15 puntos de felicidad'),
(21, 'STRESS_REDUCTION', 10, false, NULL, false, NULL, NULL, 'Reduce 10 puntos de estrés'),
(21, 'CHARISMA_BOOST', 5, false, NULL, false, NULL, NULL, 'Mejora 5 puntos de carisma'),
(22, 'HAPPINESS_BOOST', 25, false, NULL, false, NULL, NULL, 'Aumenta 25 puntos de felicidad'),
(22, 'STRESS_REDUCTION', 15, false, NULL, false, NULL, NULL, 'Reduce 15 puntos de estrés'),
(22, 'CHARISMA_BOOST', 10, false, NULL, false, NULL, NULL, 'Mejora 10 puntos de carisma');

-- ============================================
-- 3. EFECTOS PASIVOS (product_passive_effects)
-- ============================================

INSERT INTO product_passive_effects (product_id, stat, value, is_percentage, trigger, chance_percentage, description) VALUES
-- Gimnasio (pasivo diario)
(6, 'HEALTH', 2, false, 'DAILY_RESET', 100, '+2 salud cada día'),
(6, 'ENERGY', 3, false, 'DAILY_RESET', 100, '+3 energía cada día'),
(6, 'STRESS', -2, false, 'DAILY_RESET', 100, '-2 estrés cada día'),

-- Yoga
(7, 'STRESS', -3, false, 'DAILY_RESET', 100, '-3 estrés cada día'),
(7, 'FLEXIBILITY', 1, false, 'DAILY_RESET', 50, '+1 flexibilidad (bonificación ocasional)'),

-- Transporte (pasivo mientras equipado)
(1, 'TRAVEL_SPEED', 20, true, 'ON_TRAVEL', 100, 'Reduce 20% tiempo de viaje'),
(2, 'TRAVEL_SPEED', 30, true, 'ON_TRAVEL', 100, 'Reduce 30% tiempo de viaje'),
(3, 'TRAVEL_SPEED', 50, true, 'ON_TRAVEL', 100, 'Reduce 50% tiempo de viaje'),
(4, 'TRAVEL_SPEED', 60, true, 'ON_TRAVEL', 100, 'Reduce 60% tiempo de viaje'),
(5, 'TRAVEL_SPEED', 70, true, 'ON_TRAVEL', 100, 'Reduce 70% tiempo de viaje'),
(5, 'CHARISMA', 5, false, 'ALWAYS', 100, '+5 carisma mientras conduces vehículo de lujo'),

-- Tecnología
(18, 'STUDY_EFFICIENCY', 15, true, 'ON_STUDY_START', 100, '+15% eficiencia de estudio'),
(18, 'WORK_EFFICIENCY', 10, true, 'ON_WORK_START', 100, '+10% eficiencia en trabajo'),
(19, 'STUDY_EFFICIENCY', 10, true, 'ON_STUDY_START', 100, '+10% eficiencia de estudio'),
(20, 'NETWORKING_BONUS', 10, true, 'ON_WORK_START', 50, '+10% probabilidad de networking en trabajo'),

-- Vivienda
(23, 'STRESS', -5, false, 'DAILY_RESET', 100, '-5 estrés cada día'),
(24, 'STRESS', -10, false, 'DAILY_RESET', 100, '-10 estrés cada día'),
(24, 'HAPPINESS_BOOST', 5, false, 'DAILY_RESET', 100, '+5 felicidad cada día');
INSERT INTO product_tags (product_id, tag) VALUES
(1, 'vehiculo'), (1, 'ecologico'), (1, 'deporte'),
(2, 'transporte'), (2, 'abono'),
(3, 'vehiculo'), (3, 'electrico'), (3, 'rapido'),
(4, 'vehiculo'), (4, 'coche'), (4, 'premium'),
(5, 'vehiculo'), (5, 'lujo'), (5, 'status'),
(6, 'deporte'), (6, 'salud'), (6, 'ejercicio'),
(7, 'yoga'), (7, 'relajacion'), (7, 'salud'),
(8, 'salud'), (8, 'emergencia'), (8, 'medicina'),
(9, 'masaje'), (9, 'relajacion'), (9, 'estres'),
(10, 'spa'), (10, 'lujo'), (10, 'relajacion'),
(11, 'comida'), (11, 'restaurante'), (11, 'social'),
(12, 'comida'), (12, 'casera'), (12, 'saludable'),
(13, 'cine'), (13, 'ocio'), (13, 'social'),
(14, 'musica'), (14, 'concierto'), (14, 'evento'),
(15, 'parque'), (15, 'atracciones'), (15, 'diversion'),
(16, 'escape'), (16, 'puzzle'), (16, 'equipo'),
(17, 'bolos'), (17, 'deporte'), (17, 'social'),
(18, 'ordenador'), (18, 'trabajo'), (18, 'estudio'),
(19, 'tablet'), (19, 'lectura'), (19, 'portatil'),
(20, 'telefono'), (20, 'movil'), (20, 'tecnologia'),
(21, 'social'), (21, 'amigos'), (21, 'diversion'),
(22, 'romance'), (22, 'cita'), (22, 'pareja'),
(23, 'vivienda'), (23, 'alquiler'),
(24, 'casa'), (24, 'jardin'), (24, 'tranquilo');
-- ============================================
-- 4. RECETAS DE CRAFTING
-- ============================================

INSERT INTO product_recipes (id, result_product_id, result_quantity, crafting_time_hours, required_skill_level, xp_reward, is_discoverable) VALUES
(1, 1, 1, 2, 1, 10, true),  -- Bicicleta (necesitará materiales)
(2, 3, 1, 8, 5, 50, true);   -- Scooter Eléctrico

-- ============================================
-- 5. INGREDIENTES PARA RECETAS
-- ============================================

-- Receta 1: Bicicleta necesita componentes
INSERT INTO recipe_ingredients (recipe_id, product_id, quantity, is_consumed) VALUES
(1, 19, 1, true),   -- Necesita 1 tablet
(1, 8, 2, true);    -- Necesita 2 kits de primeros auxilios (simulado)

-- Receta 2: Scooter Eléctrico
INSERT INTO recipe_ingredients (recipe_id, product_id, quantity, is_consumed) VALUES
(2, 18, 1, true),   -- Necesita 1 ordenador portátil
(2, 6, 1, true);    -- Necesita suscripción gimnasio

-- ============================================
-- 6. OFERTAS DE TIENDA
-- ============================================

INSERT INTO product_shop_offers (product_id, offer_price, original_price, starts_at, expires_at, stock_limit, remaining_stock, daily_purchase_limit, offer_type, active) VALUES
(1, 250.00, 300.00, '2026-04-01 00:00:00', '2026-04-30 23:59:59', 10, 10, 1, 'DISCOUNT', true),
(3, 1000.00, 1500.00, '2026-04-01 00:00:00', '2026-04-15 23:59:59', 5, 5, 1, 'FLASH_SALE', true),
(6, 40.00, 50.00, '2026-04-01 00:00:00', '2026-04-30 23:59:59', 50, 50, 2, 'DISCOUNT', true),
(14, 30.00, 40.00, '2026-04-15 00:00:00', '2026-04-25 23:59:59', 20, 20, 2, 'DISCOUNT', true);

-- ============================================
-- 7. REINICIAR SECUENCIAS
-- ============================================
SELECT setval('products_id_seq', COALESCE((SELECT MAX(id) FROM products), 24));
SELECT setval('product_instant_effects_id_seq', COALESCE((SELECT MAX(id) FROM product_instant_effects), 100));
SELECT setval('product_passive_effects_id_seq', COALESCE((SELECT MAX(id) FROM product_passive_effects), 50));
SELECT setval('product_recipes_id_seq', COALESCE((SELECT MAX(id) FROM product_recipes), 2));
SELECT setval('product_shop_offers_id_seq', COALESCE((SELECT MAX(id) FROM product_shop_offers), 6));

-- ============================================
-- 8. VERIFICACIÓN
-- ============================================
SELECT 'Products: ' || COUNT(*) FROM products;
SELECT 'Instant Effects: ' || COUNT(*) FROM product_instant_effects;
SELECT 'Passive Effects: ' || COUNT(*) FROM product_passive_effects;
SELECT 'Recipes: ' || COUNT(*) FROM product_recipes;
SELECT 'Ingredients: ' || COUNT(*) FROM recipe_ingredients;
SELECT 'Shop Offers: ' || COUNT(*) FROM product_shop_offers;