-- =====================================================
-- EVENTOS REALISTAS 2026 - AUTO_TRIGGER Y RECURRENTES
-- =====================================================

-- ENERO - Año Nuevo
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_NEWYEAR_001', '🎆 Año Nuevo', 'Festivo nacional. Celebra el inicio del año con familia y amigos.', 'Un nuevo año trae nuevas oportunidades y propósitos.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-01-01 00:00:00', '2026-01-01 23:59:59', true, '0 0 0 1 1 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/newyear-icon.png', 'https://cdn.example.com/events/newyear-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_NEWYEAR_001'), 'STAT_BOOST', 'happiness', 20, true, NULL, NULL, NULL);

-- ENERO - Reyes Magos
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_KINGS_001', '👑 Día de Reyes', 'Los Reyes Magos traen ilusión y regalos. Tradición familiar.', 'La magia de la infancia nunca se pierde.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-01-06 00:00:00', '2026-01-06 23:59:59', true, '0 0 0 6 1 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/kings-icon.png', 'https://cdn.example.com/events/kings-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_KINGS_001'), 'STAT_BOOST', 'happiness', 15, true, NULL, NULL, NULL);

-- FEBRERO - Día de Andalucía
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_ANDALUCIA_001', '🌸 Día de Andalucía', 'Fiesta autonómica. Celebra la cultura y tradiciones andaluzas.', 'Andalucía tiene un color y un sabor especial.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-02-28 00:00:00', '2026-02-28 23:59:59', true, '0 0 0 28 2 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/andalucia-icon.png', 'https://cdn.example.com/events/andalucia-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_ANDALUCIA_001'), 'XP_JOBS', 'culture', 100, true, NULL, NULL, NULL);

-- MARZO - Día del Padre
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_FATHERS_DAY_001', '👔 Día del Padre', 'Homenaje a los padres. Celebra con tu familia.', 'Un padre es un héroe sin capa.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-03-19 00:00:00', '2026-03-19 23:59:59', true, '0 0 0 19 3 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/fathers-icon.png', 'https://cdn.example.com/events/fathers-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_FATHERS_DAY_001'), 'STAT_BOOST', 'happiness', 10, true, NULL, NULL, NULL);

-- ABRIL - Jueves Santo
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_HOLY_THURSDAY_001', '⛪ Jueves Religioso', 'Inicio de la Semana Santa. Procesiones y tradición.', 'La Semana Santa es cultura, fe y tradición.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-04-02 00:00:00', '2026-04-02 23:59:59', true, '0 0 0 2 4 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/holy-icon.png', 'https://cdn.example.com/events/holy-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_HOLY_THURSDAY_001'), 'XP_JOBS', 'culture', 80, true, NULL, NULL, NULL);

-- ABRIL - Viernes Santo
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_GOOD_FRIDAY_001', '⛪ Viernes Santo', 'Día de recogimiento y tradición.', 'El silencio también habla.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-04-03 00:00:00', '2026-04-03 23:59:59', true, '0 0 0 3 4 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/goodfriday-icon.png', 'https://cdn.example.com/events/goodfriday-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_GOOD_FRIDAY_001'), 'XP_JOBS', 'culture', 80, true, NULL, NULL, NULL);

-- MAYO - Día del Trabajador
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_LABOR_DAY_001', '👷 Día del Trabajador', 'Festivo nacional. Homenaje a todos los trabajadores.', 'El trabajo dignifica, el descanso también es necesario.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-05-01 00:00:00', '2026-05-01 23:59:59', true, '0 0 0 1 5 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/labor-icon.png', 'https://cdn.example.com/events/labor-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_LABOR_DAY_001'), 'XP_JOBS', 'celebration', 150, true, NULL, NULL, NULL);

-- MAYO - Día de la Madre
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_MOTHERS_DAY_001', '💐 Día de la Madre', 'Homenaje a las madres. Celebra con tu familia.', 'Madre solo hay una.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-05-03 00:00:00', '2026-05-03 23:59:59', true, '0 0 0 3 5 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/mothers-icon.png', 'https://cdn.example.com/events/mothers-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_MOTHERS_DAY_001'), 'STAT_BOOST', 'happiness', 15, true, NULL, NULL, NULL);

