-- =====================================================
-- SCRIPT DE INSERCIÓN CORREGIDO CON VALORES REALES DE EnumAll
-- =====================================================

-- Limpiar tablas
DELETE FROM mission_unlock_codes;
DELETE FROM cmr_objective_progress;
DELETE FROM character_mission_records;
DELETE FROM character_event_records;
DELETE FROM mission_objectives;
DELETE FROM rewards;
DELETE FROM requirements;
DELETE FROM missions;
DELETE FROM events;
DELETE FROM mission_chains;

-- Resetear secuencias
ALTER SEQUENCE events_id_seq RESTART WITH 1;
ALTER SEQUENCE missions_id_seq RESTART WITH 1;
ALTER SEQUENCE mission_chains_id_seq RESTART WITH 1;
ALTER SEQUENCE rewards_id_seq RESTART WITH 1;
ALTER SEQUENCE requirements_id_seq RESTART WITH 1;
ALTER SEQUENCE mission_objectives_id_seq RESTART WITH 1;

-- =====================================================
-- EVENTOS (usando valores reales de EnumAll)
-- =====================================================

-- Valores válidos según EnumAll:
-- EventType: BONUS, PROMOTION, DEMOTION, CONFLICT, PROJECT_SUCCESS, PROJECT_FAILURE, BURNOUT, OFFER_FROM_RIVAL, MENTOR_LEAVES, TEAM_RESTRUCTURE, QUARTERLY_REVIEW
-- EventScope: GLOBAL, CITY, PERSONAL
-- EventStatus: SCHEDULED, IN_PROGRESS, COMPLETED, SKIPPED, FAILED, CANCELLED

-- EVENTO 1: BONUS - Bono de productividad
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_BONUS_001_PRODUCTIVITY', 'Bono de Productividad', 'Has superado tus objetivos este trimestre. ¡Recibe un bono extra!', 'El trabajo duro siempre tiene recompensa.', 'BONUS', 'PERSONAL', null, NOW(), NOW() + INTERVAL '7 days', false, null, 1, 60, 'SCHEDULED', true, 'https://cdn.example.com/events/bonus-icon.png', 'https://cdn.example.com/events/bonus-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary)
VALUES (1, 'MONEY', null, 5000, true);

-- EVENTO 2: PROMOTION - Ascenso laboral
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_PROMOTION_001_ASCENSO', 'Ascenso Laboral', 'Tu jefe ha reconocido tu talento. ¡Es hora de un ascenso!', 'Los grandes profesionales son recompensados.', 'PROMOTION', 'PERSONAL', null, NOW(), NOW() + INTERVAL '3 days', false, null, 1, 120, 'SCHEDULED', true, 'https://cdn.example.com/events/promotion-icon.png', 'https://cdn.example.com/events/promotion-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, title_granted)
VALUES
(2, 'XP_JOBS', 'promotion', 1000, true, null),
(2, 'TITLE', null, null, false, 'Senior');

-- EVENTO 3: CONFLICT - Conflicto con compañero
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_CONFLICT_001_COWORKER', 'Conflicto con Compañero', 'Un compañero no está colaborando. Debes resolver la situación.', 'Los conflictos laborales son inevitables. Cómo los manejes define tu carácter.', 'CONFLICT', 'PERSONAL', null, NOW(), NOW() + INTERVAL '2 days', false, null, 1, 90, 'IN_PROGRESS', true, 'https://cdn.example.com/events/conflict-icon.png', 'https://cdn.example.com/events/conflict-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary)
VALUES (3, 'STAT_BOOST', 'charisma', 3, true);

-- EVENTO 4: PROJECT_SUCCESS - Éxito de proyecto
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_PROJECT_SUCCESS_001', 'Éxito del Proyecto', 'El proyecto ha sido un éxito rotundo. Celebra con tu equipo.', 'Los proyectos exitosos construyen reputación.', 'PROJECT_SUCCESS', 'CITY', 'Madrid', NOW(), NOW() + INTERVAL '1 day', false, null, 50, 180, 'SCHEDULED', false, 'https://cdn.example.com/events/project-success-icon.png', 'https://cdn.example.com/events/project-success-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary, badge_code)
VALUES
(4, 'XP_JOBS', 'project_success', 800, true, null),
(4, 'BADGE', null, null, false, 'PROJECT_LEADER');

