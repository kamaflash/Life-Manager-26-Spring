-- ============================================
-- LIMPIAR TABLAS (PostgreSQL)
-- ============================================
TRUNCATE TABLE formation_exams CASCADE;
TRUNCATE TABLE formations CASCADE;
TRUNCATE TABLE scholarships CASCADE;
TRUNCATE TABLE formation_stat_rewards CASCADE;
TRUNCATE TABLE course_working_days CASCADE;
--TRUNCATE TABLE scholarshipapplication CASCADE;
--TRUNCATE TABLE character_training CASCADE;
--TRUNCATE TABLE character_exams CASCADE;

-- Reiniciar secuencias
ALTER SEQUENCE IF EXISTS formation_exams_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS formations_id_seq RESTART WITH 5;
ALTER SEQUENCE IF EXISTS scholarships_id_seq RESTART WITH 1;

-- ============================================
-- 1. FORMATIONS (cursos de formación)
-- ============================================
INSERT INTO formations (id, code, name, description, category, type, difficulty, min_education_level, min_academic_level, min_academic_xp, max_academic_xp, duration_hours, cost, effort, academic_xp_reward, repeatable, active, level, locked, start_time, end_time) VALUES
(5, 'JAVA_BASIC_01', 'Introducción a Java', 'Curso básico de programación en Java, orientado a principiantes.', 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 40, 150, 50, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(6, 'WEB_DEV_01', 'Desarrollo Web Frontend', 'Aprende HTML, CSS y JavaScript para crear páginas web interactivas.', 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 50, 180, 60, 60, false, true, 1, false, '08:00:00', '14:00:00'),
(7, 'DATA_SCI_01', 'Fundamentos de Ciencia de Datos', 'Introducción al análisis de datos y conceptos básicos de Machine Learning.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'university', 0, 100, 300, 60, 250, 80, 100, false, true, 1, false, '08:00:00', '14:00:00'),
(8, 'CYBERSEC_01', 'Seguridad Informática Básica', 'Conceptos esenciales de ciberseguridad y protección de sistemas.', 'TECHNOLOGY', 'COURSE', 'BASIC', 'secondary', 0, 0, 100, 35, 120, 40, 40, false, true, 1, false, '08:00:00', '14:00:00'),
(9, 'PYTHON_BASIC_01', 'Introducción a Python', 'Aprende los fundamentos de Python y programación orientada a objetos.', 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 45, 160, 50, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(10, 'FRONTEND_02', 'Frameworks Frontend (React y Angular)', 'Aprende a crear aplicaciones web modernas usando React y Angular.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'highschool', 50, 50, 200, 60, 220, 70, 80, false, true, 1, false, '08:00:00', '14:00:00'),
(11, 'DEVOPS_01', 'Fundamentos de DevOps', 'Conceptos de integración y despliegue continuo, y gestión de entornos.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'technical', 50, 50, 200, 50, 200, 70, 75, false, true, 1, false, '08:00:00', '14:00:00'),
(12, 'CLOUD_01', 'Introducción a la Nube', 'Aprende conceptos de cloud computing, AWS, Azure y Google Cloud.', 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 40, 180, 50, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(13, 'AI_INTRO_01', 'Inteligencia Artificial Básica', 'Curso introductorio a IA y Machine Learning con ejemplos prácticos.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'university', 100, 100, 300, 55, 250, 80, 100, false, true, 1, false, '08:00:00', '14:00:00'),
(14, 'JAVA_ADV_01', 'Programación Avanzada en Java', 'Aprende conceptos avanzados de Java, patrones de diseño y buenas prácticas.', 'TECHNOLOGY', 'COURSE', 'ADVANCED', 'technical', 100, 100, 300, 60, 280, 90, 120, false, true, 1, false, '08:00:00', '14:00:00'),
(15, 'WEB_SECURITY_01', 'Seguridad Web y Ciberseguridad', 'Conoce las técnicas de seguridad web y protección frente a ataques informáticos.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'highschool', 50, 50, 200, 45, 200, 70, 75, false, true, 1, false, '08:00:00', '14:00:00'),
(16, 'DATA_ANALYTICS_01', 'Analítica de Datos y Big Data', 'Aprende a analizar datos, crear dashboards y comprender Big Data.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'technical', 60, 50, 250, 50, 220, 75, 85, false, true, 1, false, '08:00:00', '14:00:00'),
(17, 'MOBILE_DEV_01', 'Desarrollo de Aplicaciones Móviles', 'Crea apps móviles para iOS y Android con frameworks modernos.', 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'technical', 50, 50, 200, 55, 240, 80, 90, false, true, 1, false, '08:00:00', '14:00:00'),
(18, 'CYBER_PHYSICS_01', 'Robótica y Física Aplicada', 'Curso práctico sobre robótica, automatización y principios de física aplicada.', 'TECHNOLOGY', 'COURSE', 'ADVANCED', 'technical', 100, 100, 300, 70, 300, 100, 150, false, true, 1, false, '08:00:00', '14:00:00'),
(19, 'HEALTH_BASIC_01', 'Fundamentos de Salud Pública', 'Introducción a la salud pública, epidemiología y promoción de la salud.', 'HEALTH', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 40, 180, 50, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(20, 'NUTRITION_01', 'Nutrición y Dietética', 'Principios de nutrición, planificación de dietas y hábitos saludables.', 'HEALTH', 'COURSE', 'BASIC', 'highschool', 10, 0, 100, 35, 150, 40, 45, false, true, 1, false, '08:00:00', '14:00:00'),
(21, 'FIRST_AID_01', 'Primeros Auxilios y Emergencias', 'Aprende técnicas de primeros auxilios y manejo de situaciones de emergencia.', 'HEALTH', 'COURSE', 'INTERMEDIATE', 'secondary', 20, 10, 150, 30, 120, 60, 60, false, true, 1, false, '08:00:00', '14:00:00'),
(22, 'ANATOMY_01', 'Anatomía Humana', 'Estudio del cuerpo humano, sistemas y funciones principales.', 'HEALTH', 'COURSE', 'INTERMEDIATE', 'secondary', 30, 20, 200, 50, 250, 80, 90, false, true, 1, false, '08:00:00', '14:00:00'),
(23, 'PHYSIOTHERAPY_01', 'Introducción a la Fisioterapia', 'Conceptos básicos de fisioterapia y técnicas de rehabilitación.', 'HEALTH', 'COURSE', 'BASIC', 'secondary', 20, 10, 150, 40, 200, 60, 60, false, true, 1, false, '08:00:00', '14:00:00'),
(24, 'MENTAL_HEALTH_01', 'Salud Mental y Bienestar', 'Aprende técnicas para promover el bienestar mental y emocional.', 'HEALTH', 'COURSE', 'BASIC', 'highschool', 10, 0, 100, 30, 150, 40, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(25, 'PHARMACY_01', 'Fundamentos de Farmacología', 'Principios básicos de farmacología y administración de medicamentos.', 'HEALTH', 'COURSE', 'INTERMEDIATE', 'technical', 40, 30, 200, 45, 220, 70, 80, false, true, 1, false, '08:00:00', '14:00:00'),
(26, 'DIAGNOSTICS_01', 'Técnicas de Diagnóstico Médico', 'Aprende sobre pruebas médicas, diagnóstico y procedimientos clínicos básicos.', 'HEALTH', 'COURSE', 'ADVANCED', 'university', 70, 50, 300, 60, 300, 100, 120, false, true, 1, false, '08:00:00', '14:00:00'),
(27, 'NURSING_01', 'Cuidado y Enfermería', 'Aprende los principios básicos de enfermería y cuidado del paciente.', 'HEALTH', 'COURSE', 'INTERMEDIATE', 'secondary', 30, 20, 200, 50, 240, 80, 90, false, true, 1, false, '08:00:00', '14:00:00'),
(28, 'BIOMED_01', 'Introducción a la Biomedicina', 'Curso sobre fundamentos de biomedicina y tecnología aplicada a la salud.', 'HEALTH', 'COURSE', 'ADVANCED', 'technical', 50, 40, 250, 55, 280, 90, 110, false, true, 1, false, '08:00:00', '14:00:00'),
(29, 'CONSTRUCTION_BASIC_01', 'Introducción a la Construcción', 'Conceptos básicos de construcción, materiales y técnicas iniciales.', 'CONSTRUCTION', 'COURSE', 'BASIC', 'secondary', 0, 0, 100, 40, 150, 50, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(30, 'MASONRY_01', 'Albañilería y Estructuras', 'Aprende técnicas de albañilería, construcción de muros y estructuras básicas.', 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'secondary', 10, 0, 120, 45, 180, 60, 60, false, true, 1, false, '08:00:00', '14:00:00'),
(31, 'PLUMBING_01', 'Fontanería y Sistemas Hidráulicos', 'Instalación, mantenimiento y reparación de sistemas de fontanería.', 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'technical', 20, 10, 150, 35, 200, 70, 70, false, true, 1, false, '08:00:00', '14:00:00'),
(32, 'ELECTRICAL_01', 'Instalaciones Eléctricas', 'Principios y técnicas para la instalación y mantenimiento eléctrico.', 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'technical', 25, 10, 160, 40, 220, 70, 75, false, true, 1, false, '08:00:00', '14:00:00'),
(33, 'CARPENTRY_01', 'Carpintería Básica', 'Aprende técnicas de carpintería, herramientas y construcción de estructuras de madera.', 'CONSTRUCTION', 'COURSE', 'BASIC', 'secondary', 15, 0, 120, 30, 160, 50, 55, false, true, 1, false, '08:00:00', '14:00:00'),
(34, 'SAFETY_01', 'Seguridad en la Construcción', 'Normas y procedimientos para garantizar la seguridad en obras y proyectos.', 'CONSTRUCTION', 'COURSE', 'BASIC', 'secondary', 0, 0, 100, 25, 120, 40, 50, false, true, 1, false, '08:00:00', '14:00:00'),
(35, 'BLUEPRINT_01', 'Lectura de Planos y Diagramas', 'Interpretación de planos arquitectónicos y diagramas de construcción.', 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'secondary', 20, 10, 150, 30, 140, 50, 60, false, true, 1, false, '08:00:00', '14:00:00'),
(36, 'CONCRETE_01', 'Tecnología del Hormigón', 'Estudio de materiales, mezclas y técnicas de construcción con hormigón.', 'CONSTRUCTION', 'COURSE', 'ADVANCED', 'technical', 40, 20, 200, 50, 250, 80, 100, false, true, 1, false, '08:00:00', '14:00:00'),
(37, 'HVAC_01', 'Instalación de Sistemas HVAC', 'Aprende a instalar y mantener sistemas de calefacción, ventilación y aire acondicionado.', 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'technical', 35, 20, 180, 40, 220, 70, 80, false, true, 1, false, '08:00:00', '14:00:00'),
(38, 'PROJECT_MANAGEMENT_01', 'Gestión de Proyectos de Construcción', 'Aprende a planificar, coordinar y supervisar proyectos de construcción.', 'CONSTRUCTION', 'COURSE', 'ADVANCED', 'technical', 50, 30, 250, 60, 300, 90, 120, false, true, 1, false, '08:00:00', '14:00:00'),
(39, 'BUSINESS_BASIC_01', 'Introducción a la Administración', 'Fundamentos de administración y gestión de empresas.', 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 50, 10, 30, true, true, 1, false, '08:00:00', '14:00:00'),
(40, 'MARKETING_01', 'Marketing Digital', 'Conceptos y herramientas de marketing online y redes sociales.', 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80, 20, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(41, 'FINANCE_01', 'Finanzas Personales', 'Aprende a gestionar tus ingresos, gastos y ahorros.', 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 60, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(42, 'LEADERSHIP_01', 'Liderazgo y Gestión de Equipos', 'Desarrolla habilidades de liderazgo y gestión efectiva de equipos.', 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 30, 120, 35, 100, 25, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(43, 'ENTREPRENEUR_01', 'Emprendimiento y Startups', 'Cómo crear y gestionar tu propio negocio desde cero.', 'BUSINESS', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 50, 150, 40, 100, true, true, 1, false, '08:00:00', '14:00:00'),
(44, 'ECONOMICS_01', 'Fundamentos de Economía', 'Principios básicos de economía y su aplicación en los negocios.', 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 60, 20, 50, 10, 30, true, true, 1, false, '08:00:00', '14:00:00'),
(45, 'PROJECT_MGMT_01', 'Gestión de Proyectos', 'Técnicas y herramientas para planificar y ejecutar proyectos con éxito.', 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 120, 30, 70, true, true, 1, false, '08:00:00', '14:00:00'),
(46, 'SALES_01', 'Técnicas de Venta', 'Aprende a vender productos y servicios de manera efectiva.', 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 60, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(47, 'NEGOTIATION_01', 'Negociación y Resolución de Conflictos', 'Desarrolla habilidades de negociación y manejo de conflictos.', 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 90, 20, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(48, 'STRATEGY_01', 'Estrategia Empresarial', 'Aprende a formular estrategias efectivas para tu empresa.', 'BUSINESS', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 50, 150, 40, 100, true, true, 1, false, '08:00:00', '14:00:00'),
(49, 'GRAPHIC_DESIGN_01', 'Diseño Gráfico Básico', 'Fundamentos del diseño gráfico, teoría del color y composición visual.', 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 60, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(50, 'PHOTOGRAPHY_01', 'Fotografía Digital', 'Técnicas de fotografía, iluminación y composición para principiantes.', 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 50, 12, 35, true, true, 1, false, '08:00:00', '14:00:00'),
(51, 'ILLUSTRATION_01', 'Ilustración Digital', 'Técnicas de dibujo e ilustración utilizando herramientas digitales.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80, 20, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(52, 'UIUX_01', 'Diseño de Interfaces y Experiencia de Usuario', 'Principios de UX/UI para aplicaciones web y móviles.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 100, 25, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(53, 'MUSIC_THEORY_01', 'Teoría Musical Básica', 'Fundamentos de teoría musical, notas, escalas y ritmo.', 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 50, 12, 35, true, true, 1, false, '08:00:00', '14:00:00'),
(54, 'PHOTOGRAPHY_ADV_01', 'Fotografía Avanzada', 'Técnicas avanzadas de fotografía y edición profesional.', 'CREATIVE', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 40, 120, 30, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(55, 'VIDEO_EDITING_01', 'Edición de Video', 'Aprende a editar videos profesionales para proyectos creativos.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80, 20, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(56, 'CREATIVE_WRITING_01', 'Escritura Creativa', 'Desarrolla técnicas de narrativa y redacción literaria.', 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(57, 'ANIMATION_01', 'Animación Digital', 'Fundamentos de animación 2D y 3D para proyectos creativos.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 100, 25, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(58, 'MUSIC_PRODUCTION_01', 'Producción Musical', 'Aprende a producir música usando software profesional.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80, 20, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(59, 'WEB_DESIGN_01', 'Diseño Web Creativo', 'Aprende diseño web con enfoque creativo y UX/UI.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 100, 25, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(60, 'SOCIAL_MEDIA_01', 'Gestión de Redes Sociales', 'Aprende a gestionar redes sociales y crear contenido creativo.', 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(61, 'INTERIOR_DESIGN_01', 'Diseño de Interiores', 'Principios y técnicas de diseño de espacios interiores.', 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80, 20, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(62, 'FASHION_DESIGN_01', 'Diseño de Moda', 'Conceptos y técnicas para crear colecciones de moda.', 'CREATIVE', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 40, 120, 30, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(63, 'VIDEO_GAME_DESIGN_01', 'Diseño de Videojuegos', 'Aprende diseño creativo y mecánicas para videojuegos.', 'CREATIVE', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 50, 150, 40, 100, true, true, 1, false, '08:00:00', '14:00:00'),
(64, 'EDUCATION_BASIC_01', 'Fundamentos de Educación', 'Introducción a la pedagogía y métodos educativos para principiantes.', 'SOCIAL', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(65, 'CHILD_PSYC_01', 'Psicología Infantil', 'Principios básicos de la psicología infantil y desarrollo cognitivo.', 'SOCIAL', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 60, 18, 45, true, true, 1, false, '08:00:00', '14:00:00'),
(66, 'SOCIAL_WORK_01', 'Trabajo Social Comunitario', 'Introducción al trabajo social y estrategias para comunidades.', 'SOCIAL', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 55, 17, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(67, 'PSYCHOLOGY_ADV_01', 'Psicología Avanzada', 'Conceptos avanzados de psicología y análisis de comportamiento humano.', 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 120, 35, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(68, 'EDUCATION_TECH_01', 'Tecnología Educativa', 'Uso de herramientas digitales para la enseñanza y aprendizaje.', 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 80, 22, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(69, 'COUNSELING_01', 'Terapia y Counseling', 'Técnicas de counseling y acompañamiento emocional.', 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 90, 25, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(70, 'SPECIAL_EDU_01', 'Educación Especial', 'Fundamentos y técnicas de educación para estudiantes con necesidades especiales.', 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 70, 18, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(71, 'SOCIAL_POLICIES_01', 'Políticas Sociales', 'Análisis de políticas públicas y programas sociales.', 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 120, 35, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(72, 'COUNSELING_ADV_01', 'Counseling Avanzado', 'Métodos avanzados de acompañamiento psicológico y coaching.', 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 45, 130, 38, 90, true, true, 1, false, '08:00:00', '14:00:00'),
(73, 'YOUTH_WORK_01', 'Trabajo con Jóvenes', 'Programas y técnicas para el desarrollo de jóvenes en riesgo.', 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 85, 22, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(74, 'COMMUNITY_DEVELOPMENT_01', 'Desarrollo Comunitario', 'Técnicas de planificación y gestión de proyectos comunitarios.', 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 75, 18, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(75, 'EDUCATION_POLICY_01', 'Políticas Educativas', 'Estudio de políticas y regulaciones del sistema educativo.', 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 45, 130, 35, 90, true, true, 1, false, '08:00:00', '14:00:00'),
(76, 'PSYCHOLOGY_COMMUNITY_01', 'Psicología Comunitaria', 'Aplicación de técnicas psicológicas para mejorar la calidad de vida de comunidades.', 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 150, 40, 100, true, true, 1, false, '08:00:00', '14:00:00'),
(77, 'BIOLOGY_BASIC_01', 'Biología General', 'Introducción a la biología, estructuras celulares y funciones básicas.', 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(78, 'CHEMISTRY_BASIC_01', 'Química General', 'Principios fundamentales de química, elementos y reacciones básicas.', 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(79, 'PHYSICS_BASIC_01', 'Física General', 'Fundamentos de la física clásica y principios mecánicos.', 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(80, 'GENETICS_01', 'Genética Básica', 'Introducción a los genes, herencia y variación biológica.', 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 80, 20, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(81, 'MICROBIOLOGY_01', 'Microbiología', 'Estudio de microorganismos y su papel en la vida y la industria.', 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 85, 22, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(82, 'ORGANIC_CHEMISTRY_01', 'Química Orgánica', 'Principios de química orgánica y reacciones de compuestos carbonados.', 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 120, 35, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(83, 'BIOCHEMISTRY_01', 'Bioquímica', 'Estudio de procesos químicos en organismos vivos.', 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 130, 38, 85, true, true, 1, false, '08:00:00', '14:00:00'),
(84, 'ENVIRONMENTAL_SCIENCE_01', 'Ciencias Ambientales', 'Estudio del medio ambiente, ecosistemas y sostenibilidad.', 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 90, 25, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(85, 'RESEARCH_METHODS_01', 'Métodos de Investigación', 'Técnicas y metodologías para la investigación científica.', 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 75, 20, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(86, 'GENETIC_ENGINEERING_01', 'Ingeniería Genética', 'Técnicas de manipulación genética y biotecnología aplicada.', 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 140, 40, 95, true, true, 1, false, '08:00:00', '14:00:00'),
(87, 'CHEM_ANALYSIS_01', 'Análisis Químico', 'Técnicas de laboratorio para análisis de sustancias químicas.', 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 45, 125, 38, 90, true, true, 1, false, '08:00:00', '14:00:00'),
(88, 'ECOLOGY_01', 'Ecología', 'Estudio de los ecosistemas, biodiversidad y relaciones ambientales.', 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 80, 20, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(89, 'LAB_SAFETY_01', 'Seguridad en Laboratorio', 'Normas y procedimientos para un trabajo seguro en laboratorios científicos.', 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 40, 12, 35, true, true, 1, false, '08:00:00', '14:00:00'),
(90, 'CULINARY_BASICS_01', 'Cocina Básica', 'Introducción a técnicas culinarias fundamentales y preparación de platos sencillos.', 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 30, 50, 15, 40, true, true, 1, false, '08:00:00', '14:00:00'),
(91, 'PASTRY_BASICS_01', 'Repostería Básica', 'Técnicas iniciales de repostería, bizcochos, galletas y postres simples.', 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 25, 45, 12, 35, true, true, 1, false, '08:00:00', '14:00:00'),
(92, 'INTERMEDIATE_CULINARY_01', 'Cocina Intermedia', 'Preparación de platos más complejos y técnicas culinarias intermedias.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 40, 80, 20, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(93, 'FOOD_SAFETY_01', 'Higiene y Seguridad Alimentaria', 'Normas básicas de higiene y seguridad en cocina y manipulación de alimentos.', 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 20, 40, 10, 35, true, true, 1, false, '08:00:00', '14:00:00'),
(94, 'WORLD_CUISINE_01', 'Cocina Internacional', 'Exploración de recetas y técnicas culinarias de distintas culturas.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 45, 90, 22, 60, true, true, 1, false, '08:00:00', '14:00:00'),
(95, 'RESTAURANT_MANAGEMENT_01', 'Gestión de Restaurantes', 'Administración, planificación y organización de un restaurante.', 'HOSPITALITY', 'COURSE', 'ADVANCED', 'secondary', 2, 50, 200, 50, 120, 35, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(96, 'BARISTA_SKILLS_01', 'Técnicas de Barista', 'Preparación profesional de café y bebidas de especialidad.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 30, 60, 18, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(97, 'EVENT_PLANNING_01', 'Planificación de Eventos', 'Organización de eventos, logística y atención al cliente.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'secondary', 1, 20, 100, 35, 75, 20, 55, true, true, 1, false, '08:00:00', '14:00:00'),
(98, 'TOURISM_MANAGEMENT_01', 'Gestión Turística', 'Principios de administración en empresas turísticas y hoteles.', 'HOSPITALITY', 'COURSE', 'ADVANCED', 'secondary', 2, 50, 200, 50, 120, 35, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(99, 'FOOD_PRESENTATION_01', 'Presentación de Alimentos', 'Técnicas de emplatado y presentación profesional de platos.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 25, 55, 15, 45, true, true, 1, false, '08:00:00', '14:00:00'),
(100, 'HOTEL_OPERATIONS_01', 'Operaciones Hoteleras', 'Gestión diaria de hoteles, atención al cliente y logística.', 'HOSPITALITY', 'COURSE', 'ADVANCED', 'secondary', 2, 50, 200, 50, 120, 35, 80, true, true, 1, false, '08:00:00', '14:00:00'),
(101, 'FOOD_COST_CONTROL_01', 'Control de Costes de Alimentos', 'Gestión de inventario y control de costes en cocina y restaurantes.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'secondary', 1, 20, 100, 30, 60, 18, 50, true, true, 1, false, '08:00:00', '14:00:00'),
(102, 'HOSPITALITY_CUSTOMER_SERVICE_01', 'Atención al Cliente en Hostelería', 'Técnicas de atención al cliente y servicio profesional en restaurantes y hoteles.', 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 20, 40, 12, 35, true, true, 1, false, '08:00:00', '14:00:00'),
(103, 'COCKTAILS_01', 'Preparación de Cócteles', 'Técnicas para preparar y presentar cócteles profesionales.', 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 25, 55, 15, 45, true, true, 1, false, '08:00:00', '14:00:00'),
(104, 'FORMACION_PROFESIONAL_01', 'Formación Profesional', 'Curso de formación profesional orientado a habilidades prácticas y desarrollo académico.', 'EDUCATION', 'VOCATIONAL_TRAINING', 'BASIC', 'technical', 0, 0, 100, 2000, 0, 50, 180, false, true, 1, false, '08:00:00', '14:00:00'),
(105, 'HIGH_SCHOOL_01', 'Estudios Secundaria', 'Estudiando Bachillerato', 'EDUCATION', 'HIGH_SCHOOL', 'BASIC', 'none', 0, 0, 100, 2000, 0, 30, 180, false, true, 1, false, '08:00:00', '14:00:00');

-- ============================================
-- 2. FORMATION ALLOWED CAREERS
-- ============================================
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(5, 0), (5, 6),   -- TECHNOLOGY=0, SCIENCE=6
(6, 0), (6, 4),   -- TECHNOLOGY=0, CREATIVE=4
(7, 0), (7, 6),   -- TECHNOLOGY=0, SCIENCE=6
(8, 0), (8, 3),   -- TECHNOLOGY=0, BUSINESS=3
(9, 0), (9, 6),   -- TECHNOLOGY=0, SCIENCE=6
(10, 0), (10, 4), -- TECHNOLOGY=0, CREATIVE=4
(11, 0), (11, 3), -- TECHNOLOGY=0, BUSINESS=3
(12, 0), (12, 3), -- TECHNOLOGY=0, BUSINESS=3
(13, 0), (13, 6), -- TECHNOLOGY=0, SCIENCE=6
(14, 0), (14, 6), -- TECHNOLOGY=0, SCIENCE=6
(15, 0), (15, 3), -- TECHNOLOGY=0, BUSINESS=3
(16, 0), (16, 3), (16, 6), -- TECHNOLOGY=0, BUSINESS=3, SCIENCE=6
(17, 0), (17, 4), -- TECHNOLOGY=0, CREATIVE=4
(18, 0), (18, 6), -- TECHNOLOGY=0, SCIENCE=6
(19, 1), (19, 9), -- HEALTH=1, EDUCATION=9
(20, 1), (20, 9), -- HEALTH=1, EDUCATION=9
(21, 1), (21, 10), -- HEALTH=1, SPORTS=10
(22, 1), (22, 6), -- HEALTH=1, SCIENCE=6
(23, 1), (23, 10), -- HEALTH=1, SPORTS=10
(24, 1), (24, 9), (24, 5), -- HEALTH=1, EDUCATION=9, SOCIAL=5
(25, 1), (25, 6), -- HEALTH=1, SCIENCE=6
(26, 1), (26, 6), -- HEALTH=1, SCIENCE=6
(27, 1), (27, 5), -- HEALTH=1, SOCIAL=5
(28, 1), (28, 6), (28, 0), -- HEALTH=1, SCIENCE=6, TECHNOLOGY=0
(29, 2), (29, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(30, 2), -- CONSTRUCTION=2
(31, 2), (31, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(32, 2), (32, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(33, 2), (33, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(34, 2), (34, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(35, 2), (35, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(36, 2), -- CONSTRUCTION=2
(37, 2), (37, 0), -- CONSTRUCTION=2, TECHNOLOGY=0
(38, 2), (38, 3), -- CONSTRUCTION=2, BUSINESS=3
(39, 3), (39, 11), -- BUSINESS=3, OTHER=11
(40, 3), -- BUSINESS=3
(41, 3), (41, 11), -- BUSINESS=3, OTHER=11
(42, 3), (42, 11), -- BUSINESS=3, OTHER=11
(43, 3), (43, 11), -- BUSINESS=3, OTHER=11
(44, 3), (44, 11), -- BUSINESS=3, OTHER=11
(45, 3), (45, 11), -- BUSINESS=3, OTHER=11
(46, 3), (46, 11), -- BUSINESS=3, OTHER=11
(47, 3), (47, 11), -- BUSINESS=3, OTHER=11
(48, 3), (48, 11), -- BUSINESS=3, OTHER=11
(49, 4), (49, 8), -- CREATIVE=4, ARTS=8
(50, 4), (50, 8), -- CREATIVE=4, ARTS=8
(51, 4), (51, 8), -- CREATIVE=4, ARTS=8
(52, 4), (52, 0), -- CREATIVE=4, TECHNOLOGY=0
(53, 4), (53, 8), (53, 11), -- CREATIVE=4, ARTS=8, OTHER=11
(54, 4), (54, 8), -- CREATIVE=4, ARTS=8
(55, 4), (55, 8), (55, 11), -- CREATIVE=4, ARTS=8, OTHER=11
(56, 4), (56, 8), (56, 11), -- CREATIVE=4, ARTS=8, OTHER=11
(57, 4), (57, 8), (57, 11), -- CREATIVE=4, ARTS=8, OTHER=11
(58, 4), (58, 11), (58, 8), -- CREATIVE=4, OTHER=11, ARTS=8
(59, 4), (59, 0), -- CREATIVE=4, TECHNOLOGY=0
(60, 4), (60, 11), -- CREATIVE=4, OTHER=11
(61, 4), (61, 8), -- CREATIVE=4, ARTS=8
(62, 4), (62, 8), (62, 11), -- CREATIVE=4, ARTS=8, OTHER=11
(63, 4), (63, 0), (63, 8), -- CREATIVE=4, TECHNOLOGY=0, ARTS=8
(64, 5), (64, 9), -- SOCIAL=5, EDUCATION=9
(65, 5), (65, 11), -- SOCIAL=5, OTHER=11
(66, 5), (66, 9), -- SOCIAL=5, EDUCATION=9
(67, 5), (67, 11), -- SOCIAL=5, OTHER=11
(68, 5), (68, 9), (68, 0), -- SOCIAL=5, EDUCATION=9, TECHNOLOGY=0
(69, 5), (69, 11), -- SOCIAL=5, OTHER=11
(70, 5), (70, 9), -- SOCIAL=5, EDUCATION=9
(71, 5), (71, 9), (71, 11), -- SOCIAL=5, EDUCATION=9, OTHER=11
(72, 5), (72, 11), -- SOCIAL=5, OTHER=11
(73, 5), (73, 9), (73, 11), -- SOCIAL=5, EDUCATION=9, OTHER=11
(74, 5), (74, 11), -- SOCIAL=5, OTHER=11
(75, 5), (75, 9), (75, 11), -- SOCIAL=5, EDUCATION=9, OTHER=11
(76, 5), (76, 11), (76, 11), -- SOCIAL=5, OTHER=11, OTHER=11
(77, 6), (77, 9), -- SCIENCE=6, EDUCATION=9
(78, 6), (78, 9), -- SCIENCE=6, EDUCATION=9
(79, 6), (79, 9), -- SCIENCE=6, EDUCATION=9
(80, 6), (80, 11), -- SCIENCE=6, OTHER=11
(81, 6), (81, 11), -- SCIENCE=6, OTHER=11
(82, 6), (82, 11), -- SCIENCE=6, OTHER=11
(83, 6), (83, 11), (83, 11), -- SCIENCE=6, OTHER=11, OTHER=11
(84, 6), (84, 11), -- SCIENCE=6, OTHER=11
(85, 6), (85, 9), -- SCIENCE=6, EDUCATION=9
(86, 6), (86, 11), -- SCIENCE=6, OTHER=11
(87, 6), (87, 11), -- SCIENCE=6, OTHER=11
(88, 6), (88, 11), -- SCIENCE=6, OTHER=11
(89, 6), (89, 11), (89, 11), -- SCIENCE=6, OTHER=11, OTHER=11
(90, 7), (90, 11), -- HOSPITALITY=7, OTHER=11
(91, 7), (91, 11), -- HOSPITALITY=7, OTHER=11
(92, 7), (92, 11), -- HOSPITALITY=7, OTHER=11
(93, 7), (93, 11), -- HOSPITALITY=7, OTHER=11
(94, 7), (94, 11), -- HOSPITALITY=7, OTHER=11
(95, 7), (95, 3), -- HOSPITALITY=7, BUSINESS=3
(96, 7), (96, 11), -- HOSPITALITY=7, OTHER=11
(97, 7), (97, 3), (97, 11), -- HOSPITALITY=7, BUSINESS=3, OTHER=11
(98, 7), (98, 11), (98, 3), -- HOSPITALITY=7, OTHER=11, BUSINESS=3
(99, 7), (99, 11), -- HOSPITALITY=7, OTHER=11
(100, 7), (100, 3), (100, 11), -- HOSPITALITY=7, BUSINESS=3, OTHER=11
(101, 7), (101, 11), (101, 3), -- HOSPITALITY=7, OTHER=11, BUSINESS=3
(102, 7), (102, 11), -- HOSPITALITY=7, OTHER=11
(103, 7), (103, 11), -- HOSPITALITY=7, OTHER=11
(104, 0), (104, 6), (104, 11), -- TECHNOLOGY=0, SCIENCE=6, OTHER=11
(105, 11); -- OTHER=11

-- ============================================
-- 3. FORMATION SKILLS UNLOCKED
-- ============================================
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(5, 'basic_programming'), (5, 'logic_thinking'),
(6, 'web_design'), (6, 'frontend_development'),
(7, 'data_analysis'), (7, 'statistics'), (7, 'python_basics'),
(8, 'cybersecurity_basics'), (8, 'network_safety'),
(9, 'basic_programming'), (9, 'python_basics'),
(10, 'frontend_development'), (10, 'react'), (10, 'angular'),
(11, 'devops_basics'), (11, 'ci_cd'), (11, 'automation'),
(12, 'cloud_basics'), (12, 'aws'), (12, 'azure'), (12, 'google_cloud'),
(13, 'ai_basics'), (13, 'machine_learning'), (13, 'python_advanced'),
(14, 'java_advanced'), (14, 'object_oriented_design'),
(15, 'web_security'), (15, 'cybersecurity_basics'),
(16, 'data_analysis'), (16, 'big_data'), (16, 'sql'),
(17, 'mobile_development'), (17, 'react_native'), (17, 'flutter'),
(18, 'robotics'), (18, 'automation'), (18, 'physics_applied'),
(19, 'public_health_basics'), (19, 'health_awareness'),
(20, 'nutrition_basics'), (20, 'diet_planning'),
(21, 'first_aid'), (21, 'emergency_response'),
(22, 'human_anatomy'), (22, 'physiology_basics'),
(23, 'physiotherapy_basics'), (23, 'rehabilitation'),
(24, 'mental_health_awareness'), (24, 'stress_management'),
(25, 'pharmacology_basics'), (25, 'medication_management'),
(26, 'medical_diagnostics'), (26, 'clinical_procedures'),
(27, 'nursing_basics'), (27, 'patient_care'),
(28, 'biomedicine'), (28, 'lab_techniques'), (28, 'medical_research'),
(29, 'construction_basics'), (29, 'material_handling'),
(30, 'masonry'), (30, 'structural_basics'),
(31, 'plumbing'), (31, 'water_systems'),
(32, 'electrical_installation'), (32, 'wiring'),
(33, 'carpentry_basics'), (33, 'woodwork'),
(34, 'construction_safety'), (34, 'risk_management'),
(35, 'blueprint_reading'), (35, 'technical_drawing'),
(36, 'concrete_technology'), (36, 'structural_analysis'),
(37, 'hvac_installation'), (37, 'ventilation_systems'),
(38, 'project_management'), (38, 'construction_coordination'),
(39, 'basic_management'),
(40, 'digital_marketing'),
(41, 'financial_other'),
(42, 'team_leadership'),
(43, 'business_planning'), (43, 'startup_other'),
(44, 'economic_fundamentals'),
(45, 'project_other'),
(46, 'sales_skills'),
(47, 'negotiation'), (47, 'conflict_resolution'),
(48, 'business_strategy'),
(49, 'basic_graphic_design'),
(50, 'basic_photography'),
(51, 'digital_illustration'),
(52, 'uiux_design'),
(53, 'basic_music_theory'),
(54, 'advanced_photography'),
(55, 'video_editing'),
(56, 'creative_writing'),
(57, 'digital_animation'),
(58, 'music_production'),
(59, 'web_design'),
(60, 'social_media_management'),
(61, 'interior_design'),
(62, 'fashion_design'),
(63, 'game_design'), (63, 'game_mechanics'),
(64, 'basic_teaching'),
(65, 'child_psychology_basic'),
(66, 'community_work_basic'),
(67, 'advanced_psychology'),
(68, 'edtech_tools'),
(69, 'counseling_skills'),
(70, 'special_education'),
(71, 'social_policy_analysis'),
(72, 'advanced_counseling'),
(73, 'youth_programs'),
(74, 'community_development'),
(75, 'education_policy_analysis'),
(76, 'community_psychology'),
(77, 'basic_biology'),
(78, 'basic_chemistry'),
(79, 'basic_physics'),
(80, 'genetics_basics'),
(81, 'microbiology'),
(82, 'organic_chemistry'),
(83, 'biochemistry'),
(84, 'environmental_science'),
(85, 'research_methods'),
(86, 'genetic_engineering'),
(87, 'chemical_analysis'),
(88, 'ecology_basics'),
(89, 'lab_safety'),
(90, 'basic_cooking'),
(91, 'basic_pastry'),
(92, 'intermediate_cooking'),
(93, 'food_safety'),
(94, 'international_cuisine'),
(95, 'restaurant_management'),
(96, 'barista_skills'),
(97, 'event_planning'),
(98, 'tourism_management'),
(99, 'food_presentation'),
(100, 'hotel_operations'),
(101, 'food_cost_control'),
(102, 'customer_service'),
(103, 'cocktail_preparation'),
(104, 'basic_skills'), (104, 'logic_thinking'), (104, 'communication'),
(105, 'reading'), (105, 'writing'), (105, 'basic_math');

-- ============================================
-- 4. FORMATION STAT REWARDS
-- ============================================
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(5, 'intelligence', 5), (5, 'resilience', 1),
(6, 'intelligence', 4), (6, 'creativity', 3), (6, 'resilience', 1),
(7, 'intelligence', 8), (7, 'resilience', 2),
(8, 'intelligence', 5), (8, 'resilience', 2),
(9, 'intelligence', 5), (9, 'resilience', 1),
(10, 'intelligence', 6), (10, 'creativity', 3), (10, 'resilience', 2),
(11, 'intelligence', 6), (11, 'resilience', 3),
(12, 'intelligence', 4), (12, 'resilience', 1),
(13, 'intelligence', 8), (13, 'resilience', 2),
(14, 'intelligence', 8), (14, 'resilience', 3),
(15, 'intelligence', 6), (15, 'resilience', 3),
(16, 'intelligence', 7), (16, 'resilience', 2),
(17, 'intelligence', 6), (17, 'creativity', 3), (17, 'resilience', 2),
(18, 'intelligence', 7), (18, 'resilience', 5), (18, 'creativity', 2),
(19, 'intelligence', 4), (19, 'resilience', 2), (19, 'charisma', 1),
(20, 'intelligence', 3), (20, 'resilience', 3),
(21, 'resilience', 4), (21, 'charisma', 2),
(22, 'intelligence', 6), (22, 'resilience', 2),
(23, 'intelligence', 4), (23, 'resilience', 3), (23, 'charisma', 1),
(24, 'charisma', 4), (24, 'resilience', 3),
(25, 'intelligence', 6), (25, 'resilience', 2),
(26, 'intelligence', 8), (26, 'resilience', 3), (26, 'charisma', 1),
(27, 'intelligence', 5), (27, 'resilience', 4), (27, 'charisma', 2),
(28, 'intelligence', 7), (28, 'resilience', 3), (28, 'creativity', 2),
(29, 'resilience', 4), (29, 'intelligence', 2),
(30, 'resilience', 5), (30, 'intelligence', 2),
(31, 'resilience', 4), (31, 'intelligence', 3),
(32, 'intelligence', 5), (32, 'resilience', 3),
(33, 'resilience', 4), (33, 'creativity', 2), (33, 'intelligence', 1),
(34, 'resilience', 3), (34, 'intelligence', 2),
(35, 'intelligence', 5), (35, 'creativity', 2), (35, 'resilience', 1),
(36, 'intelligence', 6), (36, 'resilience', 4),
(37, 'intelligence', 5), (37, 'resilience', 3),
(38, 'intelligence', 6), (38, 'charisma', 4), (38, 'resilience', 3), (38, 'finances', 2),
(39, 'intelligence', 2), (39, 'charisma', 2), (39, 'finances', 2),
(40, 'charisma', 4), (40, 'creativity', 3), (40, 'finances', 2),
(41, 'intelligence', 2), (41, 'finances', 4),
(42, 'charisma', 5), (42, 'resilience', 3), (42, 'intelligence', 2),
(43, 'intelligence', 4), (43, 'charisma', 4), (43, 'creativity', 3), (43, 'finances', 3), (43, 'resilience', 3),
(44, 'intelligence', 3), (44, 'finances', 3),
(45, 'intelligence', 4), (45, 'charisma', 2), (45, 'resilience', 2), (45, 'finances', 2),
(46, 'charisma', 4), (46, 'resilience', 2), (46, 'finances', 2),
(47, 'charisma', 5), (47, 'intelligence', 2), (47, 'resilience', 2),
(48, 'intelligence', 6), (48, 'charisma', 3), (48, 'finances', 4), (48, 'resilience', 2),
(49, 'creativity', 5), (49, 'intelligence', 1),
(50, 'creativity', 4), (50, 'resilience', 1),
(51, 'creativity', 6), (51, 'intelligence', 1),
(52, 'creativity', 4), (52, 'intelligence', 3), (52, 'charisma', 1),
(53, 'creativity', 4), (53, 'intelligence', 1),
(54, 'creativity', 5), (54, 'resilience', 2), (54, 'intelligence', 1),
(55, 'creativity', 4), (55, 'intelligence', 2), (55, 'resilience', 1),
(56, 'creativity', 5), (56, 'charisma', 2),
(57, 'creativity', 6), (57, 'resilience', 2), (57, 'intelligence', 1),
(58, 'creativity', 5), (58, 'intelligence', 2),
(59, 'creativity', 4), (59, 'intelligence', 3),
(60, 'charisma', 4), (60, 'creativity', 3),
(61, 'creativity', 5), (61, 'intelligence', 1),
(62, 'creativity', 6), (62, 'charisma', 2), (62, 'resilience', 1),
(63, 'creativity', 6), (63, 'intelligence', 4), (63, 'resilience', 2),
(64, 'charisma', 3), (64, 'intelligence', 2), (64, 'resilience', 1),
(65, 'intelligence', 3), (65, 'charisma', 3), (65, 'resilience', 1),
(66, 'charisma', 4), (66, 'resilience', 2),
(67, 'intelligence', 6), (67, 'charisma', 4), (67, 'resilience', 2),
(68, 'intelligence', 4), (68, 'creativity', 3), (68, 'resilience', 1),
(69, 'charisma', 5), (69, 'resilience', 3), (69, 'intelligence', 2),
(70, 'charisma', 4), (70, 'resilience', 3), (70, 'intelligence', 2),
(71, 'intelligence', 5), (71, 'charisma', 3), (71, 'resilience', 2),
(72, 'charisma', 6), (72, 'resilience', 4), (72, 'intelligence', 3),
(73, 'charisma', 4), (73, 'resilience', 3), (73, 'creativity', 1),
(74, 'charisma', 4), (74, 'resilience', 2), (74, 'intelligence', 2),
(75, 'intelligence', 5), (75, 'charisma', 3), (75, 'resilience', 2),
(76, 'intelligence', 5), (76, 'charisma', 5), (76, 'resilience', 3),
(77, 'intelligence', 4), (77, 'resilience', 1),
(78, 'intelligence', 4), (78, 'resilience', 1),
(79, 'intelligence', 4), (79, 'resilience', 1),
(80, 'intelligence', 5), (80, 'resilience', 1),
(81, 'intelligence', 5), (81, 'resilience', 2),
(82, 'intelligence', 6), (82, 'resilience', 2),
(83, 'intelligence', 6), (83, 'resilience', 2),
(84, 'intelligence', 4), (84, 'resilience', 2), (84, 'charisma', 1),
(85, 'intelligence', 5), (85, 'resilience', 2),
(86, 'intelligence', 7), (86, 'resilience', 2), (86, 'creativity', 1),
(87, 'intelligence', 6), (87, 'resilience', 2),
(88, 'intelligence', 4), (88, 'resilience', 2), (88, 'charisma', 1),
(89, 'intelligence', 2), (89, 'resilience', 2),
(90, 'creativity', 3), (90, 'resilience', 2),
(91, 'creativity', 3), (91, 'resilience', 1),
(92, 'creativity', 4), (92, 'resilience', 2), (92, 'intelligence', 1),
(93, 'intelligence', 2), (93, 'resilience', 2),
(94, 'creativity', 4), (94, 'resilience', 2), (94, 'charisma', 1),
(95, 'charisma', 4), (95, 'intelligence', 3), (95, 'finances', 3), (95, 'resilience', 2),
(96, 'creativity', 3), (96, 'charisma', 2), (96, 'resilience', 1),
(97, 'charisma', 4), (97, 'creativity', 3), (97, 'resilience', 2), (97, 'finances', 1),
(98, 'charisma', 4), (98, 'intelligence', 3), (98, 'finances', 3), (98, 'resilience', 2),
(99, 'creativity', 4), (99, 'resilience', 1),
(100, 'charisma', 4), (100, 'resilience', 3), (100, 'intelligence', 2), (100, 'finances', 2),
(101, 'intelligence', 3), (101, 'finances', 3), (101, 'resilience', 1),
(102, 'charisma', 4), (102, 'resilience', 2),
(103, 'creativity', 3), (103, 'charisma', 2), (103, 'resilience', 1),
(104, 'intelligence', 15), (104, 'resilience', 10), (104, 'charisma', 5), (104, 'creativity', 5), (104, 'finances', 5),
(105, 'intelligence', 10), (105, 'resilience', 5), (105, 'charisma', 3), (105, 'creativity', 2);

-- ============================================
-- 5. COURSE WORKING DAYS
-- ============================================
INSERT INTO course_working_days (formation_id, day_of_week) VALUES
(5, 'MONDAY'), (5, 'TUESDAY'), (5, 'WEDNESDAY'), (5, 'THURSDAY'), (5, 'FRIDAY'),
(6, 'MONDAY'), (6, 'TUESDAY'), (6, 'WEDNESDAY'), (6, 'THURSDAY'), (6, 'FRIDAY'),
(7, 'MONDAY'), (7, 'TUESDAY'), (7, 'WEDNESDAY'), (7, 'THURSDAY'), (7, 'FRIDAY'),
(8, 'MONDAY'), (8, 'TUESDAY'), (8, 'WEDNESDAY'), (8, 'THURSDAY'), (8, 'FRIDAY'),
(9, 'MONDAY'), (9, 'TUESDAY'), (9, 'WEDNESDAY'), (9, 'THURSDAY'), (9, 'FRIDAY'),
(10, 'MONDAY'), (10, 'TUESDAY'), (10, 'WEDNESDAY'), (10, 'THURSDAY'), (10, 'FRIDAY'),
(11, 'MONDAY'), (11, 'TUESDAY'), (11, 'WEDNESDAY'), (11, 'THURSDAY'), (11, 'FRIDAY'),
(12, 'MONDAY'), (12, 'TUESDAY'), (12, 'WEDNESDAY'), (12, 'THURSDAY'), (12, 'FRIDAY'),
(13, 'MONDAY'), (13, 'TUESDAY'), (13, 'WEDNESDAY'), (13, 'THURSDAY'), (13, 'FRIDAY'),
(14, 'MONDAY'), (14, 'TUESDAY'), (14, 'WEDNESDAY'), (14, 'THURSDAY'), (14, 'FRIDAY'),
(15, 'MONDAY'), (15, 'TUESDAY'), (15, 'WEDNESDAY'), (15, 'THURSDAY'), (15, 'FRIDAY'),
(16, 'MONDAY'), (16, 'TUESDAY'), (16, 'WEDNESDAY'), (16, 'THURSDAY'), (16, 'FRIDAY'),
(17, 'MONDAY'), (17, 'TUESDAY'), (17, 'WEDNESDAY'), (17, 'THURSDAY'), (17, 'FRIDAY'),
(18, 'MONDAY'), (18, 'TUESDAY'), (18, 'WEDNESDAY'), (18, 'THURSDAY'), (18, 'FRIDAY'),
(19, 'MONDAY'), (19, 'TUESDAY'), (19, 'WEDNESDAY'), (19, 'THURSDAY'), (19, 'FRIDAY'),
(20, 'MONDAY'), (20, 'TUESDAY'), (20, 'WEDNESDAY'), (20, 'THURSDAY'), (20, 'FRIDAY'),
(21, 'MONDAY'), (21, 'TUESDAY'), (21, 'WEDNESDAY'), (21, 'THURSDAY'), (21, 'FRIDAY'),
(22, 'MONDAY'), (22, 'TUESDAY'), (22, 'WEDNESDAY'), (22, 'THURSDAY'), (22, 'FRIDAY'),
(23, 'MONDAY'), (23, 'TUESDAY'), (23, 'WEDNESDAY'), (23, 'THURSDAY'), (23, 'FRIDAY'),
(24, 'MONDAY'), (24, 'TUESDAY'), (24, 'WEDNESDAY'), (24, 'THURSDAY'), (24, 'FRIDAY'),
(25, 'MONDAY'), (25, 'TUESDAY'), (25, 'WEDNESDAY'), (25, 'THURSDAY'), (25, 'FRIDAY'),
(26, 'MONDAY'), (26, 'TUESDAY'), (26, 'WEDNESDAY'), (26, 'THURSDAY'), (26, 'FRIDAY'),
(27, 'MONDAY'), (27, 'TUESDAY'), (27, 'WEDNESDAY'), (27, 'THURSDAY'), (27, 'FRIDAY'),
(28, 'MONDAY'), (28, 'TUESDAY'), (28, 'WEDNESDAY'), (28, 'THURSDAY'), (28, 'FRIDAY'),
(29, 'MONDAY'), (29, 'TUESDAY'), (29, 'WEDNESDAY'), (29, 'THURSDAY'), (29, 'FRIDAY'),
(30, 'MONDAY'), (30, 'TUESDAY'), (30, 'WEDNESDAY'), (30, 'THURSDAY'), (30, 'FRIDAY'),
(31, 'MONDAY'), (31, 'TUESDAY'), (31, 'WEDNESDAY'), (31, 'THURSDAY'), (31, 'FRIDAY'),
(32, 'MONDAY'), (32, 'TUESDAY'), (32, 'WEDNESDAY'), (32, 'THURSDAY'), (32, 'FRIDAY'),
(33, 'MONDAY'), (33, 'TUESDAY'), (33, 'WEDNESDAY'), (33, 'THURSDAY'), (33, 'FRIDAY'),
(34, 'MONDAY'), (34, 'TUESDAY'), (34, 'WEDNESDAY'), (34, 'THURSDAY'), (34, 'FRIDAY'),
(35, 'MONDAY'), (35, 'TUESDAY'), (35, 'WEDNESDAY'), (35, 'THURSDAY'), (35, 'FRIDAY'),
(36, 'MONDAY'), (36, 'TUESDAY'), (36, 'WEDNESDAY'), (36, 'THURSDAY'), (36, 'FRIDAY'),
(37, 'MONDAY'), (37, 'TUESDAY'), (37, 'WEDNESDAY'), (37, 'THURSDAY'), (37, 'FRIDAY'),
(38, 'MONDAY'), (38, 'TUESDAY'), (38, 'WEDNESDAY'), (38, 'THURSDAY'), (38, 'FRIDAY'),
(39, 'MONDAY'), (39, 'TUESDAY'), (39, 'WEDNESDAY'), (39, 'THURSDAY'), (39, 'FRIDAY'),
(40, 'MONDAY'), (40, 'TUESDAY'), (40, 'WEDNESDAY'), (40, 'THURSDAY'), (40, 'FRIDAY'),
(41, 'MONDAY'), (41, 'TUESDAY'), (41, 'WEDNESDAY'), (41, 'THURSDAY'), (41, 'FRIDAY'),
(42, 'MONDAY'), (42, 'TUESDAY'), (42, 'WEDNESDAY'), (42, 'THURSDAY'), (42, 'FRIDAY'),
(43, 'MONDAY'), (43, 'TUESDAY'), (43, 'WEDNESDAY'), (43, 'THURSDAY'), (43, 'FRIDAY'),
(44, 'MONDAY'), (44, 'TUESDAY'), (44, 'WEDNESDAY'), (44, 'THURSDAY'), (44, 'FRIDAY'),
(45, 'MONDAY'), (45, 'TUESDAY'), (45, 'WEDNESDAY'), (45, 'THURSDAY'), (45, 'FRIDAY'),
(46, 'MONDAY'), (46, 'TUESDAY'), (46, 'WEDNESDAY'), (46, 'THURSDAY'), (46, 'FRIDAY'),
(47, 'MONDAY'), (47, 'TUESDAY'), (47, 'WEDNESDAY'), (47, 'THURSDAY'), (47, 'FRIDAY'),
(48, 'MONDAY'), (48, 'TUESDAY'), (48, 'WEDNESDAY'), (48, 'THURSDAY'), (48, 'FRIDAY'),
(49, 'MONDAY'), (49, 'TUESDAY'), (49, 'WEDNESDAY'), (49, 'THURSDAY'), (49, 'FRIDAY'),
(50, 'MONDAY'), (50, 'TUESDAY'), (50, 'WEDNESDAY'), (50, 'THURSDAY'), (50, 'FRIDAY'),
(51, 'MONDAY'), (51, 'TUESDAY'), (51, 'WEDNESDAY'), (51, 'THURSDAY'), (51, 'FRIDAY'),
(52, 'MONDAY'), (52, 'TUESDAY'), (52, 'WEDNESDAY'), (52, 'THURSDAY'), (52, 'FRIDAY'),
(53, 'MONDAY'), (53, 'TUESDAY'), (53, 'WEDNESDAY'), (53, 'THURSDAY'), (53, 'FRIDAY'),
(54, 'MONDAY'), (54, 'TUESDAY'), (54, 'WEDNESDAY'), (54, 'THURSDAY'), (54, 'FRIDAY'),
(55, 'MONDAY'), (55, 'TUESDAY'), (55, 'WEDNESDAY'), (55, 'THURSDAY'), (55, 'FRIDAY'),
(56, 'MONDAY'), (56, 'TUESDAY'), (56, 'WEDNESDAY'), (56, 'THURSDAY'), (56, 'FRIDAY'),
(57, 'MONDAY'), (57, 'TUESDAY'), (57, 'WEDNESDAY'), (57, 'THURSDAY'), (57, 'FRIDAY'),
(58, 'MONDAY'), (58, 'TUESDAY'), (58, 'WEDNESDAY'), (58, 'THURSDAY'), (58, 'FRIDAY'),
(59, 'MONDAY'), (59, 'TUESDAY'), (59, 'WEDNESDAY'), (59, 'THURSDAY'), (59, 'FRIDAY'),
(60, 'MONDAY'), (60, 'TUESDAY'), (60, 'WEDNESDAY'), (60, 'THURSDAY'), (60, 'FRIDAY'),
(61, 'MONDAY'), (61, 'TUESDAY'), (61, 'WEDNESDAY'), (61, 'THURSDAY'), (61, 'FRIDAY'),
(62, 'MONDAY'), (62, 'TUESDAY'), (62, 'WEDNESDAY'), (62, 'THURSDAY'), (62, 'FRIDAY'),
(63, 'MONDAY'), (63, 'TUESDAY'), (63, 'WEDNESDAY'), (63, 'THURSDAY'), (63, 'FRIDAY'),
(64, 'MONDAY'), (64, 'TUESDAY'), (64, 'WEDNESDAY'), (64, 'THURSDAY'), (64, 'FRIDAY'),
(65, 'MONDAY'), (65, 'TUESDAY'), (65, 'WEDNESDAY'), (65, 'THURSDAY'), (65, 'FRIDAY'),
(66, 'MONDAY'), (66, 'TUESDAY'), (66, 'WEDNESDAY'), (66, 'THURSDAY'), (66, 'FRIDAY'),
(67, 'MONDAY'), (67, 'TUESDAY'), (67, 'WEDNESDAY'), (67, 'THURSDAY'), (67, 'FRIDAY'),
(68, 'MONDAY'), (68, 'TUESDAY'), (68, 'WEDNESDAY'), (68, 'THURSDAY'), (68, 'FRIDAY'),
(69, 'MONDAY'), (69, 'TUESDAY'), (69, 'WEDNESDAY'), (69, 'THURSDAY'), (69, 'FRIDAY'),
(70, 'MONDAY'), (70, 'TUESDAY'), (70, 'WEDNESDAY'), (70, 'THURSDAY'), (70, 'FRIDAY'),
(71, 'MONDAY'), (71, 'TUESDAY'), (71, 'WEDNESDAY'), (71, 'THURSDAY'), (71, 'FRIDAY'),
(72, 'MONDAY'), (72, 'TUESDAY'), (72, 'WEDNESDAY'), (72, 'THURSDAY'), (72, 'FRIDAY'),
(73, 'MONDAY'), (73, 'TUESDAY'), (73, 'WEDNESDAY'), (73, 'THURSDAY'), (73, 'FRIDAY'),
(74, 'MONDAY'), (74, 'TUESDAY'), (74, 'WEDNESDAY'), (74, 'THURSDAY'), (74, 'FRIDAY'),
(75, 'MONDAY'), (75, 'TUESDAY'), (75, 'WEDNESDAY'), (75, 'THURSDAY'), (75, 'FRIDAY'),
(76, 'MONDAY'), (76, 'TUESDAY'), (76, 'WEDNESDAY'), (76, 'THURSDAY'), (76, 'FRIDAY'),
(77, 'MONDAY'), (77, 'TUESDAY'), (77, 'WEDNESDAY'), (77, 'THURSDAY'), (77, 'FRIDAY'),
(78, 'MONDAY'), (78, 'TUESDAY'), (78, 'WEDNESDAY'), (78, 'THURSDAY'), (78, 'FRIDAY'),
(79, 'MONDAY'), (79, 'TUESDAY'), (79, 'WEDNESDAY'), (79, 'THURSDAY'), (79, 'FRIDAY'),
(80, 'MONDAY'), (80, 'TUESDAY'), (80, 'WEDNESDAY'), (80, 'THURSDAY'), (80, 'FRIDAY'),
(81, 'MONDAY'), (81, 'TUESDAY'), (81, 'WEDNESDAY'), (81, 'THURSDAY'), (81, 'FRIDAY'),
(82, 'MONDAY'), (82, 'TUESDAY'), (82, 'WEDNESDAY'), (82, 'THURSDAY'), (82, 'FRIDAY'),
(83, 'MONDAY'), (83, 'TUESDAY'), (83, 'WEDNESDAY'), (83, 'THURSDAY'), (83, 'FRIDAY'),
(84, 'MONDAY'), (84, 'TUESDAY'), (84, 'WEDNESDAY'), (84, 'THURSDAY'), (84, 'FRIDAY'),
(85, 'MONDAY'), (85, 'TUESDAY'), (85, 'WEDNESDAY'), (85, 'THURSDAY'), (85, 'FRIDAY'),
(86, 'MONDAY'), (86, 'TUESDAY'), (86, 'WEDNESDAY'), (86, 'THURSDAY'), (86, 'FRIDAY'),
(87, 'MONDAY'), (87, 'TUESDAY'), (87, 'WEDNESDAY'), (87, 'THURSDAY'), (87, 'FRIDAY'),
(88, 'MONDAY'), (88, 'TUESDAY'), (88, 'WEDNESDAY'), (88, 'THURSDAY'), (88, 'FRIDAY'),
(89, 'MONDAY'), (89, 'TUESDAY'), (89, 'WEDNESDAY'), (89, 'THURSDAY'), (89, 'FRIDAY'),
(90, 'MONDAY'), (90, 'TUESDAY'), (90, 'WEDNESDAY'), (90, 'THURSDAY'), (90, 'FRIDAY'),
(91, 'MONDAY'), (91, 'TUESDAY'), (91, 'WEDNESDAY'), (91, 'THURSDAY'), (91, 'FRIDAY'),
(92, 'MONDAY'), (92, 'TUESDAY'), (92, 'WEDNESDAY'), (92, 'THURSDAY'), (92, 'FRIDAY'),
(93, 'MONDAY'), (93, 'TUESDAY'), (93, 'WEDNESDAY'), (93, 'THURSDAY'), (93, 'FRIDAY'),
(94, 'MONDAY'), (94, 'TUESDAY'), (94, 'WEDNESDAY'), (94, 'THURSDAY'), (94, 'FRIDAY'),
(95, 'MONDAY'), (95, 'TUESDAY'), (95, 'WEDNESDAY'), (95, 'THURSDAY'), (95, 'FRIDAY'),
(96, 'MONDAY'), (96, 'TUESDAY'), (96, 'WEDNESDAY'), (96, 'THURSDAY'), (96, 'FRIDAY'),
(97, 'MONDAY'), (97, 'TUESDAY'), (97, 'WEDNESDAY'), (97, 'THURSDAY'), (97, 'FRIDAY'),
(98, 'MONDAY'), (98, 'TUESDAY'), (98, 'WEDNESDAY'), (98, 'THURSDAY'), (98, 'FRIDAY'),
(99, 'MONDAY'), (99, 'TUESDAY'), (99, 'WEDNESDAY'), (99, 'THURSDAY'), (99, 'FRIDAY'),
(100, 'MONDAY'), (100, 'TUESDAY'), (100, 'WEDNESDAY'), (100, 'THURSDAY'), (100, 'FRIDAY'),
(101, 'MONDAY'), (101, 'TUESDAY'), (101, 'WEDNESDAY'), (101, 'THURSDAY'), (101, 'FRIDAY'),
(102, 'MONDAY'), (102, 'TUESDAY'), (102, 'WEDNESDAY'), (102, 'THURSDAY'), (102, 'FRIDAY'),
(103, 'MONDAY'), (103, 'TUESDAY'), (103, 'WEDNESDAY'), (103, 'THURSDAY'), (103, 'FRIDAY'),
(104, 'MONDAY'), (104, 'TUESDAY'), (104, 'WEDNESDAY'), (104, 'THURSDAY'), (104, 'FRIDAY'),
(105, 'MONDAY'), (105, 'TUESDAY'), (105, 'WEDNESDAY'), (105, 'THURSDAY'), (105, 'FRIDAY');

-- ============================================
-- 6. FORMATION EXAMS
-- ============================================
INSERT INTO formation_exams (id, formation_id, name, required_hours, difficulty, max_score, mandatory, min_passing_score, max_attempts, active, weight) VALUES
(1, 5, 'Examen final: Introducción a Java', 40, 'BASIC', 100, true, 60, 3, true, 100),
(2, 6, 'Examen final: Desarrollo Web Frontend', 50, 'BASIC', 100, true, 60, 3, true, 100),
(3, 7, 'Examen final: Fundamentos de Ciencia de Datos', 60, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(4, 8, 'Examen final: Seguridad Informática Básica', 35, 'BASIC', 100, true, 60, 3, true, 100),
(5, 9, 'Examen final: Introducción a Python', 45, 'BASIC', 100, true, 60, 3, true, 100),
(6, 10, 'Examen final: Frameworks Frontend (React y Angular)', 60, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(7, 11, 'Examen final: Fundamentos de DevOps', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(8, 12, 'Examen final: Introducción a la Nube', 40, 'BASIC', 100, true, 60, 3, true, 100),
(9, 13, 'Examen final: Inteligencia Artificial Básica', 55, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(10, 14, 'Examen final: Programación Avanzada en Java', 60, 'ADVANCED', 100, true, 70, 3, true, 100),
(11, 15, 'Examen final: Seguridad Web y Ciberseguridad', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(12, 16, 'Examen final: Analítica de Datos y Big Data', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(13, 17, 'Examen final: Desarrollo de Aplicaciones Móviles', 55, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(14, 18, 'Examen final: Robótica y Física Aplicada', 70, 'ADVANCED', 100, true, 70, 3, true, 100),
(15, 19, 'Examen final: Fundamentos de Salud Pública', 40, 'BASIC', 100, true, 60, 3, true, 100),
(16, 20, 'Examen final: Nutrición y Dietética', 35, 'BASIC', 100, true, 60, 3, true, 100),
(17, 21, 'Examen final: Primeros Auxilios y Emergencias', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(18, 22, 'Examen final: Anatomía Humana', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(19, 23, 'Examen final: Introducción a la Fisioterapia', 40, 'BASIC', 100, true, 60, 3, true, 100),
(20, 24, 'Examen final: Salud Mental y Bienestar', 30, 'BASIC', 100, true, 60, 3, true, 100),
(21, 25, 'Examen final: Fundamentos de Farmacología', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(22, 26, 'Examen final: Técnicas de Diagnóstico Médico', 60, 'ADVANCED', 100, true, 70, 3, true, 100),
(23, 27, 'Examen final: Cuidado y Enfermería', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(24, 28, 'Examen final: Introducción a la Biomedicina', 55, 'ADVANCED', 100, true, 70, 3, true, 100),
(25, 29, 'Examen final: Introducción a la Construcción', 40, 'BASIC', 100, true, 60, 3, true, 100),
(26, 30, 'Examen final: Albañilería y Estructuras', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(27, 31, 'Examen final: Fontanería y Sistemas Hidráulicos', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(28, 32, 'Examen final: Instalaciones Eléctricas', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(29, 33, 'Examen final: Carpintería Básica', 30, 'BASIC', 100, true, 60, 3, true, 100),
(30, 34, 'Examen final: Seguridad en la Construcción', 25, 'BASIC', 100, true, 60, 3, true, 100),
(31, 35, 'Examen final: Lectura de Planos y Diagramas', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(32, 36, 'Examen final: Tecnología del Hormigón', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(33, 37, 'Examen final: Instalación de Sistemas HVAC', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(34, 38, 'Examen final: Gestión de Proyectos de Construcción', 60, 'ADVANCED', 100, true, 70, 3, true, 100),
(35, 39, 'Examen final: Introducción a la Administración', 20, 'BASIC', 100, false, 50, 3, true, 100),
(36, 40, 'Examen final: Marketing Digital', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(37, 41, 'Examen final: Finanzas Personales', 25, 'BASIC', 100, false, 50, 3, true, 100),
(38, 42, 'Examen final: Liderazgo y Gestión de Equipos', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(39, 43, 'Examen final: Emprendimiento y Startups', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(40, 44, 'Examen final: Fundamentos de Economía', 20, 'BASIC', 100, false, 50, 3, true, 100),
(41, 45, 'Examen final: Gestión de Proyectos', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(42, 46, 'Examen final: Técnicas de Venta', 25, 'BASIC', 100, false, 50, 3, true, 100),
(43, 47, 'Examen final: Negociación y Resolución de Conflictos', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(44, 48, 'Examen final: Estrategia Empresarial', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(45, 49, 'Examen final: Diseño Gráfico Básico', 25, 'BASIC', 100, false, 50, 3, true, 100),
(46, 50, 'Examen final: Fotografía Digital', 20, 'BASIC', 100, false, 50, 3, true, 100),
(47, 51, 'Examen final: Ilustración Digital', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(48, 52, 'Examen final: Diseño de Interfaces y Experiencia de Usuario', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(49, 53, 'Examen final: Teoría Musical Básica', 20, 'BASIC', 100, false, 50, 3, true, 100),
(50, 54, 'Examen final: Fotografía Avanzada', 40, 'ADVANCED', 100, true, 70, 3, true, 100),
(51, 55, 'Examen final: Edición de Video', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(52, 56, 'Examen final: Escritura Creativa', 25, 'BASIC', 100, false, 50, 3, true, 100),
(53, 57, 'Examen final: Animación Digital', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(54, 58, 'Examen final: Producción Musical', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(55, 59, 'Examen final: Diseño Web Creativo', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(56, 60, 'Examen final: Gestión de Redes Sociales', 25, 'BASIC', 100, false, 50, 3, true, 100),
(57, 61, 'Examen final: Diseño de Interiores', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(58, 62, 'Examen final: Diseño de Moda', 40, 'ADVANCED', 100, true, 70, 3, true, 100),
(59, 63, 'Examen final: Diseño de Videojuegos', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(60, 64, 'Examen final: Fundamentos de Educación', 25, 'BASIC', 100, false, 50, 3, true, 100),
(61, 65, 'Examen final: Psicología Infantil', 30, 'BASIC', 100, false, 50, 3, true, 100),
(62, 66, 'Examen final: Trabajo Social Comunitario', 30, 'BASIC', 100, false, 50, 3, true, 100),
(63, 67, 'Examen final: Psicología Avanzada', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(64, 68, 'Examen final: Tecnología Educativa', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(65, 69, 'Examen final: Terapia y Counseling', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(66, 70, 'Examen final: Educación Especial', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(67, 71, 'Examen final: Políticas Sociales', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(68, 72, 'Examen final: Counseling Avanzado', 45, 'ADVANCED', 100, true, 70, 3, true, 100),
(69, 73, 'Examen final: Trabajo con Jóvenes', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(70, 74, 'Examen final: Desarrollo Comunitario', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(71, 75, 'Examen final: Políticas Educativas', 45, 'ADVANCED', 100, true, 70, 3, true, 100),
(72, 76, 'Examen final: Psicología Comunitaria', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(73, 77, 'Examen final: Biología General', 30, 'BASIC', 100, false, 50, 3, true, 100),
(74, 78, 'Examen final: Química General', 30, 'BASIC', 100, false, 50, 3, true, 100),
(75, 79, 'Examen final: Física General', 30, 'BASIC', 100, false, 50, 3, true, 100),
(76, 80, 'Examen final: Genética Básica', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(77, 81, 'Examen final: Microbiología', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(78, 82, 'Examen final: Química Orgánica', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(79, 83, 'Examen final: Bioquímica', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(80, 84, 'Examen final: Ciencias Ambientales', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(81, 85, 'Examen final: Métodos de Investigación', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(82, 86, 'Examen final: Ingeniería Genética', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(83, 87, 'Examen final: Análisis Químico', 45, 'ADVANCED', 100, true, 70, 3, true, 100),
(84, 88, 'Examen final: Ecología', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(85, 89, 'Examen final: Seguridad en Laboratorio', 20, 'BASIC', 100, false, 50, 3, true, 100),
(86, 90, 'Examen final: Cocina Básica', 30, 'BASIC', 100, false, 50, 3, true, 100),
(87, 91, 'Examen final: Repostería Básica', 25, 'BASIC', 100, false, 50, 3, true, 100),
(88, 92, 'Examen final: Cocina Intermedia', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(89, 93, 'Examen final: Higiene y Seguridad Alimentaria', 20, 'BASIC', 100, false, 50, 3, true, 100),
(90, 94, 'Examen final: Cocina Internacional', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(91, 95, 'Examen final: Gestión de Restaurantes', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(92, 96, 'Examen final: Técnicas de Barista', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(93, 97, 'Examen final: Planificación de Eventos', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(94, 98, 'Examen final: Gestión Turística', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(95, 99, 'Examen final: Presentación de Alimentos', 25, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(96, 100, 'Examen final: Operaciones Hoteleras', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(97, 101, 'Examen final: Control de Costes de Alimentos', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(98, 102, 'Examen final: Atención al Cliente en Hostelería', 20, 'BASIC', 100, false, 50, 3, true, 100),
(99, 103, 'Examen final: Preparación de Cócteles', 25, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(100, 104, 'Examen final: Formación Profesional', 2000, 'BASIC', 100, true, 60, 3, true, 100),
(101, 105, 'Examen final: Estudios Secundaria', 2000, 'BASIC', 100, false, 50, 3, true, 100);

-- ============================================
-- 7. SCHOLARSHIPS
-- ============================================
INSERT INTO scholarships (id, title, description, amount, active, min_xp_required, start_date, end_date, education_level, min_grade, requires_merit, max_family_income, requires_economic_proof, resolution_date, created_at, updated_at) VALUES
(1, 'Beca Excelencia Universitaria', 'Beca destinada a estudiantes con alto rendimiento académico.', 2500.00, true, 500, '2026-09-01', '2027-06-30', 'university', 8.5, true, NULL, false, '2026-08-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Beca Apoyo Familiar', 'Ayuda económica para estudiantes con bajos ingresos familiares.', 3000.00, true, 0, '2026-01-01', '2027-06-30', 'secondary', 0, false, 18000.00, false, '2026-08-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Beca Talento Joven', 'Beca para estudiantes con buen rendimiento y necesidades económicas.', 4000.00, true, 400, '2026-09-01', '2027-06-30', 'vocational', 7.5, true, 25000.00, true, '2026-08-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- 8. Reiniciar secuencias
-- ============================================
--SELECT setval('formations_id_seq', (SELECT MAX(id) FROM formations));
SELECT setval('formation_exams_id_seq', (SELECT MAX(id) FROM formation_exams));
SELECT setval('scholarships_id_seq', (SELECT MAX(id) FROM scholarships));