-- AGOSTO - Asunción de la Virgen
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_ASUNCION_001', '⛪ Asunción de la Virgen', 'Festivo nacional. Tradición y celebración religiosa.', 'El verano está en su punto más álgido.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-08-15 00:00:00', '2026-08-15 23:59:59', true, '0 0 0 15 8 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/asuncion-icon.png', 'https://cdn.example.com/events/asuncion-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_ASUNCION_001'), 'STAT_BOOST', 'happiness', 10, true, NULL, NULL, NULL);

-- OCTUBRE - Día de la Hispanidad
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_HISPANIDAD_001', '🇪🇸 Día de la Hispanidad', 'Fiesta nacional. Desfile militar y celebraciones patrióticas.', 'Orgullo de ser hispano.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-10-12 00:00:00', '2026-10-12 23:59:59', true, '0 0 0 12 10 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/hispanidad-icon.png', 'https://cdn.example.com/events/hispanidad-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_HISPANIDAD_001'), 'XP_JOBS', 'culture', 120, true, NULL, NULL, NULL);

-- NOVIEMBRE - Día de Todos los Santos
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_ALL_SAINTS_001', '🕯️ Día de Todos los Santos', 'Festivo nacional. Recuerda a tus seres queridos.', 'La memoria es el corazón de la tradición.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-11-01 00:00:00', '2026-11-01 23:59:59', true, '0 0 0 1 11 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/allsaints-icon.png', 'https://cdn.example.com/events/allsaints-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_ALL_SAINTS_001'), 'XP_JOBS', 'culture', 80, true, NULL, NULL, NULL);

-- DICIEMBRE - Día de la Constitución
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_CONSTITUTION_001', '📜 Día de la Constitución', 'Festivo nacional. Celebra nuestra Carta Magna.', 'La constitución nos une como nación.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-12-06 00:00:00', '2026-12-06 23:59:59', true, '0 0 0 6 12 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/constitution-icon.png', 'https://cdn.example.com/events/constitution-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_CONSTITUTION_001'), 'XP_JOBS', 'culture', 100, true, NULL, NULL, NULL);

-- DICIEMBRE - Inmaculada Concepción
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_IMMACULATE_001', '⛪ Inmaculada Concepción', 'Festivo nacional. Tradición y recogimiento.', 'La pureza y la fe se celebran.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-12-08 00:00:00', '2026-12-08 23:59:59', true, '0 0 0 8 12 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/immaculate-icon.png', 'https://cdn.example.com/events/immaculate-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_IMMACULATE_001'), 'XP_JOBS', 'culture', 80, true, NULL, NULL, NULL);

-- DICIEMBRE - Nochebuena
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_CHRISTMAS_EVE_001', '🎄 Nochebuena', 'Cena familiar y celebración navideña.', 'La magia de la Navidad comienza en Nochebuena.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-12-24 00:00:00', '2026-12-24 23:59:59', true, '0 0 0 24 12 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/christmaseve-icon.png', 'https://cdn.example.com/events/christmaseve-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_CHRISTMAS_EVE_001'), 'STAT_BOOST', 'happiness', 25, true, NULL, NULL, NULL);

-- DICIEMBRE - Navidad
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_CHRISTMAS_DAY_001', '🎄 Navidad', 'Festivo nacional. Celebra en familia el nacimiento de Jesús.', 'La Navidad es amor, paz y esperanza.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-12-25 00:00:00', '2026-12-25 23:59:59', true, '0 0 0 25 12 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/christmas-icon.png', 'https://cdn.example.com/events/christmas-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_CHRISTMAS_DAY_001'), 'STAT_BOOST', 'happiness', 30, true, NULL, NULL, NULL);

-- DICIEMBRE - Nochevieja
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_NEWYEAR_EVE_001', '🎆 Nochevieja', 'Despide el año con las 12 uvas y celebración.', 'El año nuevo está a la vuelta de la esquina.', 'PROJECT_SUCCESS', 'GLOBAL', NULL, '2026-12-31 00:00:00', '2026-12-31 23:59:59', true, '0 0 0 31 12 ? *', 999999, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/newyeareve-icon.png', 'https://cdn.example.com/events/newyeareve-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, item_code, title_granted, badge_code)
VALUES ((SELECT id FROM events WHERE code = 'EVENT_NEWYEAR_EVE_001'), 'STAT_BOOST', 'happiness', 35, true, NULL, NULL, NULL);