-- EVENTO 5: PROJECT_FAILURE - Fracaso de proyecto
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_PROJECT_FAILURE_001', 'Fracaso del Proyecto', 'El proyecto no ha salido como esperabas. Aprende de los errores.', 'El fracaso es el mejor maestro.', 'PROJECT_FAILURE', 'PERSONAL', null, NOW(), NOW() + INTERVAL '5 days', false, null, 1, 60, 'IN_PROGRESS', true, 'https://cdn.example.com/events/project-failure-icon.png', 'https://cdn.example.com/events/project-failure-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary)
VALUES (5, 'XP_JOBS', 'learning', 300, true);

-- EVENTO 6: BURNOUT - Agotamiento
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_BURNOUT_001', 'Señales de Agotamiento', 'Has estado trabajando demasiado. Necesitas descansar.', 'El burnout es real. Escucha a tu cuerpo y mente.', 'BURNOUT', 'PERSONAL', null, NOW(), NOW() + INTERVAL '14 days', false, null, 1, 10080, 'IN_PROGRESS', true, 'https://cdn.example.com/events/burnout-icon.png', 'https://cdn.example.com/events/burnout-banner.jpg', NOW());

INSERT INTO requirements (event_id, type, target_value, fail_message)
VALUES (6, 'MIN_XP_JOBS', 500, 'Solo afecta a trabajadores con experiencia');

-- EVENTO 7: OFFER_FROM_RIVAL - Oferta de la competencia
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_OFFER_RIVAL_001', 'Oferta de la Competencia', 'Una empresa rival te ofrece un puesto. ¿Aceptarás?', 'Las oportunidades llaman a la puerta cuando menos lo esperas.', 'OFFER_FROM_RIVAL', 'PERSONAL', null, NOW(), NOW() + INTERVAL '7 days', false, null, 1, 1440, 'SCHEDULED', true, 'https://cdn.example.com/events/offer-rival-icon.png', 'https://cdn.example.com/events/offer-rival-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary)
VALUES (7, 'MONEY', null, 15000, true);

INSERT INTO requirements (event_id, type, target_value, fail_message)
VALUES (7, 'MIN_XP_JOBS', 800, 'Necesitas experiencia para recibir ofertas');

-- EVENTO 8: MENTOR_LEAVES - El mentor se va
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_MENTOR_LEAVES_001', 'Tu Mentor se Marcha', 'Tu mentor ha aceptado un puesto en otra empresa. Estarás solo a partir de ahora.', 'Los mentores te guían, pero eventualmente debes caminar solo.', 'MENTOR_LEAVES', 'PERSONAL', null, NOW(), NOW() + INTERVAL '30 days', false, null, 1, 43200, 'SCHEDULED', true, 'https://cdn.example.com/events/mentor-leaves-icon.png', 'https://cdn.example.com/events/mentor-leaves-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary)
VALUES (8, 'STAT_BOOST', 'independence', 5, true);

-- EVENTO 9: TEAM_RESTRUCTURE - Reestructuración de equipo
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_TEAM_RESTRUCTURE_001', 'Reestructuración del Equipo', 'Tu departamento se reorganiza. Nuevos compañeros y responsabilidades.', 'El cambio es constante en el mundo laboral.', 'TEAM_RESTRUCTURE', 'CITY', 'Barcelona', NOW(), NOW() + INTERVAL '14 days', false, null, 30, 2880, 'IN_PROGRESS', false, 'https://cdn.example.com/events/team-restructure-icon.png', 'https://cdn.example.com/events/team-restructure-banner.jpg', NOW());

