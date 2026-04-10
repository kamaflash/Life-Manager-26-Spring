-- ============================================
-- LIMPIAR TABLAS (PostgreSQL)
-- ============================================
TRUNCATE TABLE product_effects CASCADE;
TRUNCATE TABLE products CASCADE;
TRUNCATE TABLE character_inventory CASCADE;
TRUNCATE TABLE product_passive_effects CASCADE;

-- Reiniciar secuencias (si usas IDENTITY)
ALTER SEQUENCE IF EXISTS products_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS product_effects_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS character_inventory_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS product_passive_effects_id_seq RESTART WITH 1;

-- ============================================
-- 1. PRODUCTS
-- ============================================
INSERT INTO products (id, name, description, img, icon, price, pa, category, usage_type, active, rarity, stackable, sellable, cooldown_hours) VALUES
(1, 'Alquiler Estudio', 'Pequeño estudio en el centro de la ciudad. Incluye gastos de comunidad y agua.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180267/xjwmah4igfv6ebdi3pca.png', 'home', 800.00, 4, 'VIVIENDA', 'SUBSCRIPTION', true, 'COMMON', false, false, 24),
(2, 'Suscripción Gimnasio', 'Entrenamiento regular para mejorar la condición física.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180264/mmyx9fgqp5lwvp5dluux.png', 'fitness_center', 50.00, 1, 'SALUD', 'SUBSCRIPTION', true, 'COMMON', false, false, 24),
(3, 'Clases de Yoga', 'Mejora la flexibilidad y reduce el estrés.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180262/ov3d6ondk2mvdwgcm9bz.png', 'self_improvement', 35.00, 1, 'SALUD', 'SUBSCRIPTION', true, 'COMMON', false, false, 12),
(4, 'Terapia de Masajes', 'Alivio muscular y relajación profunda.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180260/g8mb9vscskiu0kcdfckl.png', 'spa', 70.00, 1, 'SALUD', 'CONSUMABLE', true, 'RARE', false, false, 24),
(5, 'Spa Relax', 'Sesión de spa para relajación total.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180260/nl5yvxqdz2drc8amlc1i.png', 'hot_tub', 120.00, 2, 'SALUD', 'CONSUMABLE', true, 'EPIC', false, false, 48),
(6, 'Bicicleta Urbana', 'Bicicleta para desplazamientos cortos en la ciudad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180253/adlplo1cudastgisjwcj.png', 'directions_bike', 300.00, 2, 'TRANSPORTE', 'REUSABLE', true, 'COMMON', false, true, 0),
(7, 'Abono Transporte Público', 'Billete mensual para metro, bus o tranvía.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180252/bs5mr64duuiuakh8doss.png', 'directions_bus', 50.00, 1, 'TRANSPORTE', 'SUBSCRIPTION', true, 'COMMON', false, false, 24),
(8, 'Scooter Motorizado', 'Scooter para moverse rápido por la ciudad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180251/iqy1fwahjfettqjmtp89.png', 'electric_scooter', 1500.00, 2, 'TRANSPORTE', 'REUSABLE', true, 'RARE', false, true, 0),
(9, 'Coche Compacto', 'Vehículo personal para desplazamientos cómodos.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180249/gdw3pagtiqr5q7z2qboh.png', 'directions_car', 12000.00, 3, 'TRANSPORTE', 'REUSABLE', true, 'RARE', false, true, 0),
(10, 'Coche de Lujo', 'Vehículo premium que mejora el estatus social.', '', 'sports_car', 50000.00, 4, 'TRANSPORTE', 'REUSABLE', true, 'LEGENDARY', false, true, 0),
(11, 'Cine VIP', 'Entrada para ver películas en salas premium.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180247/xyxdxngacqq4ryqb8tdd.png', 'movie', 15.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', true, false, 6),
(12, 'Concierto en Vivo', 'Asistencia a evento musical en directo.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180246/qutgdraqh4hgqrphfa7a.png', 'music_note', 40.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 12),
(13, 'Videojuego Nuevo', 'Último lanzamiento de videojuego para consola o PC.', '', 'sports_esports', 60.00, 2, 'ENTRETENIMIENTO', 'REUSABLE', true, 'COMMON', false, true, 0),
(14, 'Teatro', 'Asistencia a obras de teatro y representaciones culturales.', '', 'theater_comedy', 25.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 12),
(15, 'Parque de Atracciones', 'Entrada a parque temático para diversión y aventura.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180243/bbtaukshi4okco3q5ah6.png', 'roller_skating', 50.00, 3, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 24),
(16, 'Ordenador Portátil', 'Equipo personal para trabajo y estudio con buen rendimiento.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180244/exx1cawdh0aa4vdzvpzw.png', 'laptop', 1200.00, 2, 'TECNOLOGIA', 'REUSABLE', true, 'RARE', false, true, 0),
(17, 'Tablet', 'Dispositivo ligero para tareas rápidas y lectura.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180242/vki4pa0kxlnfuwjsxhfs.png', 'tablet', 500.00, 1, 'TECNOLOGIA', 'REUSABLE', true, 'COMMON', false, true, 0),
(18, 'Smartphone Avanzado', 'Teléfono inteligente para comunicación y productividad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180241/ewj5eh5caul5mevjf6b1.png', 'smartphone', 1000.00, 1, 'TECNOLOGIA', 'REUSABLE', true, 'RARE', false, true, 0),
(19, 'Cámara Profesional', 'Equipo para fotografía y vídeo de alta calidad.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180240/efaylntlteh0akpa2qo1.png', 'camera_alt', 1500.00, 2, 'TECNOLOGIA', 'REUSABLE', true, 'EPIC', false, true, 0),
(20, 'Cena Restaurante', 'Comida preparada para disfrutar fuera de casa.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180237/fchzqhkx0e8wlsnbgtsw.png', 'restaurant', 25.00, 2, 'ALIMENTACION', 'CONSUMABLE', true, 'COMMON', true, false, 6),
(21, 'Comida Casera', 'Preparación de platos saludables en casa.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180237/e18pdhfdpul0sonpqav4.png', 'kitchen', 0.00, 2, 'ALIMENTACION', 'CONSUMABLE', true, 'COMMON', true, false, 3),
(22, 'Salir con Amigos', 'Reunión social casual para divertirse.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180268/bwtgaik3qn4yzeojpq3u.png', 'group', 10.00, 2, 'SOCIAL', 'CONSUMABLE', true, 'COMMON', false, false, 12),
(23, 'Cita Romántica', 'Encuentro especial para fortalecer la relación.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180236/hsskx59nc5rt1g2hhd7s.png', 'favorite', 30.00, 2, 'SOCIAL', 'CONSUMABLE', true, 'RARE', false, false, 24),
(24, 'Llamar a un amigo', 'Conectarse por teléfono y mantener el vínculo.', 'https://res.cloudinary.com/dxjcjee8f/image/upload/v1773180236/xvb6dzzjyu9rlb1fu7q1.png', 'phone', 0.00, 1, 'SOCIAL', 'CONSUMABLE', true, 'COMMON', true, false, 1),
(25, 'Voluntariado', 'Participar en actividades sociales y ayudar a otros.', '', 'volunteer_activism', 0.00, 3, 'SOCIAL', 'CONSUMABLE', true, 'RARE', false, false, 24),
(26, 'Monitor 4K', 'Pantalla de alta resolución para trabajar o jugar.', '', 'monitor', 350.00, 2, 'TECNOLOGIA', 'REUSABLE', true, 'RARE', false, true, 0),
(28, 'Videoconsola 5G', 'Videoconsola de última generación. Disfruta de videojuegos con la mejor fluides y gráficos.', '', 'mouse', 500.00, 1, 'TECNOLOGIA', 'REUSABLE', true, 'COMMON', false, true, 0),
(34, 'Escape Room', 'Experiencia de puzzles y trabajo en equipo.', '', 'puzzle', 25.00, 3, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 24),
(35, 'Bolera', 'Partida de bolos con amigos.', '', 'sports_score', 20.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', false, false, 12),
(36, 'Karaoke', 'Noche de canciones y diversión asegurada.', '', 'mic_external_on', 15.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', false, false, 12),
(37, 'Paintball', 'Juego de estrategia y adrenalina al aire libre.', '', 'sports_mma', 30.00, 3, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 24),
(38, 'Minigolf', 'Partida de minigolf para disfrutar en familia o amigos.', '', 'golf_course', 12.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'COMMON', false, false, 12),
(39, 'Simulador de Vuelo', 'Experiencia de realidad virtual de vuelo.', '', 'flight', 45.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 24),
(40, 'Sala de Escape Virtual', 'Experiencia online de escape room desde casa.', '', 'vrpano', 18.00, 2, 'ENTRETENIMIENTO', 'CONSUMABLE', true, 'RARE', false, false, 12);

-- ============================================
-- 2. PRODUCT EFFECTS
-- ============================================
INSERT INTO product_effects (product_id, category, effect_value) VALUES
(1, 'ENERGY', -2), (1, 'STRESS', -3), (1, 'HAPPINESS', 3),
(2, 'ENERGY', -3), (2, 'STRESS', -3), (2, 'HAPPINESS', 3), (2, 'HEALTH', 1),
(3, 'ENERGY', -2), (3, 'STRESS', -4), (3, 'HAPPINESS', 3), (3, 'HEALTH', 1),
(4, 'ENERGY', -2), (4, 'STRESS', -5), (4, 'HAPPINESS', 4), (4, 'HEALTH', 1),
(5, 'ENERGY', -1), (5, 'STRESS', -6), (5, 'HAPPINESS', 5), (5, 'HEALTH', 1),
(6, 'ENERGY', -3), (6, 'STRESS', -2), (6, 'HEALTH', 1),
(7, 'ENERGY', -2), (7, 'STRESS', -2), (7, 'FINANCES', 3),
(8, 'ENERGY', -2), (8, 'STRESS', -1), (8, 'CHARISMA', 1),
(9, 'ENERGY', -1), (9, 'CHARISMA', 2),
(10, 'CHARISMA', 5), (10, 'HAPPINESS', 3),
(11, 'CHARISMA', 1), (11, 'HAPPINESS', 3), (11, 'CREATIVITY', 1), (11, 'STRESS', -2),
(12, 'CHARISMA', 2), (12, 'HAPPINESS', 4), (12, 'CREATIVITY', 2), (12, 'STRESS', -3),
(13, 'INTELLIGENCE', 1), (13, 'HAPPINESS', 3), (13, 'CREATIVITY', 1), (13, 'STRESS', -2),
(14, 'CHARISMA', 2), (14, 'HAPPINESS', 3), (14, 'CREATIVITY', 2), (14, 'INTELLIGENCE', 1),
(15, 'HAPPINESS', 5), (15, 'STRESS', -4), (15, 'ENERGY', -2),
(16, 'INTELLIGENCE', 3), (16, 'CREATIVITY', 2),
(17, 'INTELLIGENCE', 1), (17, 'HAPPINESS', 2),
(18, 'INTELLIGENCE', 1), (18, 'CHARISMA', 3),
(19, 'CREATIVITY', 4), (19, 'INTELLIGENCE', 1),
(20, 'HAPPINESS', 3), (20, 'HEALTH', 1), (20, 'STRESS', -2), (20, 'CHARISMA', 2),
(21, 'HAPPINESS', 2), (21, 'HEALTH', 2), (21, 'STRESS', -2), (21, 'FINANCES', 2),
(22, 'HAPPINESS', 3), (22, 'STRESS', -3), (22, 'CHARISMA', 2), (22, 'ENERGY', -2),
(23, 'HAPPINESS', 4), (23, 'STRESS', -3), (23, 'CHARISMA', 3),
(24, 'HAPPINESS', 1), (24, 'STRESS', -1), (24, 'CHARISMA', 1),
(25, 'CHARISMA', 3), (25, 'HAPPINESS', 2),
(26, 'INTELLIGENCE', 2), (26, 'CREATIVITY', 2),
(28, 'PRODUCTIVITY', 1),
(34, 'INTELLIGENCE', 2), (34, 'CHARISMA', 2), (34, 'HAPPINESS', 4),
(35, 'HAPPINESS', 4), (35, 'STRESS', -3),
(36, 'CHARISMA', 3), (36, 'HAPPINESS', 4), (36, 'STRESS', -3),
(37, 'ENERGY', -4), (37, 'STRESS', -4), (37, 'HAPPINESS', 5),
(38, 'HAPPINESS', 3), (38, 'STRESS', -3),
(39, 'INTELLIGENCE', 2), (39, 'HAPPINESS', 5),
(40, 'INTELLIGENCE', 2), (40, 'HAPPINESS', 3);

-- ============================================
-- 3. PRODUCT PASSIVE EFFECTS (ejemplo)
-- ============================================
-- INSERT INTO product_passive_effects (product_id, stat, value, percentage, condition, condition_value) VALUES
-- (6, 'ENERGY', 5, false, 'TIME_OF_DAY', 'MORNING'),
-- (16, 'INTELLIGENCE', 10, true, 'HAS_BUFF', 'STUDYING');

-- ============================================
-- 4. REINICIAR SECUENCIAS
-- ============================================
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));
SELECT setval('product_effects_id_seq', (SELECT MAX(id) FROM product_effects));