-- =====================================================
-- VERIFICACIÓN
-- =====================================================
SELECT 'Eventos GLOBAL: ' || COUNT(*) FROM events WHERE scope = 'GLOBAL';
SELECT 'Eventos con auto_trigger: ' || COUNT(*) FROM events WHERE auto_trigger = true;
SELECT 'Eventos recurrentes: ' || COUNT(*) FROM events WHERE recurring = true;
SELECT 'Total Rewards: ' || COUNT(*) FROM rewards;

-- =====================================================
-- SISTEMA DE MISIONES PROGRESIVO - CORREGIDO
-- MissionType: ONE_TIME, DAILY, WEEKLY, MONTHLY, REPEATABLE, STORY
-- MissionCategory: STUDY, WORK, SOCIAL, ROMANCE, EXPLORATION, TRAINING, PERSONAL, SPECIAL
-- MissionDifficulty: EASY, MEDIUM, HARD, EPIC
-- =====================================================

-- =====================================================
-- NIVEL 1: MISIONES PRINCIPIANTES (0-200 XP JOBS)
-- =====================================================

-- MISIÓN 1: Primer día de trabajo
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_FIRST_DAY_WORK', 'Primer Día de Trabajo', 'Es tu primer día en el nuevo trabajo. Causa una buena impresión.', 'Todo gran viaje comienza con un primer paso. La puntualidad y la actitud lo son todo.', 'STORY', 'WORK', 'EASY', 16, 0, 0, false, NULL, false, true, 1, NULL, 'https://cdn.example.com/missions/first-day-icon.png', 'https://cdn.example.com/missions/first-day-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'Llega puntual a tu primer día', 'COMPLETE_ACTIONS', 'punctual_first_day', 1, 0, false, 1, 'La puntualidad es clave para causar buena impresión'),
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'Preséntate a tus compañeros', 'COMPLETE_ACTIONS', 'introduce_yourself', 1, 0, false, 2, 'Sé amable y sonríe'),
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'Completa la orientación inicial', 'COMPLETE_ACTIONS', 'orientation', 1, 0, false, 3, 'Presta atención a la información importante');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'XP_JOBS', 'first_day', 200, true),
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'MONEY', NULL, 500, false);

INSERT INTO requirements (mission_id, type, target_value, fail_message)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'MIN_AGE', 16, 'Debes tener al menos 16 años para trabajar');

INSERT INTO mission_unlock_codes (mission_id, unlocks_mission_code)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_FIRST_DAY_WORK'), 'MISSION_COMPLETE_PROBATION');

-- MISIÓN 2: Completar período de prueba
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_COMPLETE_PROBATION', 'Completar Período de Prueba', 'Supera tus primeros 90 días en la empresa y consigue la estabilidad.', 'El período de prueba es tu oportunidad para brillar y demostrar tu valía.', 'STORY', 'WORK', 'MEDIUM', 16, 50, 0, false, NULL, false, false, 2, NULL, 'https://cdn.example.com/missions/probation-icon.png', 'https://cdn.example.com/missions/probation-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_COMPLETE_PROBATION'), 'Completa 30 días de trabajo', 'COMPLETE_ACTIONS', 'work_day', 30, 0, false, 1, 'Asiste puntualmente cada día'),
((SELECT id FROM missions WHERE code = 'MISSION_COMPLETE_PROBATION'), 'Recibe feedback positivo', 'COMPLETE_ACTIONS', 'positive_feedback', 3, 0, false, 2, 'Haz preguntas y muestra iniciativa');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_COMPLETE_PROBATION'), 'XP_JOBS', 'probation', 500, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_COMPLETE_PROBATION'), 'TITLE', NULL, NULL, false, 'Empleado Fijo');

INSERT INTO requirements (mission_id, type, target_key, fail_message)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_COMPLETE_PROBATION'), 'MISSION_COMPLETED', 'MISSION_FIRST_DAY_WORK', 'Debes completar el primer día primero');

INSERT INTO mission_unlock_codes (mission_id, unlocks_mission_code)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_COMPLETE_PROBATION'), 'MISSION_BUILD_NETWORK');

-- =====================================================
-- NIVEL 2: MISIONES INTERMEDIAS (200-500 XP JOBS)
-- =====================================================