-- EVENTO 10: QUARTERLY_REVIEW - Revisión trimestral
INSERT INTO events (code, title, description, lore, type, scope, city, start_date, end_date, recurring, cron_expression, max_participants, duration_minutes, status, auto_trigger, icon_url, banner_url, created_at)
VALUES ('EVENT_QUARTERLY_REVIEW_001', 'Revisión Trimestral', 'Es hora de evaluar tu rendimiento del trimestre.', 'Las revisiones definen tu futuro en la empresa.', 'QUARTERLY_REVIEW', 'PERSONAL', null, NOW(), NOW() + INTERVAL '1 day', true, '0 0 9 1 1,4,7,10 ? *', 1, 120, 'SCHEDULED', true, 'https://cdn.example.com/events/quarterly-review-icon.png', 'https://cdn.example.com/events/quarterly-review-banner.jpg', NOW());

INSERT INTO rewards (event_id, type, target_key, value, is_primary)
VALUES (10, 'XP_JOBS', 'review', 400, true);

-- =====================================================
-- MISIONES (usando valores reales de EnumAll)
-- =====================================================

-- Valores válidos según EnumAll:
-- MissionCategory: STUDY, WORK, SOCIAL, ROMANCE, EXPLORATION, TRAINING, PERSONAL, SPECIAL
-- MissionType: ONE_TIME, DAILY, WEEKLY, MONTHLY, REPEATABLE, STORY
-- MissionDifficulty: EASY, MEDIUM, HARD, EPIC
-- MissionStatus: ASSIGNED, ACTIVE, PAUSED, COMPLETED, FAILED, ABANDONED, EXPIRED

-- MISIÓN 1: Primer día de trabajo
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_WORK_001_FIRST_DAY', 'Primer Día de Trabajo', 'Es tu primer día en el nuevo trabajo. Causa una buena impresión.', 'Todo gran viaje comienza con un primer paso.', 'ONE_TIME', 'WORK', 'EASY', 16, 0, 0, false, null, false, true, null, null, 'https://cdn.example.com/missions/work-first-day-icon.png', 'https://cdn.example.com/missions/work-first-day-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(1, 'Llega puntual a tu primer día', 'COMPLETE_ACTIONS', 'punctual_first_day', 1, 0, false, 1, 'La puntualidad es clave'),
(1, 'Preséntate a tus compañeros', 'COMPLETE_ACTIONS', 'introduce_yourself', 1, 0, false, 2, 'Sé amable y sonríe'),
(1, 'Completa la orientación inicial', 'COMPLETE_ACTIONS', 'orientation', 1, 0, false, 3, 'Presta atención a la información');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES
(1, 'XP_JOBS', 'first_day', 200, true),
(1, 'MONEY', null, 500, false);

INSERT INTO requirements (mission_id, type, target_value, fail_message)
VALUES (1, 'MIN_AGE', 16, 'Debes tener al menos 16 años para trabajar');

INSERT INTO mission_unlock_codes (mission_id, unlocks_mission_code)
VALUES (1, 'MISSION_WORK_002_PROBATION');

-- MISIÓN 2: Período de prueba
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_WORK_002_PROBATION', 'Período de Prueba', 'Demuestra tu valía durante los primeros 90 días.', 'El período de prueba es tu oportunidad para brillar.', 'ONE_TIME', 'WORK', 'MEDIUM', 16, 50, 0, false, null, false, false, null, null, 'https://cdn.example.com/missions/work-probation-icon.png', 'https://cdn.example.com/missions/work-probation-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(2, 'Completa 30 días de trabajo', 'COMPLETE_ACTIONS', 'work_day', 30, 0, false, 1, 'Asiste puntualmente cada día'),
(2, 'Recibe feedback positivo', 'COMPLETE_ACTIONS', 'positive_feedback', 3, 0, false, 2, 'Haz preguntas y muestra iniciativa');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted)
VALUES
(2, 'XP_JOBS', 'probation', 500, true, null),
(2, 'TITLE', null, null, false, 'Empleado Fijo');

INSERT INTO requirements (mission_id, type, target_key, fail_message)
VALUES (2, 'MISSION_COMPLETED', 'MISSION_WORK_001_FIRST_DAY', 'Debes completar el primer día primero');