-- MISIÓN 3: Construir red de contactos
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_BUILD_NETWORK', 'Construir Red de Contactos', 'Amplía tu círculo profesional con nuevas conexiones.', 'En los negocios, a quién conoces es tan importante como lo que sabes.', 'REPEATABLE', 'SOCIAL', 'MEDIUM', 18, 200, 0, true, 720, false, false, NULL, NULL, 'https://cdn.example.com/missions/network-icon.png', 'https://cdn.example.com/missions/network-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_BUILD_NETWORK'), 'Asiste a 3 eventos de networking', 'ATTEND_EVENT', 'networking_event', 3, 0, false, 1, 'Busca eventos profesionales en tu ciudad'),
((SELECT id FROM missions WHERE code = 'MISSION_BUILD_NETWORK'), 'Añade 10 contactos profesionales', 'COMPLETE_ACTIONS', 'professional_contact', 10, 0, false, 2, 'Conecta en LinkedIn y redes profesionales'),
((SELECT id FROM missions WHERE code = 'MISSION_BUILD_NETWORK'), 'Intercambia tarjetas de visita', 'COMPLETE_ACTIONS', 'business_card', 5, 0, false, 3, 'Lleva siempre tus tarjetas contigo');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, badge_code)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_BUILD_NETWORK'), 'XP_JOBS', 'networking', 400, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_BUILD_NETWORK'), 'BADGE', NULL, NULL, false, 'NETWORKER');

-- MISIÓN 4: Completar primer proyecto importante
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_FIRST_BIG_PROJECT', 'Primer Proyecto Importante', 'Lidera o participa en un proyecto de alto impacto.', 'Los grandes proyectos forjan grandes profesionales.', 'STORY', 'WORK', 'HARD', 21, 400, 100, false, NULL, false, false, 3, NULL, 'https://cdn.example.com/missions/project-icon.png', 'https://cdn.example.com/missions/project-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_BIG_PROJECT'), 'Completa un proyecto con éxito', 'COMPLETE_ACTIONS', 'successful_project', 1, 0, false, 1, 'Aplica todo lo aprendido'),
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_BIG_PROJECT'), 'Recibe reconocimiento del jefe', 'COMPLETE_ACTIONS', 'boss_recognition', 1, 0, false, 2, 'Supera las expectativas');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_BIG_PROJECT'), 'XP_JOBS', 'big_project', 800, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_FIRST_BIG_PROJECT'), 'TITLE', NULL, NULL, false, 'Project Leader');

INSERT INTO requirements (mission_id, type, target_key, fail_message)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_FIRST_BIG_PROJECT'), 'MISSION_COMPLETED', 'MISSION_COMPLETE_PROBATION', 'Debes superar el período de prueba primero');

INSERT INTO mission_unlock_codes (mission_id, unlocks_mission_code)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_FIRST_BIG_PROJECT'), 'MISSION_FIND_MENTOR');

-- =====================================================
-- NIVEL 3: MISIONES AVANZADAS (500-1000 XP JOBS)
-- =====================================================

-- MISIÓN 5: Encontrar un mentor
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_FIND_MENTOR', 'Encontrar un Mentor', 'Busca la guía de un profesional experimentado.', 'Un buen mentor acelera tu crecimiento profesional exponencialmente.', 'STORY', 'WORK', 'HARD', 21, 600, 200, false, NULL, false, false, NULL, NULL, 'https://cdn.example.com/missions/mentor-icon.png', 'https://cdn.example.com/missions/mentor-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FIND_MENTOR'), 'Identifica 3 mentores potenciales', 'COMPLETE_ACTIONS', 'identify_mentor', 3, 0, false, 1, 'Busca profesionales que admires'),
((SELECT id FROM missions WHERE code = 'MISSION_FIND_MENTOR'), 'Solicita una reunión de mentoría', 'COMPLETE_ACTIONS', 'mentor_meeting', 1, 0, false, 2, 'Prepara un buen pitch'),
((SELECT id FROM missions WHERE code = 'MISSION_FIND_MENTOR'), 'Establece relación de mentoría', 'COMPLETE_ACTIONS', 'mentorship_established', 1, 0, false, 3, 'Muestra interés genuino en aprender');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, badge_code)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FIND_MENTOR'), 'XP_JOBS', 'mentorship', 800, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_FIND_MENTOR'), 'BADGE', NULL, NULL, false, 'MENTEE');

-- MISIÓN 6: Formación especializada
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_SPECIALIZED_TRAINING', 'Formación Especializada', 'Completa un curso o certificación en tu área.', 'El conocimiento nunca es suficiente. Siempre hay algo nuevo que aprender.', 'REPEATABLE', 'STUDY', 'MEDIUM', 20, 700, 300, true, 2160, false, false, NULL, NULL, 'https://cdn.example.com/missions/training-icon.png', 'https://cdn.example.com/missions/training-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_SPECIALIZED_TRAINING'), 'Completa un curso profesional', 'COMPLETE_ACTIONS', 'professional_course', 1, 0, false, 1, 'Busca cursos en tu área'),
((SELECT id FROM missions WHERE code = 'MISSION_SPECIALIZED_TRAINING'), 'Obtén una certificación', 'COMPLETE_ACTIONS', 'certification', 1, 0, false, 2, 'Certifica tus conocimientos');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_SPECIALIZED_TRAINING'), 'XP_JOBS', 'training', 600, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_SPECIALIZED_TRAINING'), 'TITLE', NULL, NULL, false, 'Certified Professional');

-- =====================================================
-- NIVEL 4: MISIONES EXPERTO (1000-2000 XP JOBS)
-- =====================================================

-- MISIÓN 7: Ser mentor de otros
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_BECOME_MENTOR', 'Convertirse en Mentor', 'Comparte tu conocimiento con profesionales más jóvenes.', 'El conocimiento compartido se multiplica.', 'STORY', 'WORK', 'EPIC', 25, 1200, 500, false, NULL, false, false, 4, NULL, 'https://cdn.example.com/missions/become-mentor-icon.png', 'https://cdn.example.com/missions/become-mentor-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'Mentorea a 3 personas', 'COMPLETE_ACTIONS', 'mentor_others', 3, 0, false, 1, 'Comparte tu experiencia'),
((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'Ayuda a alguien a ascender', 'COMPLETE_ACTIONS', 'help_promotion', 1, 0, false, 2, 'Guía a otros hacia el éxito'),
((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'Recibe feedback positivo como mentor', 'COMPLETE_ACTIONS', 'positive_mentor_feedback', 3, 0, false, 3, 'Tus mentorizados te lo agradecerán');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted, badge_code)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'XP_JOBS', 'mentor', 1500, true, NULL, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'TITLE', NULL, NULL, false, 'Master Mentor', NULL),
((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'BADGE', NULL, NULL, false, NULL, 'MENTOR_LEGEND');

INSERT INTO requirements (mission_id, type, target_key, fail_message)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_BECOME_MENTOR'), 'MISSION_COMPLETED', 'MISSION_FIND_MENTOR', 'Debes haber tenido un mentor primero');

-- MISIÓN 8: Logro financiero
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_FINANCIAL_GOAL', 'Primer Gran Ahorro', 'Alcanza tu primer objetivo financiero importante.', 'La libertad financiera comienza con pequeños pasos.', 'ONE_TIME', 'PERSONAL', 'HARD', 22, 800, 200, false, NULL, false, false, NULL, NULL, 'https://cdn.example.com/missions/financial-icon.png', 'https://cdn.example.com/missions/financial-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FINANCIAL_GOAL'), 'Ahorra 5000 monedas', 'COMPLETE_ACTIONS', 'save_money', 5000, 0, false, 1, 'Controla tus gastos'),
((SELECT id FROM missions WHERE code = 'MISSION_FINANCIAL_GOAL'), 'Invierte en formación', 'COMPLETE_ACTIONS', 'invest_training', 1000, 0, false, 2, 'Invertir en ti es la mejor inversión');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_FINANCIAL_GOAL'), 'XP_JOBS', 'financial', 600, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_FINANCIAL_GOAL'), 'TITLE', NULL, NULL, false, 'Savvy Saver');

-- =====================================================
-- NIVEL 5: MISIONES ÉLITE (2000+ XP JOBS)
-- =====================================================