INSERT INTO mission_unlock_codes (mission_id, unlocks_mission_code)
VALUES (2, 'MISSION_WORK_003_NETWORKING');

-- MISIÓN 3: Networking profesional
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_WORK_003_NETWORKING', 'Red de Contactos', 'Construye tu red profesional.', 'En los negocios, a quién conoces es tan importante como lo que sabes.', 'REPEATABLE', 'SOCIAL', 'MEDIUM', 18, 200, 0, true, 720, false, false, null, null, 'https://cdn.example.com/missions/networking-icon.png', 'https://cdn.example.com/missions/networking-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(3, 'Asiste a 3 eventos de networking', 'ATTEND_EVENT', 'networking_event', 3, 0, false, 1, 'Busca eventos profesionales'),
(3, 'Añade 10 contactos profesionales', 'COMPLETE_ACTIONS', 'professional_contact', 10, 0, false, 2, 'Conecta en LinkedIn');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES
(3, 'XP_JOBS', 'networking', 400, true),
(3, 'UNLOCK_MISSION', 'MISSION_WORK_004_MENTOR', null, false);

-- MISIÓN 4: Encontrar un mentor
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_WORK_004_MENTOR', 'Encuentra un Mentor', 'Busca la guía de un profesional experimentado.', 'Un buen mentor acelera tu crecimiento profesional.', 'ONE_TIME', 'WORK', 'HARD', 21, 400, 100, false, null, false, false, null, null, 'https://cdn.example.com/missions/mentor-icon.png', 'https://cdn.example.com/missions/mentor-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(4, 'Identifica 3 mentores potenciales', 'COMPLETE_ACTIONS', 'identify_mentor', 3, 0, false, 1, 'Busca profesionales que admires'),
(4, 'Solicita una reunión de mentoría', 'COMPLETE_ACTIONS', 'mentor_meeting', 1, 0, false, 2, 'Prepara un buen pitch');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, badge_code)
VALUES
(4, 'XP_JOBS', 'mentorship', 800, true, null),
(4, 'BADGE', null, null, false, 'MENTEE');

-- MISIÓN 5: Estudio diario
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_STUDY_001_DAILY', 'Estudio Diario', 'Dedica tiempo a estudiar cada día.', 'La constancia es la clave del éxito académico.', 'DAILY', 'STUDY', 'EASY', 14, 0, 0, true, 24, false, false, null, null, 'https://cdn.example.com/missions/study-daily-icon.png', 'https://cdn.example.com/missions/study-daily-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES (5, 'Estudia durante 2 horas', 'COMPLETE_ACTIONS', 'study_time', 120, 0, false, 1, 'Encuentra un lugar tranquilo');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES (5, 'XP_ACADEMY', 'daily_study', 100, true);

-- MISIÓN 6: Socializar
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_SOCIAL_001_ICE_BREAKER', 'Rompehielos', 'Supera tu timidez y conoce gente nueva.', 'Las mejores amistades comienzan con un simple hola.', 'ONE_TIME', 'SOCIAL', 'EASY', 14, 0, 0, false, null, false, true, null, null, 'https://cdn.example.com/missions/social-icebreaker-icon.png', 'https://cdn.example.com/missions/social-icebreaker-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(6, 'Inicia conversación con 5 desconocidos', 'COMPLETE_ACTIONS', 'start_conversation', 5, 0, false, 1, 'Un comentario sobre el tiempo funciona'),
(6, 'Haz 3 nuevos amigos', 'MAKE_FRIEND', 'new_friend', 3, 0, false, 2, 'Sé auténtico y muestra interés');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES (6, 'STAT_BOOST', 'charisma', 3, true);