-- MISIÓN 9: Liderazgo de equipo
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_TEAM_LEADER', 'Liderar un Equipo', 'Asume el liderazgo de un equipo de trabajo.', 'Los líderes no nacen, se hacen a base de esfuerzo.', 'STORY', 'WORK', 'EPIC', 28, 2000, 800, false, NULL, false, false, 5, NULL, 'https://cdn.example.com/missions/leader-icon.png', 'https://cdn.example.com/missions/leader-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_TEAM_LEADER'), 'Asume rol de liderazgo', 'COMPLETE_ACTIONS', 'leadership_role', 1, 0, false, 1, 'Demuestra tu capacidad'),
((SELECT id FROM missions WHERE code = 'MISSION_TEAM_LEADER'), 'Completa 5 proyectos como líder', 'COMPLETE_ACTIONS', 'projects_as_leader', 5, 0, false, 2, 'Lidera con el ejemplo'),
((SELECT id FROM missions WHERE code = 'MISSION_TEAM_LEADER'), 'Mejora la moral del equipo', 'COMPLETE_ACTIONS', 'team_morale', 80, 0, false, 3, 'Un equipo feliz es productivo');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted, badge_code)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_TEAM_LEADER'), 'XP_JOBS', 'leadership', 3000, true, NULL, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_TEAM_LEADER'), 'TITLE', NULL, NULL, false, 'Team Leader', NULL),
((SELECT id FROM missions WHERE code = 'MISSION_TEAM_LEADER'), 'BADGE', NULL, NULL, false, NULL, 'BORN_LEADER');

-- MISIÓN 10: Reconocimiento internacional
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_INTERNATIONAL_RECOGNITION', 'Reconocimiento Internacional', 'Tu trabajo trasciende fronteras y es reconocido globalmente.', 'El éxito no tiene fronteras.', 'STORY', 'WORK', 'EPIC', 30, 3000, 1000, false, NULL, true, false, 6, NULL, 'https://cdn.example.com/missions/international-icon.png', 'https://cdn.example.com/missions/international-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_INTERNATIONAL_RECOGNITION'), 'Participa en conferencia internacional', 'COMPLETE_ACTIONS', 'international_conference', 1, 0, false, 1, 'Comparte tu conocimiento globalmente'),
((SELECT id FROM missions WHERE code = 'MISSION_INTERNATIONAL_RECOGNITION'), 'Recibe premio internacional', 'COMPLETE_ACTIONS', 'international_award', 1, 0, false, 2, 'El reconocimiento llega con excelencia'),
((SELECT id FROM missions WHERE code = 'MISSION_INTERNATIONAL_RECOGNITION'), 'Colabora con equipo internacional', 'COMPLETE_ACTIONS', 'international_collaboration', 3, 0, false, 3, 'Trabaja más allá de fronteras');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted, badge_code)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_INTERNATIONAL_RECOGNITION'), 'XP_JOBS', 'international', 5000, true, NULL, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_INTERNATIONAL_RECOGNITION'), 'TITLE', NULL, NULL, false, 'Global Expert', NULL),
((SELECT id FROM missions WHERE code = 'MISSION_INTERNATIONAL_RECOGNITION'), 'BADGE', NULL, NULL, false, NULL, 'WORLD_CLASS');

-- =====================================================
-- MISIONES DIARIAS Y REPETIBLES
-- =====================================================

-- MISIÓN DIARIA: Estudiar
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_DAILY_STUDY', 'Estudio Diario', 'Dedica tiempo a estudiar cada día.', 'La constancia es la clave del éxito académico.', 'DAILY', 'STUDY', 'EASY', 14, 0, 0, true, 24, false, true, NULL, NULL, 'https://cdn.example.com/missions/study-daily-icon.png', 'https://cdn.example.com/missions/study-daily-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_DAILY_STUDY'), 'Estudia durante 2 horas', 'COMPLETE_ACTIONS', 'study_time', 120, 0, false, 1, 'Encuentra un lugar tranquilo');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_DAILY_STUDY'), 'XP_ACADEMY', 'daily_study', 100, true);

-- MISIÓN DIARIA: Ejercicio
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_DAILY_EXERCISE', 'Ejercicio Diario', 'Mantén tu cuerpo activo con ejercicio regular.', 'Mente sana en cuerpo sano.', 'DAILY', 'TRAINING', 'EASY', 14, 0, 0, true, 24, false, true, NULL, NULL, 'https://cdn.example.com/missions/exercise-icon.png', 'https://cdn.example.com/missions/exercise-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_DAILY_EXERCISE'), 'Haz ejercicio 30 minutos', 'COMPLETE_ACTIONS', 'exercise_time', 30, 0, false, 1, 'Camina, corre o ve al gimnasio');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_DAILY_EXERCISE'), 'STAT_BOOST', 'endurance', 2, true);