-- MISIÓN 7: Romance
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_ROMANCE_001_FIRST_DATE', 'Primera Cita', 'Consigue una cita romántica.', 'El amor puede aparecer cuando menos lo esperas.', 'STORY', 'ROMANCE', 'MEDIUM', 16, 0, 0, true, 168, false, false, null, null, 'https://cdn.example.com/missions/romance-date-icon.png', 'https://cdn.example.com/missions/romance-date-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(7, 'Invita a alguien a salir', 'COMPLETE_ACTIONS', 'ask_out', 1, 0, false, 1, 'Sé respetuoso y directo'),
(7, 'Ten una cita exitosa', 'GET_PARTNER', 'first_date', 1, 0, false, 2, 'Sé tú mismo');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted)
VALUES
(7, 'RELATIONSHIP_BONUS', 'romance', 50, true, null),
(7, 'TITLE', null, null, false, 'Enamorado');

-- MISIÓN 8: Entrenamiento físico
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_TRAINING_001_FITNESS', 'Ponte en Forma', 'Mejora tu condición física.', 'Mente sana en cuerpo sano.', 'WEEKLY', 'TRAINING', 'MEDIUM', 15, 0, 0, true, 168, false, false, null, null, 'https://cdn.example.com/missions/fitness-icon.png', 'https://cdn.example.com/missions/fitness-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(8, 'Haz ejercicio 3 veces', 'COMPLETE_ACTIONS', 'workout', 3, 0, false, 1, '30 minutos cada sesión');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary)
VALUES (8, 'STAT_BOOST', 'endurance', 2, true);

-- MISIÓN 9: Exploración
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_EXPLORE_001_NEW_CITY', 'Explora tu Ciudad', 'Descubre lugares nuevos en tu ciudad.', 'A veces lo mejor está justo delante de nosotros.', 'ONE_TIME', 'EXPLORATION', 'EASY', 12, 0, 0, false, null, false, true, null, null, 'https://cdn.example.com/missions/explore-icon.png', 'https://cdn.example.com/missions/explore-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(9, 'Visita 5 lugares nuevos', 'COMPLETE_ACTIONS', 'visit_place', 5, 0, false, 1, 'Usa Google Maps para descubrir sitios');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, badge_code)
VALUES
(9, 'XP_ACADEMY', 'exploration', 200, true, null),
(9, 'BADGE', null, null, false, 'EXPLORER');

-- MISIÓN 10: Especial (Legendaria)
INSERT INTO missions (code, title, description, lore, type, category, difficulty, min_age, required_xp_jobs, required_xp_academy, repeatable, cooldown_hours, hidden, auto_accept, order_in_chain, chain_id, icon_url, banner_url, created_at)
VALUES ('MISSION_SPECIAL_001_LEGEND', 'El Camino del Maestro', 'Alcanza la maestría en tu profesión.', 'Solo unos pocos alcanzan la verdadera maestría.', 'STORY', 'SPECIAL', 'EPIC', 25, 2000, 1000, false, null, true, true, null, null, 'https://cdn.example.com/missions/legend-icon.png', 'https://cdn.example.com/missions/legend-banner.jpg', NOW());

INSERT INTO mission_objectives (mission_id, description, type, target_key, target_value, current_default, optional, order_index, hint)
VALUES
(10, 'Alcanza 3000 XP en Trabajos', 'REACH_XP', 'jobs', 3000, 0, false, 1, 'Años de dedicación'),
(10, 'Completa 10 proyectos importantes', 'COMPLETE_ACTIONS', 'major_project', 10, 0, false, 2, 'Lidera iniciativas clave'),
(10, 'Mentora a 5 personas', 'COMPLETE_ACTIONS', 'mentor_others', 5, 0, false, 3, 'Comparte tu conocimiento');

INSERT INTO rewards (mission_id, type, target_key, value, is_primary, title_granted, badge_code)
VALUES
(10, 'XP_JOBS', 'mastery', 5000, true, null, null),
(10, 'TITLE', null, null, false, 'Maestro', null),
(10, 'BADGE', null, null, false, null, 'LEGENDARY_MASTER');

-- Verificar inserciones
SELECT 'Events: ' || COUNT(*) FROM events;
SELECT 'Missions: ' || COUNT(*) FROM missions;
SELECT 'Rewards: ' || COUNT(*) FROM rewards;