-- MISIÓN DIARIA: Socializar
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_DAILY_SOCIALIZE', 'Socializar Diario', 'Conecta con otras personas cada día.', 'Las relaciones son el tejido de la vida.', 'DAILY', 'SOCIAL', 'EASY', 14, 0, 0, true, 24, false, true, NULL, NULL, 'https://cdn.example.com/missions/social-icon.png', 'https://cdn.example.com/missions/social-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_DAILY_SOCIALIZE'), 'Conversa con 5 personas', 'COMPLETE_ACTIONS', 'conversations', 5, 0, false, 1, 'Saluda a tus compañeros');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_DAILY_SOCIALIZE'), 'STAT_BOOST', 'charisma', 2, true);

-- =====================================================
-- MISIÓN SEMANAL
-- =====================================================

INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_WEEKLY_NETWORKING', 'Networking Semanal', 'Mantén activa tu red de contactos profesionales.', 'Las relaciones profesionales necesitan mantenimiento constante.', 'WEEKLY', 'SOCIAL', 'MEDIUM', 18, 300, 0, true, 168, false, true, NULL, NULL, 'https://cdn.example.com/missions/weekly-network-icon.png', 'https://cdn.example.com/missions/weekly-network-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_WEEKLY_NETWORKING'), 'Contacta con 5 profesionales', 'COMPLETE_ACTIONS', 'professional_contact', 5, 0, false, 1, 'Mantén vivo tu network'),
((SELECT id FROM missions WHERE code = 'MISSION_WEEKLY_NETWORKING'), 'Asiste a 1 evento de networking', 'ATTEND_EVENT', 'networking_event', 1, 0, false, 2, 'Amplía tu círculo');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES ((SELECT id FROM missions WHERE code = 'MISSION_WEEKLY_NETWORKING'), 'XP_JOBS', 'weekly_networking', 300, true);

-- =====================================================
-- MISIÓN OCULTA
-- =====================================================

INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_SECRET_ACHIEVEMENT', '🏆 Logro Secreto', 'Has descubierto una misión oculta. Completa objetivos especiales.', 'Los verdaderos héroes buscan desafíos donde nadie mira.', 'ONE_TIME', 'SPECIAL', 'EPIC', 18, 500, 200, false, NULL, true, false, NULL, NULL, 'https://cdn.example.com/missions/secret-icon.png', 'https://cdn.example.com/missions/secret-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_SECRET_ACHIEVEMENT'), 'Completa 10 misiones diarias seguidas', 'COMPLETE_ACTIONS', 'daily_streak', 10, 0, false, 1, 'La constancia tiene recompensas ocultas'),
((SELECT id FROM missions WHERE code = 'MISSION_SECRET_ACHIEVEMENT'), 'Asiste a 5 eventos diferentes', 'ATTEND_EVENT', 'unique_events', 5, 0, false, 2, 'Explora diferentes actividades');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, badge_code)
VALUES
((SELECT id FROM missions WHERE code = 'MISSION_SECRET_ACHIEVEMENT'), 'XP_JOBS', 'secret', 1000, true, NULL),
((SELECT id FROM missions WHERE code = 'MISSION_SECRET_ACHIEVEMENT'), 'BADGE', NULL, NULL, false, 'SECRET_SEEKER');

-- =====================================================
-- VERIFICACIÓN
-- =====================================================
SELECT 'Misiones totales: ' || COUNT(*) FROM missions;
SELECT 'Misiones por tipo: ' || type || ': ' || COUNT(*) FROM missions GROUP BY type;
SELECT 'Misiones por categoría: ' || category || ': ' || COUNT(*) FROM missions GROUP BY category;
SELECT 'Misiones por dificultad: ' || difficulty || ': ' || COUNT(*) FROM missions GROUP BY difficulty;
SELECT 'Mission Objectives totales: ' || COUNT(*) FROM mission_objectives;
SELECT 'Rewards totales: ' || COUNT(*) FROM rewards;