-- =====================================================
-- LIMPIAR TABLAS EXISTENTES (en orden correcto por FK)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE character_exams;
TRUNCATE TABLE formation_exams;
TRUNCATE TABLE formation_stat_rewards;
TRUNCATE TABLE course_working_days;
TRUNCATE TABLE formations;
TRUNCATE TABLE scholarships;
TRUNCATE TABLE scholarshipapplication;
TRUNCATE TABLE training;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. INSERTAR FORMACIONES (formations)
-- =====================================================
INSERT INTO formations (
    id, code, name, description, category, type, difficulty,
    min_education_level, min_academic_level, min_academic_xp, max_academic_xp,
    duration_hours, cost, effort, academic_xp_reward, repeatable, active,
    level, xp, locked, start_time, end_time
) VALUES
-- Tecnología (id 5-18)
(5, 'JAVA_BASIC_01', 'Introducción a Java', 'Curso básico de programación en Java, orientado a principiantes.',
 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 40, 150.00, 50, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(6, 'WEB_DEV_01', 'Desarrollo Web Frontend', 'Aprende HTML, CSS y JavaScript para crear páginas web interactivas.',
 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 50, 180.00, 60, 60, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(7, 'DATA_SCI_01', 'Fundamentos de Ciencia de Datos', 'Introducción al análisis de datos y conceptos básicos de Machine Learning.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'university', 0, 100, 300, 60, 250.00, 80, 100, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(8, 'CYBERSEC_01', 'Seguridad Informática Básica', 'Conceptos esenciales de ciberseguridad y protección de sistemas.',
 'TECHNOLOGY', 'COURSE', 'BASIC', 'secondary', 0, 0, 100, 35, 120.00, 40, 40, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(9, 'PYTHON_BASIC_01', 'Introducción a Python', 'Aprende los fundamentos de Python y programación orientada a objetos.',
 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 45, 160.00, 50, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(10, 'FRONTEND_02', 'Frameworks Frontend (React y Angular)', 'Aprende a crear aplicaciones web modernas usando React y Angular.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'highschool', 50, 50, 200, 60, 220.00, 70, 80, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(11, 'DEVOPS_01', 'Fundamentos de DevOps', 'Conceptos de integración y despliegue continuo, y gestión de entornos.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'technical', 50, 50, 200, 50, 200.00, 70, 75, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(12, 'CLOUD_01', 'Introducción a la Nube', 'Aprende conceptos de cloud computing, AWS, Azure y Google Cloud.',
 'TECHNOLOGY', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 40, 180.00, 50, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(13, 'AI_INTRO_01', 'Inteligencia Artificial Básica', 'Curso introductorio a IA y Machine Learning con ejemplos prácticos.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'university', 100, 100, 300, 55, 250.00, 80, 100, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(14, 'JAVA_ADV_01', 'Programación Avanzada en Java', 'Aprende conceptos avanzados de Java, patrones de diseño y buenas prácticas.',
 'TECHNOLOGY', 'COURSE', 'ADVANCED', 'technical', 100, 100, 300, 60, 280.00, 90, 120, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(15, 'WEB_SECURITY_01', 'Seguridad Web y Ciberseguridad', 'Conoce las técnicas de seguridad web y protección frente a ataques informáticos.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'highschool', 50, 50, 200, 45, 200.00, 70, 75, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(16, 'DATA_ANALYTICS_01', 'Analítica de Datos y Big Data', 'Aprende a analizar datos, crear dashboards y comprender Big Data.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'technical', 60, 50, 250, 50, 220.00, 75, 85, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(17, 'MOBILE_DEV_01', 'Desarrollo de Aplicaciones Móviles', 'Crea apps móviles para iOS y Android con frameworks modernos.',
 'TECHNOLOGY', 'COURSE', 'INTERMEDIATE', 'technical', 50, 50, 200, 55, 240.00, 80, 90, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(18, 'CYBER_PHYSICS_01', 'Robótica y Física Aplicada', 'Curso práctico sobre robótica, automatización y principios de física aplicada.',
 'TECHNOLOGY', 'COURSE', 'ADVANCED', 'technical', 100, 100, 300, 70, 300.00, 100, 150, false, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Salud (id 19-28)
(19, 'HEALTH_BASIC_01', 'Fundamentos de Salud Pública', 'Introducción a la salud pública, epidemiología y promoción de la salud.',
 'HEALTH', 'COURSE', 'BASIC', 'highschool', 0, 0, 100, 40, 180.00, 50, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(20, 'NUTRITION_01', 'Nutrición y Dietética', 'Principios de nutrición, planificación de dietas y hábitos saludables.',
 'HEALTH', 'COURSE', 'BASIC', 'highschool', 10, 0, 100, 35, 150.00, 40, 45, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(21, 'FIRST_AID_01', 'Primeros Auxilios y Emergencias', 'Aprende técnicas de primeros auxilios y manejo de situaciones de emergencia.',
 'HEALTH', 'COURSE', 'INTERMEDIATE', 'secondary', 20, 10, 150, 30, 120.00, 60, 60, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(22, 'ANATOMY_01', 'Anatomía Humana', 'Estudio del cuerpo humano, sistemas y funciones principales.',
 'HEALTH', 'COURSE', 'INTERMEDIATE', 'secondary', 30, 20, 200, 50, 250.00, 80, 90, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(23, 'PHYSIOTHERAPY_01', 'Introducción a la Fisioterapia', 'Conceptos básicos de fisioterapia y técnicas de rehabilitación.',
 'HEALTH', 'COURSE', 'BASIC', 'secondary', 20, 10, 150, 40, 200.00, 60, 60, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(24, 'MENTAL_HEALTH_01', 'Salud Mental y Bienestar', 'Aprende técnicas para promover el bienestar mental y emocional.',
 'HEALTH', 'COURSE', 'BASIC', 'highschool', 10, 0, 100, 30, 150.00, 40, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(25, 'PHARMACY_01', 'Fundamentos de Farmacología', 'Principios básicos de farmacología y administración de medicamentos.',
 'HEALTH', 'COURSE', 'INTERMEDIATE', 'technical', 40, 30, 200, 45, 220.00, 70, 80, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(26, 'DIAGNOSTICS_01', 'Técnicas de Diagnóstico Médico', 'Aprende sobre pruebas médicas, diagnóstico y procedimientos clínicos básicos.',
 'HEALTH', 'COURSE', 'ADVANCED', 'university', 70, 50, 300, 60, 300.00, 100, 120, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(27, 'NURSING_01', 'Cuidado y Enfermería', 'Aprende los principios básicos de enfermería y cuidado del paciente.',
 'HEALTH', 'COURSE', 'INTERMEDIATE', 'secondary', 30, 20, 200, 50, 240.00, 80, 90, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(28, 'BIOMED_01', 'Introducción a la Biomedicina', 'Curso sobre fundamentos de biomedicina y tecnología aplicada a la salud.',
 'HEALTH', 'COURSE', 'ADVANCED', 'technical', 50, 40, 250, 55, 280.00, 90, 110, false, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Construcción (id 29-38)
(29, 'CONSTRUCTION_BASIC_01', 'Introducción a la Construcción', 'Conceptos básicos de construcción, materiales y técnicas iniciales.',
 'CONSTRUCTION', 'COURSE', 'BASIC', 'secondary', 0, 0, 100, 40, 150.00, 50, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(30, 'MASONRY_01', 'Albañilería y Estructuras', 'Aprende técnicas de albañilería, construcción de muros y estructuras básicas.',
 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'secondary', 10, 0, 120, 45, 180.00, 60, 60, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(31, 'PLUMBING_01', 'Fontanería y Sistemas Hidráulicos', 'Instalación, mantenimiento y reparación de sistemas de fontanería.',
 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'technical', 20, 10, 150, 35, 200.00, 70, 70, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(32, 'ELECTRICAL_01', 'Instalaciones Eléctricas', 'Principios y técnicas para la instalación y mantenimiento eléctrico.',
 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'technical', 25, 10, 160, 40, 220.00, 70, 75, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(33, 'CARPENTRY_01', 'Carpintería Básica', 'Aprende técnicas de carpintería, herramientas y construcción de estructuras de madera.',
 'CONSTRUCTION', 'COURSE', 'BASIC', 'secondary', 15, 0, 120, 30, 160.00, 50, 55, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(34, 'SAFETY_01', 'Seguridad en la Construcción', 'Normas y procedimientos para garantizar la seguridad en obras y proyectos.',
 'CONSTRUCTION', 'COURSE', 'BASIC', 'secondary', 0, 0, 100, 25, 120.00, 40, 50, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(35, 'BLUEPRINT_01', 'Lectura de Planos y Diagramas', 'Interpretación de planos arquitectónicos y diagramas de construcción.',
 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'secondary', 20, 10, 150, 30, 140.00, 50, 60, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(36, 'CONCRETE_01', 'Tecnología del Hormigón', 'Estudio de materiales, mezclas y técnicas de construcción con hormigón.',
 'CONSTRUCTION', 'COURSE', 'ADVANCED', 'technical', 40, 20, 200, 50, 250.00, 80, 100, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(37, 'HVAC_01', 'Instalación de Sistemas HVAC', 'Aprende a instalar y mantener sistemas de calefacción, ventilación y aire acondicionado.',
 'CONSTRUCTION', 'COURSE', 'INTERMEDIATE', 'technical', 35, 20, 180, 40, 220.00, 70, 80, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(38, 'PROJECT_MANAGEMENT_01', 'Gestión de Proyectos de Construcción', 'Aprende a planificar, coordinar y supervisar proyectos de construcción.',
 'CONSTRUCTION', 'COURSE', 'ADVANCED', 'technical', 50, 30, 250, 60, 300.00, 90, 120, false, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Negocios (id 39-48)
(39, 'BUSINESS_BASIC_01', 'Introducción a la Administración', 'Fundamentos de administración y gestión de empresas.',
 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 50.00, 10, 30, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(40, 'MARKETING_01', 'Marketing Digital', 'Conceptos y herramientas de marketing online y redes sociales.',
 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80.00, 20, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(41, 'FINANCE_01', 'Finanzas Personales', 'Aprende a gestionar tus ingresos, gastos y ahorros.',
 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 60.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(42, 'LEADERSHIP_01', 'Liderazgo y Gestión de Equipos', 'Desarrolla habilidades de liderazgo y gestión efectiva de equipos.',
 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 30, 120, 35, 100.00, 25, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(43, 'ENTREPRENEUR_01', 'Emprendimiento y Startups', 'Cómo crear y gestionar tu propio negocio desde cero.',
 'BUSINESS', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 50, 150.00, 40, 100, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(44, 'ECONOMICS_01', 'Fundamentos de Economía', 'Principios básicos de economía y su aplicación en los negocios.',
 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 60, 20, 50.00, 10, 30, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(45, 'PROJECT_MGMT_01', 'Gestión de Proyectos', 'Técnicas y herramientas para planificar y ejecutar proyectos con éxito.',
 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 120.00, 30, 70, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(46, 'SALES_01', 'Técnicas de Venta', 'Aprende a vender productos y servicios de manera efectiva.',
 'BUSINESS', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 60.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(47, 'NEGOTIATION_01', 'Negociación y Resolución de Conflictos', 'Desarrolla habilidades de negociación y manejo de conflictos.',
 'BUSINESS', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 90.00, 20, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(48, 'STRATEGY_01', 'Estrategia Empresarial', 'Aprende a formular estrategias efectivas para tu empresa.',
 'BUSINESS', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 50, 150.00, 40, 100, true, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Creativo (id 49-63)
(49, 'GRAPHIC_DESIGN_01', 'Diseño Gráfico Básico', 'Fundamentos del diseño gráfico, teoría del color y composición visual.',
 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 60.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(50, 'PHOTOGRAPHY_01', 'Fotografía Digital', 'Técnicas de fotografía, iluminación y composición para principiantes.',
 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 50.00, 12, 35, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(51, 'ILLUSTRATION_01', 'Ilustración Digital', 'Técnicas de dibujo e ilustración utilizando herramientas digitales.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80.00, 20, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(52, 'UIUX_01', 'Diseño de Interfaces y Experiencia de Usuario', 'Principios de UX/UI para aplicaciones web y móviles.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 100.00, 25, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(53, 'MUSIC_THEORY_01', 'Teoría Musical Básica', 'Fundamentos de teoría musical, notas, escalas y ritmo.',
 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 50.00, 12, 35, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(54, 'PHOTOGRAPHY_ADV_01', 'Fotografía Avanzada', 'Técnicas avanzadas de fotografía y edición profesional.',
 'CREATIVE', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 40, 120.00, 30, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(55, 'VIDEO_EDITING_01', 'Edición de Video', 'Aprende a editar videos profesionales para proyectos creativos.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80.00, 20, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(56, 'CREATIVE_WRITING_01', 'Escritura Creativa', 'Desarrolla técnicas de narrativa y redacción literaria.',
 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(57, 'ANIMATION_01', 'Animación Digital', 'Fundamentos de animación 2D y 3D para proyectos creativos.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 100.00, 25, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(58, 'MUSIC_PRODUCTION_01', 'Producción Musical', 'Aprende a producir música usando software profesional.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80.00, 20, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(59, 'WEB_DESIGN_01', 'Diseño Web Creativo', 'Aprende diseño web con enfoque creativo y UX/UI.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 100.00, 25, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(60, 'SOCIAL_MEDIA_01', 'Gestión de Redes Sociales', 'Aprende a gestionar redes sociales y crear contenido creativo.',
 'CREATIVE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(61, 'INTERIOR_DESIGN_01', 'Diseño de Interiores', 'Principios y técnicas de diseño de espacios interiores.',
 'CREATIVE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 80.00, 20, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(62, 'FASHION_DESIGN_01', 'Diseño de Moda', 'Conceptos y técnicas para crear colecciones de moda.',
 'CREATIVE', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 40, 120.00, 30, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(63, 'VIDEO_GAME_DESIGN_01', 'Diseño de Videojuegos', 'Aprende diseño creativo y mecánicas para videojuegos.',
 'CREATIVE', 'COURSE', 'ADVANCED', 'highschool', 2, 50, 200, 50, 150.00, 40, 100, true, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Social (id 64-76)
(64, 'EDUCATION_BASIC_01', 'Fundamentos de Educación', 'Introducción a la pedagogía y métodos educativos para principiantes.',
 'SOCIAL', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 25, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(65, 'CHILD_PSYC_01', 'Psicología Infantil', 'Principios básicos de la psicología infantil y desarrollo cognitivo.',
 'SOCIAL', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 60.00, 18, 45, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(66, 'SOCIAL_WORK_01', 'Trabajo Social Comunitario', 'Introducción al trabajo social y estrategias para comunidades.',
 'SOCIAL', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 55.00, 17, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(67, 'PSYCHOLOGY_ADV_01', 'Psicología Avanzada', 'Conceptos avanzados de psicología y análisis de comportamiento humano.',
 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 120.00, 35, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(68, 'EDUCATION_TECH_01', 'Tecnología Educativa', 'Uso de herramientas digitales para la enseñanza y aprendizaje.',
 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 80.00, 22, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(69, 'COUNSELING_01', 'Terapia y Counseling', 'Técnicas de counseling y acompañamiento emocional.',
 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 90.00, 25, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(70, 'SPECIAL_EDU_01', 'Educación Especial', 'Fundamentos y técnicas de educación para estudiantes con necesidades especiales.',
 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 70.00, 18, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(71, 'SOCIAL_POLICIES_01', 'Políticas Sociales', 'Análisis de políticas públicas y programas sociales.',
 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 120.00, 35, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(72, 'COUNSELING_ADV_01', 'Counseling Avanzado', 'Métodos avanzados de acompañamiento psicológico y coaching.',
 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 45, 130.00, 38, 90, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(73, 'YOUTH_WORK_01', 'Trabajo con Jóvenes', 'Programas y técnicas para el desarrollo de jóvenes en riesgo.',
 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 85.00, 22, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(74, 'COMMUNITY_DEVELOPMENT_01', 'Desarrollo Comunitario', 'Técnicas de planificación y gestión de proyectos comunitarios.',
 'SOCIAL', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 30, 75.00, 18, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(75, 'EDUCATION_POLICY_01', 'Políticas Educativas', 'Estudio de políticas y regulaciones del sistema educativo.',
 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 45, 130.00, 35, 90, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(76, 'PSYCHOLOGY_COMMUNITY_01', 'Psicología Comunitaria', 'Aplicación de técnicas psicológicas para mejorar la calidad de vida de comunidades.',
 'SOCIAL', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 150.00, 40, 100, true, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Ciencia (id 77-89)
(77, 'BIOLOGY_BASIC_01', 'Biología General', 'Introducción a la biología, estructuras celulares y funciones básicas.',
 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(78, 'CHEMISTRY_BASIC_01', 'Química General', 'Principios fundamentales de química, elementos y reacciones básicas.',
 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(79, 'PHYSICS_BASIC_01', 'Física General', 'Fundamentos de la física clásica y principios mecánicos.',
 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 30, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(80, 'GENETICS_01', 'Genética Básica', 'Introducción a los genes, herencia y variación biológica.',
 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 80.00, 20, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(81, 'MICROBIOLOGY_01', 'Microbiología', 'Estudio de microorganismos y su papel en la vida y la industria.',
 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 85.00, 22, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(82, 'ORGANIC_CHEMISTRY_01', 'Química Orgánica', 'Principios de química orgánica y reacciones de compuestos carbonados.',
 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 120.00, 35, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(83, 'BIOCHEMISTRY_01', 'Bioquímica', 'Estudio de procesos químicos en organismos vivos.',
 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 130.00, 38, 85, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(84, 'ENVIRONMENTAL_SCIENCE_01', 'Ciencias Ambientales', 'Estudio del medio ambiente, ecosistemas y sostenibilidad.',
 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 40, 90.00, 25, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(85, 'RESEARCH_METHODS_01', 'Métodos de Investigación', 'Técnicas y metodologías para la investigación científica.',
 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 75.00, 20, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(86, 'GENETIC_ENGINEERING_01', 'Ingeniería Genética', 'Técnicas de manipulación genética y biotecnología aplicada.',
 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 50, 140.00, 40, 95, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(87, 'CHEM_ANALYSIS_01', 'Análisis Químico', 'Técnicas de laboratorio para análisis de sustancias químicas.',
 'SCIENCE', 'COURSE', 'ADVANCED', 'university', 2, 50, 200, 45, 125.00, 38, 90, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(88, 'ECOLOGY_01', 'Ecología', 'Estudio de los ecosistemas, biodiversidad y relaciones ambientales.',
 'SCIENCE', 'COURSE', 'INTERMEDIATE', 'highschool', 1, 20, 100, 35, 80.00, 20, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(89, 'LAB_SAFETY_01', 'Seguridad en Laboratorio', 'Normas y procedimientos para un trabajo seguro en laboratorios científicos.',
 'SCIENCE', 'COURSE', 'BASIC', 'highschool', 0, 0, 50, 20, 40.00, 12, 35, true, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Hospitalidad (id 90-103)
(90, 'CULINARY_BASICS_01', 'Cocina Básica', 'Introducción a técnicas culinarias fundamentales y preparación de platos sencillos.',
 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 30, 50.00, 15, 40, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(91, 'PASTRY_BASICS_01', 'Repostería Básica', 'Técnicas iniciales de repostería, bizcochos, galletas y postres simples.',
 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 25, 45.00, 12, 35, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(92, 'INTERMEDIATE_CULINARY_01', 'Cocina Intermedia', 'Preparación de platos más complejos y técnicas culinarias intermedias.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 40, 80.00, 20, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(93, 'FOOD_SAFETY_01', 'Higiene y Seguridad Alimentaria', 'Normas básicas de higiene y seguridad en cocina y manipulación de alimentos.',
 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 20, 40.00, 10, 35, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(94, 'WORLD_CUISINE_01', 'Cocina Internacional', 'Exploración de recetas y técnicas culinarias de distintas culturas.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 45, 90.00, 22, 60, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(95, 'RESTAURANT_MANAGEMENT_01', 'Gestión de Restaurantes', 'Administración, planificación y organización de un restaurante.',
 'HOSPITALITY', 'COURSE', 'ADVANCED', 'secondary', 2, 50, 200, 50, 120.00, 35, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(96, 'BARISTA_SKILLS_01', 'Técnicas de Barista', 'Preparación profesional de café y bebidas de especialidad.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 30, 60.00, 18, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(97, 'EVENT_PLANNING_01', 'Planificación de Eventos', 'Organización de eventos, logística y atención al cliente.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'secondary', 1, 20, 100, 35, 75.00, 20, 55, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(98, 'TOURISM_MANAGEMENT_01', 'Gestión Turística', 'Principios de administración en empresas turísticas y hoteles.',
 'HOSPITALITY', 'COURSE', 'ADVANCED', 'secondary', 2, 50, 200, 50, 120.00, 35, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(99, 'FOOD_PRESENTATION_01', 'Presentación de Alimentos', 'Técnicas de emplatado y presentación profesional de platos.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 25, 55.00, 15, 45, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(100, 'HOTEL_OPERATIONS_01', 'Operaciones Hoteleras', 'Gestión diaria de hoteles, atención al cliente y logística.',
 'HOSPITALITY', 'COURSE', 'ADVANCED', 'secondary', 2, 50, 200, 50, 120.00, 35, 80, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(101, 'FOOD_COST_CONTROL_01', 'Control de Costes de Alimentos', 'Gestión de inventario y control de costes en cocina y restaurantes.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'secondary', 1, 20, 100, 30, 60.00, 18, 50, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(102, 'HOSPITALITY_CUSTOMER_SERVICE_01', 'Atención al Cliente en Hostelería', 'Técnicas de atención al cliente y servicio profesional en restaurantes y hoteles.',
 'HOSPITALITY', 'COURSE', 'BASIC', 'none', 0, 0, 50, 20, 40.00, 12, 35, true, true, 1, 0, false, '08:00:00', '14:00:00'),
(103, 'COCKTAILS_01', 'Preparación de Cócteles', 'Técnicas para preparar y presentar cócteles profesionales.',
 'HOSPITALITY', 'COURSE', 'INTERMEDIATE', 'none', 1, 20, 100, 25, 55.00, 15, 45, true, true, 1, 0, false, '08:00:00', '14:00:00'),

-- Educación (id 104-105)
(104, 'FORMACION_PROFESIONAL_01', 'Formación Profesional', 'Curso de formación profesional orientado a habilidades prácticas y desarrollo académico.',
 'EDUCATION', 'VOCATIONAL_TRAINING', 'BASIC', 'technical', 0, 0, 100, 2000, 0.00, 50, 180, false, true, 1, 0, false, '08:00:00', '14:00:00'),
(105, 'HIGH_SCHOOL_01', 'Estudios Secundaria', 'Estudiando Bachillerato',
 'EDUCATION', 'HIGH_SCHOOL', 'BASIC', 'none', 0, 0, 100, 2000, 0.00, 30, 180, false, true, 1, 0, false, '08:00:00', '14:00:00');

-- =====================================================
-- 2. INSERTAR DÍAS LABORABLES (course_working_days)
-- =====================================================
-- Para todas las formaciones (id 5-105), insertamos los días laborables de Lunes a Viernes
INSERT INTO course_working_days (formation_id, day_of_week)
SELECT id, 'MONDAY' FROM formations WHERE id BETWEEN 5 AND 105
UNION ALL
SELECT id, 'TUESDAY' FROM formations WHERE id BETWEEN 5 AND 105
UNION ALL
SELECT id, 'WEDNESDAY' FROM formations WHERE id BETWEEN 5 AND 105
UNION ALL
SELECT id, 'THURSDAY' FROM formations WHERE id BETWEEN 5 AND 105
UNION ALL
SELECT id, 'FRIDAY' FROM formations WHERE id BETWEEN 5 AND 105;

-- =====================================================
-- 3. INSERTAR HABILIDADES DESBLOQUEADAS (skillsUnlocked)
-- =====================================================
-- Java Básico (id 5)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(5, 'basic_programming'),
(5, 'logic_thinking');

-- Web Frontend (id 6)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(6, 'web_design'),
(6, 'frontend_development');

-- Data Science (id 7)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(7, 'data_analysis'),
(7, 'statistics'),
(7, 'python_basics');

-- Ciberseguridad (id 8)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(8, 'cybersecurity_basics'),
(8, 'network_safety');

-- Python Básico (id 9)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(9, 'basic_programming'),
(9, 'python_basics');

-- Frameworks Frontend (id 10)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(10, 'frontend_development'),
(10, 'react'),
(10, 'angular');

-- DevOps (id 11)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(11, 'devops_basics'),
(11, 'ci_cd'),
(11, 'automation');

-- Cloud (id 12)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(12, 'cloud_basics'),
(12, 'aws'),
(12, 'azure'),
(12, 'google_cloud');

-- IA Básica (id 13)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(13, 'ai_basics'),
(13, 'machine_learning'),
(13, 'python_advanced');

-- Java Avanzado (id 14)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(14, 'java_advanced'),
(14, 'object_oriented_design');

-- Seguridad Web (id 15)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(15, 'web_security'),
(15, 'cybersecurity_basics');

-- Data Analytics (id 16)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(16, 'data_analysis'),
(16, 'big_data'),
(16, 'sql');

-- Mobile Dev (id 17)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(17, 'mobile_development'),
(17, 'react_native'),
(17, 'flutter');

-- Robótica (id 18)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(18, 'robotics'),
(18, 'automation'),
(18, 'physics_applied');

-- Salud Pública (id 19)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(19, 'public_health_basics'),
(19, 'health_awareness');

-- Nutrición (id 20)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(20, 'nutrition_basics'),
(20, 'diet_planning');

-- Primeros Auxilios (id 21)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(21, 'first_aid'),
(21, 'emergency_response');

-- Anatomía (id 22)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(22, 'human_anatomy'),
(22, 'physiology_basics');

-- Fisioterapia (id 23)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(23, 'physiotherapy_basics'),
(23, 'rehabilitation');

-- Salud Mental (id 24)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(24, 'mental_health_awareness'),
(24, 'stress_management');

-- Farmacología (id 25)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(25, 'pharmacology_basics'),
(25, 'medication_management');

-- Diagnóstico (id 26)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(26, 'medical_diagnostics'),
(26, 'clinical_procedures');

-- Enfermería (id 27)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(27, 'nursing_basics'),
(27, 'patient_care');

-- Biomedicina (id 28)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(28, 'biomedicine'),
(28, 'lab_techniques'),
(28, 'medical_research');

-- Construcción (id 29)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(29, 'construction_basics'),
(29, 'material_handling');

-- Albañilería (id 30)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(30, 'masonry'),
(30, 'structural_basics');

-- Fontanería (id 31)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(31, 'plumbing'),
(31, 'water_systems');

-- Eléctrica (id 32)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(32, 'electrical_installation'),
(32, 'wiring');

-- Carpintería (id 33)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(33, 'carpentry_basics'),
(33, 'woodwork');

-- Seguridad Construcción (id 34)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(34, 'construction_safety'),
(34, 'risk_management');

-- Planos (id 35)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(35, 'blueprint_reading'),
(35, 'technical_drawing');

-- Hormigón (id 36)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(36, 'concrete_technology'),
(36, 'structural_analysis');

-- HVAC (id 37)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(37, 'hvac_installation'),
(37, 'ventilation_systems');

-- Gestión Proyectos Construcción (id 38)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(38, 'project_management'),
(38, 'construction_coordination');

-- Negocios - Administración (id 39)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(39, 'BASIC_management');

-- Marketing (id 40)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(40, 'digital_marketing');

-- Finanzas (id 41)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(41, 'financial_other');

-- Liderazgo (id 42)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(42, 'team_leadership');

-- Emprendimiento (id 43)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(43, 'business_planning'),
(43, 'startup_other');

-- Economía (id 44)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(44, 'economic_fundamentals');

-- Gestión Proyectos (id 45)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(45, 'project_other');

-- Ventas (id 46)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(46, 'sales_skills');

-- Negociación (id 47)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(47, 'negotiation'),
(47, 'conflict_resolution');

-- Estrategia (id 48)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(48, 'business_strategy');

-- Diseño Gráfico (id 49)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(49, 'basic_graphic_design');

-- Fotografía (id 50)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(50, 'basic_photography');

-- Ilustración (id 51)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(51, 'digital_illustration');

-- UI/UX (id 52)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(52, 'uiux_design');

-- Teoría Musical (id 53)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(53, 'basic_music_theory');

-- Fotografía Avanzada (id 54)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(54, 'advanced_photography');

-- Edición Video (id 55)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(55, 'video_editing');

-- Escritura Creativa (id 56)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(56, 'creative_writing');

-- Animación (id 57)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(57, 'digital_animation');

-- Producción Musical (id 58)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(58, 'music_production');

-- Diseño Web (id 59)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(59, 'web_design');

-- Redes Sociales (id 60)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(60, 'social_media_management');

-- Diseño Interiores (id 61)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(61, 'interior_design');

-- Diseño Moda (id 62)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(62, 'fashion_design');

-- Diseño Videojuegos (id 63)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(63, 'game_design'),
(63, 'game_mechanics');

-- Educación (id 64)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(64, 'basic_teaching');

-- Psicología Infantil (id 65)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(65, 'child_psychology_basic');

-- Trabajo Social (id 66)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(66, 'community_work_basic');

-- Psicología Avanzada (id 67)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(67, 'advanced_psychology');

-- Tecnología Educativa (id 68)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(68, 'edtech_tools');

-- Counseling (id 69)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(69, 'counseling_skills');

-- Educación Especial (id 70)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(70, 'special_education');

-- Políticas Sociales (id 71)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(71, 'social_policy_analysis');

-- Counseling Avanzado (id 72)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(72, 'advanced_counseling');

-- Trabajo Jóvenes (id 73)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(73, 'youth_programs');

-- Desarrollo Comunitario (id 74)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(74, 'community_development');

-- Políticas Educativas (id 75)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(75, 'education_policy_analysis');

-- Psicología Comunitaria (id 76)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(76, 'community_psychology');

-- Biología (id 77)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(77, 'basic_biology');

-- Química (id 78)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(78, 'basic_chemistry');

-- Física (id 79)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(79, 'basic_physics');

-- Genética (id 80)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(80, 'genetics_basics');

-- Microbiología (id 81)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(81, 'microbiology');

-- Química Orgánica (id 82)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(82, 'organic_chemistry');

-- Bioquímica (id 83)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(83, 'biochemistry');

-- Ciencias Ambientales (id 84)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(84, 'environmental_science');

-- Métodos Investigación (id 85)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(85, 'research_methods');

-- Ingeniería Genética (id 86)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(86, 'genetic_engineering');

-- Análisis Químico (id 87)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(87, 'chemical_analysis');

-- Ecología (id 88)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(88, 'ecology_basics');

-- Seguridad Laboratorio (id 89)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(89, 'lab_safety');

-- Cocina Básica (id 90)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(90, 'basic_cooking');

-- Repostería (id 91)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(91, 'basic_pastry');

-- Cocina Intermedia (id 92)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(92, 'intermediate_cooking');

-- Seguridad Alimentaria (id 93)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(93, 'food_safety');

-- Cocina Internacional (id 94)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(94, 'international_cuisine');

-- Gestión Restaurantes (id 95)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(95, 'restaurant_management');

-- Barista (id 96)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(96, 'barista_skills');

-- Planificación Eventos (id 97)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(97, 'event_planning');

-- Gestión Turística (id 98)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(98, 'tourism_management');

-- Presentación Alimentos (id 99)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(99, 'food_presentation');

-- Operaciones Hoteleras (id 100)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(100, 'hotel_operations');

-- Control Costes (id 101)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(101, 'food_cost_control');

-- Atención Cliente (id 102)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(102, 'customer_service');

-- Cócteles (id 103)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(103, 'cocktail_preparation');

-- Formación Profesional (id 104)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(104, 'basic_skills'),
(104, 'logic_thinking'),
(104, 'communication');

-- Estudios Secundaria (id 105)
INSERT INTO formation_skills_unlocked (formation_id, skills_unlocked) VALUES
(105, 'reading'),
(105, 'writing'),
(105, 'basic_math');

-- =====================================================
-- 4. INSERTAR STAT REWARDS (formation_stat_rewards)
-- =====================================================
-- Java Básico (id 5)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(5, 'intelligence', 5),
(5, 'resilience', 1);

-- Web Frontend (id 6)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(6, 'intelligence', 4),
(6, 'creativity', 3),
(6, 'resilience', 1);

-- Data Science (id 7)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(7, 'intelligence', 8),
(7, 'resilience', 2);

-- Ciberseguridad (id 8)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(8, 'intelligence', 5),
(8, 'resilience', 2);

-- Python Básico (id 9)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(9, 'intelligence', 5),
(9, 'resilience', 1);

-- Frameworks Frontend (id 10)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(10, 'intelligence', 6),
(10, 'creativity', 3),
(10, 'resilience', 2);

-- DevOps (id 11)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(11, 'intelligence', 6),
(11, 'resilience', 3);

-- Cloud (id 12)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(12, 'intelligence', 4),
(12, 'resilience', 1);

-- IA Básica (id 13)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(13, 'intelligence', 8),
(13, 'resilience', 2);

-- Java Avanzado (id 14)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(14, 'intelligence', 8),
(14, 'resilience', 3);

-- Seguridad Web (id 15)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(15, 'intelligence', 6),
(15, 'resilience', 3);

-- Data Analytics (id 16)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(16, 'intelligence', 7),
(16, 'resilience', 2);

-- Mobile Dev (id 17)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(17, 'intelligence', 6),
(17, 'creativity', 3),
(17, 'resilience', 2);

-- Robótica (id 18)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(18, 'intelligence', 7),
(18, 'resilience', 5),
(18, 'creativity', 2);

-- Salud Pública (id 19)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(19, 'intelligence', 4),
(19, 'resilience', 2),
(19, 'charisma', 1);

-- Nutrición (id 20)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(20, 'intelligence', 3),
(20, 'resilience', 3);

-- Primeros Auxilios (id 21)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(21, 'resilience', 4),
(21, 'charisma', 2);

-- Anatomía (id 22)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(22, 'intelligence', 6),
(22, 'resilience', 2);

-- Fisioterapia (id 23)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(23, 'intelligence', 4),
(23, 'resilience', 3),
(23, 'charisma', 1);

-- Salud Mental (id 24)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(24, 'charisma', 4),
(24, 'resilience', 3);

-- Farmacología (id 25)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(25, 'intelligence', 6),
(25, 'resilience', 2);

-- Diagnóstico (id 26)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(26, 'intelligence', 8),
(26, 'resilience', 3),
(26, 'charisma', 1);

-- Enfermería (id 27)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(27, 'intelligence', 5),
(27, 'resilience', 4),
(27, 'charisma', 2);

-- Biomedicina (id 28)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(28, 'intelligence', 7),
(28, 'resilience', 3),
(28, 'creativity', 2);

-- Construcción (id 29)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(29, 'resilience', 4),
(29, 'intelligence', 2);

-- Albañilería (id 30)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(30, 'resilience', 5),
(30, 'intelligence', 2);

-- Fontanería (id 31)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(31, 'resilience', 4),
(31, 'intelligence', 3);

-- Eléctrica (id 32)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(32, 'intelligence', 5),
(32, 'resilience', 3);

-- Carpintería (id 33)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(33, 'resilience', 4),
(33, 'creativity', 2),
(33, 'intelligence', 1);

-- Seguridad Construcción (id 34)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(34, 'resilience', 3),
(34, 'intelligence', 2);

-- Planos (id 35)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(35, 'intelligence', 5),
(35, 'creativity', 2),
(35, 'resilience', 1);

-- Hormigón (id 36)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(36, 'intelligence', 6),
(36, 'resilience', 4);

-- HVAC (id 37)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(37, 'intelligence', 5),
(37, 'resilience', 3);

-- Gestión Proyectos Construcción (id 38)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(38, 'intelligence', 6),
(38, 'charisma', 4),
(38, 'resilience', 3),
(38, 'finances', 2);

-- Negocios (id 39-48)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(39, 'intelligence', 2), (39, 'charisma', 2), (39, 'finances', 2),
(40, 'charisma', 4), (40, 'creativity', 3), (40, 'finances', 2),
(41, 'intelligence', 2), (41, 'finances', 4),
(42, 'charisma', 5), (42, 'resilience', 3), (42, 'intelligence', 2),
(43, 'intelligence', 4), (43, 'charisma', 4), (43, 'creativity', 3), (43, 'finances', 3), (43, 'resilience', 3),
(44, 'intelligence', 3), (44, 'finances', 3),
(45, 'intelligence', 4), (45, 'charisma', 2), (45, 'resilience', 2), (45, 'finances', 2),
(46, 'charisma', 4), (46, 'resilience', 2), (46, 'finances', 2),
(47, 'charisma', 5), (47, 'intelligence', 2), (47, 'resilience', 2),
(48, 'intelligence', 6), (48, 'charisma', 3), (48, 'finances', 4), (48, 'resilience', 2);

-- Creativo (id 49-63)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
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
(63, 'creativity', 6), (63, 'intelligence', 4), (63, 'resilience', 2);

-- Social (id 64-76)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
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
(76, 'intelligence', 5), (76, 'charisma', 5), (76, 'resilience', 3);

-- Ciencia (id 77-89)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
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
(89, 'intelligence', 2), (89, 'resilience', 2);

-- Hospitalidad (id 90-103)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
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
(103, 'creativity', 3), (103, 'charisma', 2), (103, 'resilience', 1);

-- Educación (id 104-105)
INSERT INTO formation_stat_rewards (formation_id, stat_name, reward_amount) VALUES
(104, 'intelligence', 15), (104, 'resilience', 10), (104, 'charisma', 5), (104, 'creativity', 5), (104, 'finances', 5),
(105, 'intelligence', 10), (105, 'resilience', 5), (105, 'charisma', 3), (105, 'creativity', 2);

-- =====================================================
-- 5. INSERTAR CARRERAS PERMITIDAS (allowed_careers)
-- =====================================================
-- Tecnología (id 5-18)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(5, 'TECHNOLOGY'), (5, 'SCIENCE'),
(6, 'TECHNOLOGY'), (6, 'CREATIVE'),
(7, 'TECHNOLOGY'), (7, 'SCIENCE'),
(8, 'TECHNOLOGY'), (8, 'BUSINESS'),
(9, 'TECHNOLOGY'), (9, 'SCIENCE'),
(10, 'TECHNOLOGY'), (10, 'CREATIVE'),
(11, 'TECHNOLOGY'), (11, 'BUSINESS'),
(12, 'TECHNOLOGY'), (12, 'BUSINESS'),
(13, 'TECHNOLOGY'), (13, 'SCIENCE'),
(14, 'TECHNOLOGY'), (14, 'SCIENCE'),
(15, 'TECHNOLOGY'), (15, 'BUSINESS'),
(16, 'TECHNOLOGY'), (16, 'BUSINESS'), (16, 'SCIENCE'),
(17, 'TECHNOLOGY'), (17, 'CREATIVE'),
(18, 'TECHNOLOGY'), (18, 'SCIENCE');

-- Salud (id 19-28)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(19, 'HEALTH'), (19, 'EDUCATION'),
(20, 'HEALTH'), (20, 'EDUCATION'),
(21, 'HEALTH'), (21, 'SPORTS'),
(22, 'HEALTH'), (22, 'SCIENCE'),
(23, 'HEALTH'), (23, 'SPORTS'),
(24, 'HEALTH'), (24, 'EDUCATION'), (24, 'SOCIAL'),
(25, 'HEALTH'), (25, 'SCIENCE'),
(26, 'HEALTH'), (26, 'SCIENCE'),
(27, 'HEALTH'), (27, 'SOCIAL'),
(28, 'HEALTH'), (28, 'SCIENCE'), (28, 'TECHNOLOGY');

-- Construcción (id 29-38)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(29, 'CONSTRUCTION'), (29, 'TECHNOLOGY'),
(30, 'CONSTRUCTION'),
(31, 'CONSTRUCTION'), (31, 'TECHNOLOGY'),
(32, 'CONSTRUCTION'), (32, 'TECHNOLOGY'),
(33, 'CONSTRUCTION'), (33, 'TECHNOLOGY'),
(34, 'CONSTRUCTION'), (34, 'TECHNOLOGY'),
(35, 'CONSTRUCTION'), (35, 'TECHNOLOGY'),
(36, 'CONSTRUCTION'),
(37, 'CONSTRUCTION'), (37, 'TECHNOLOGY'),
(38, 'CONSTRUCTION'), (38, 'BUSINESS');

-- Negocios (id 39-48)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(39, 'BUSINESS'), (39, 'OTHER'),
(40, 'BUSINESS'),
(41, 'BUSINESS'), (41, 'OTHER'),
(42, 'BUSINESS'), (42, 'OTHER'),
(43, 'BUSINESS'), (43, 'OTHER'),
(44, 'BUSINESS'), (44, 'OTHER'),
(45, 'BUSINESS'), (45, 'OTHER'),
(46, 'BUSINESS'), (46, 'OTHER'),
(47, 'BUSINESS'), (47, 'OTHER'),
(48, 'BUSINESS'), (48, 'OTHER');

-- Creativo (id 49-63)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(49, 'CREATIVE'), (49, 'ARTS'),
(50, 'CREATIVE'), (50, 'ARTS'),
(51, 'CREATIVE'), (51, 'ARTS'),
(52, 'CREATIVE'), (52, 'TECHNOLOGY'),
(53, 'CREATIVE'), (53, 'ARTS'), (53, 'OTHER'),
(54, 'CREATIVE'), (54, 'ARTS'),
(55, 'CREATIVE'), (55, 'ARTS'), (55, 'OTHER'),
(56, 'CREATIVE'), (56, 'ARTS'), (56, 'OTHER'),
(57, 'CREATIVE'), (57, 'ARTS'), (57, 'OTHER'),
(58, 'CREATIVE'), (58, 'OTHER'), (58, 'ARTS'),
(59, 'CREATIVE'), (59, 'TECHNOLOGY'),
(60, 'CREATIVE'), (60, 'OTHER'),
(61, 'CREATIVE'), (61, 'ARTS'),
(62, 'CREATIVE'), (62, 'ARTS'), (62, 'OTHER'),
(63, 'CREATIVE'), (63, 'TECHNOLOGY'), (63, 'ARTS');

-- Social (id 64-76)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(64, 'SOCIAL'), (64, 'EDUCATION'),
(65, 'SOCIAL'), (65, 'OTHER'),
(66, 'SOCIAL'), (66, 'EDUCATION'),
(67, 'SOCIAL'), (67, 'OTHER'),
(68, 'SOCIAL'), (68, 'EDUCATION'), (68, 'TECHNOLOGY'),
(69, 'SOCIAL'), (69, 'OTHER'),
(70, 'SOCIAL'), (70, 'EDUCATION'),
(71, 'SOCIAL'), (71, 'EDUCATION'), (71, 'OTHER'),
(72, 'SOCIAL'), (72, 'OTHER'),
(73, 'SOCIAL'), (73, 'EDUCATION'), (73, 'OTHER'),
(74, 'SOCIAL'), (74, 'OTHER'),
(75, 'SOCIAL'), (75, 'EDUCATION'), (75, 'OTHER'),
(76, 'SOCIAL'), (76, 'OTHER');

-- Ciencia (id 77-89)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(77, 'SCIENCE'), (77, 'EDUCATION'),
(78, 'SCIENCE'), (78, 'EDUCATION'),
(79, 'SCIENCE'), (79, 'EDUCATION'),
(80, 'SCIENCE'), (80, 'OTHER'),
(81, 'SCIENCE'), (81, 'OTHER'),
(82, 'SCIENCE'), (82, 'OTHER'),
(83, 'SCIENCE'), (83, 'OTHER'),
(84, 'SCIENCE'), (84, 'OTHER'),
(85, 'SCIENCE'), (85, 'EDUCATION'),
(86, 'SCIENCE'), (86, 'OTHER'),
(87, 'SCIENCE'), (87, 'OTHER'),
(88, 'SCIENCE'), (88, 'OTHER'),
(89, 'SCIENCE'), (89, 'OTHER');

-- Hospitalidad (id 90-103)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(90, 'HOSPITALITY'), (90, 'OTHER'),
(91, 'HOSPITALITY'), (91, 'OTHER'),
(92, 'HOSPITALITY'), (92, 'OTHER'),
(93, 'HOSPITALITY'), (93, 'OTHER'),
(94, 'HOSPITALITY'), (94, 'OTHER'),
(95, 'HOSPITALITY'), (95, 'BUSINESS'),
(96, 'HOSPITALITY'), (96, 'OTHER'),
(97, 'HOSPITALITY'), (97, 'BUSINESS'), (97, 'OTHER'),
(98, 'HOSPITALITY'), (98, 'OTHER'), (98, 'BUSINESS'),
(99, 'HOSPITALITY'), (99, 'OTHER'),
(100, 'HOSPITALITY'), (100, 'BUSINESS'), (100, 'OTHER'),
(101, 'HOSPITALITY'), (101, 'OTHER'), (101, 'BUSINESS'),
(102, 'HOSPITALITY'), (102, 'OTHER'),
(103, 'HOSPITALITY'), (103, 'OTHER');

-- Educación (id 104-105)
INSERT INTO formation_allowed_careers (formation_id, allowed_careers) VALUES
(104, 'TECHNOLOGY'), (104, 'SCIENCE'), (104, 'OTHER'),
(105, 'OTHER');

-- =====================================================
-- 6. INSERTAR EXÁMENES (formation_exams)
-- =====================================================
INSERT INTO formation_exams (formation_id, name, required_hours, difficulty, max_score, mandatory, min_passing_score, max_attempts, active, weight) VALUES
(5, 'Examen final: Introducción a Java', 40, 'BASIC', 100, true, 60, 3, true, 100),
(6, 'Examen final: Desarrollo Web Frontend', 50, 'BASIC', 100, true, 60, 3, true, 100),
(7, 'Examen final: Fundamentos de Ciencia de Datos', 60, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(8, 'Examen final: Seguridad Informática Básica', 35, 'BASIC', 100, true, 60, 3, true, 100),
(9, 'Examen final: Introducción a Python', 45, 'BASIC', 100, true, 60, 3, true, 100),
(10, 'Examen final: Frameworks Frontend (React y Angular)', 60, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(11, 'Examen final: Fundamentos de DevOps', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(12, 'Examen final: Introducción a la Nube', 40, 'BASIC', 100, true, 60, 3, true, 100),
(13, 'Examen final: Inteligencia Artificial Básica', 55, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(14, 'Examen final: Programación Avanzada en Java', 60, 'ADVANCED', 100, true, 70, 3, true, 100),
(15, 'Examen final: Seguridad Web y Ciberseguridad', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(16, 'Examen final: Analítica de Datos y Big Data', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(17, 'Examen final: Desarrollo de Aplicaciones Móviles', 55, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(18, 'Examen final: Robótica y Física Aplicada', 70, 'ADVANCED', 100, true, 70, 3, true, 100),
(19, 'Examen final: Fundamentos de Salud Pública', 40, 'BASIC', 100, true, 60, 3, true, 100),
(20, 'Examen final: Nutrición y Dietética', 35, 'BASIC', 100, true, 60, 3, true, 100),
(21, 'Examen final: Primeros Auxilios y Emergencias', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(22, 'Examen final: Anatomía Humana', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(23, 'Examen final: Introducción a la Fisioterapia', 40, 'BASIC', 100, true, 60, 3, true, 100),
(24, 'Examen final: Salud Mental y Bienestar', 30, 'BASIC', 100, true, 60, 3, true, 100),
(25, 'Examen final: Fundamentos de Farmacología', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(26, 'Examen final: Técnicas de Diagnóstico Médico', 60, 'ADVANCED', 100, true, 70, 3, true, 100),
(27, 'Examen final: Cuidado y Enfermería', 50, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(28, 'Examen final: Introducción a la Biomedicina', 55, 'ADVANCED', 100, true, 70, 3, true, 100),
(29, 'Examen final: Introducción a la Construcción', 40, 'BASIC', 100, true, 60, 3, true, 100),
(30, 'Examen final: Albañilería y Estructuras', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(31, 'Examen final: Fontanería y Sistemas Hidráulicos', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(32, 'Examen final: Instalaciones Eléctricas', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(33, 'Examen final: Carpintería Básica', 30, 'BASIC', 100, true, 60, 3, true, 100),
(34, 'Examen final: Seguridad en la Construcción', 25, 'BASIC', 100, true, 60, 3, true, 100),
(35, 'Examen final: Lectura de Planos y Diagramas', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(36, 'Examen final: Tecnología del Hormigón', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(37, 'Examen final: Instalación de Sistemas HVAC', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(38, 'Examen final: Gestión de Proyectos de Construcción', 60, 'ADVANCED', 100, true, 70, 3, true, 100),
(39, 'Examen final: Introducción a la Administración', 20, 'BASIC', 100, false, 50, 3, true, 100),
(40, 'Examen final: Marketing Digital', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(41, 'Examen final: Finanzas Personales', 25, 'BASIC', 100, false, 50, 3, true, 100),
(42, 'Examen final: Liderazgo y Gestión de Equipos', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(43, 'Examen final: Emprendimiento y Startups', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(44, 'Examen final: Fundamentos de Economía', 20, 'BASIC', 100, false, 50, 3, true, 100),
(45, 'Examen final: Gestión de Proyectos', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(46, 'Examen final: Técnicas de Venta', 25, 'BASIC', 100, false, 50, 3, true, 100),
(47, 'Examen final: Negociación y Resolución de Conflictos', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(48, 'Examen final: Estrategia Empresarial', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(49, 'Examen final: Diseño Gráfico Básico', 25, 'BASIC', 100, false, 50, 3, true, 100),
(50, 'Examen final: Fotografía Digital', 20, 'BASIC', 100, false, 50, 3, true, 100),
(51, 'Examen final: Ilustración Digital', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(52, 'Examen final: Diseño de Interfaces y Experiencia de Usuario', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(53, 'Examen final: Teoría Musical Básica', 20, 'BASIC', 100, false, 50, 3, true, 100),
(54, 'Examen final: Fotografía Avanzada', 40, 'ADVANCED', 100, true, 70, 3, true, 100),
(55, 'Examen final: Edición de Video', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(56, 'Examen final: Escritura Creativa', 25, 'BASIC', 100, false, 50, 3, true, 100),
(57, 'Examen final: Animación Digital', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(58, 'Examen final: Producción Musical', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(59, 'Examen final: Diseño Web Creativo', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(60, 'Examen final: Gestión de Redes Sociales', 25, 'BASIC', 100, false, 50, 3, true, 100),
(61, 'Examen final: Diseño de Interiores', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(62, 'Examen final: Diseño de Moda', 40, 'ADVANCED', 100, true, 70, 3, true, 100),
(63, 'Examen final: Diseño de Videojuegos', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(64, 'Examen final: Fundamentos de Educación', 25, 'BASIC', 100, false, 50, 3, true, 100),
(65, 'Examen final: Psicología Infantil', 30, 'BASIC', 100, false, 50, 3, true, 100),
(66, 'Examen final: Trabajo Social Comunitario', 30, 'BASIC', 100, false, 50, 3, true, 100),
(67, 'Examen final: Psicología Avanzada', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(68, 'Examen final: Tecnología Educativa', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(69, 'Examen final: Terapia y Counseling', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(70, 'Examen final: Educación Especial', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(71, 'Examen final: Políticas Sociales', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(72, 'Examen final: Counseling Avanzado', 45, 'ADVANCED', 100, true, 70, 3, true, 100),
(73, 'Examen final: Trabajo con Jóvenes', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(74, 'Examen final: Desarrollo Comunitario', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(75, 'Examen final: Políticas Educativas', 45, 'ADVANCED', 100, true, 70, 3, true, 100),
(76, 'Examen final: Psicología Comunitaria', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(77, 'Examen final: Biología General', 30, 'BASIC', 100, false, 50, 3, true, 100),
(78, 'Examen final: Química General', 30, 'BASIC', 100, false, 50, 3, true, 100),
(79, 'Examen final: Física General', 30, 'BASIC', 100, false, 50, 3, true, 100),
(80, 'Examen final: Genética Básica', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(81, 'Examen final: Microbiología', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(82, 'Examen final: Química Orgánica', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(83, 'Examen final: Bioquímica', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(84, 'Examen final: Ciencias Ambientales', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(85, 'Examen final: Métodos de Investigación', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(86, 'Examen final: Ingeniería Genética', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(87, 'Examen final: Análisis Químico', 45, 'ADVANCED', 100, true, 70, 3, true, 100),
(88, 'Examen final: Ecología', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(89, 'Examen final: Seguridad en Laboratorio', 20, 'BASIC', 100, false, 50, 3, true, 100),
(90, 'Examen final: Cocina Básica', 30, 'BASIC', 100, false, 50, 3, true, 100),
(91, 'Examen final: Repostería Básica', 25, 'BASIC', 100, false, 50, 3, true, 100),
(92, 'Examen final: Cocina Intermedia', 40, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(93, 'Examen final: Higiene y Seguridad Alimentaria', 20, 'BASIC', 100, false, 50, 3, true, 100),
(94, 'Examen final: Cocina Internacional', 45, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(95, 'Examen final: Gestión de Restaurantes', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(96, 'Examen final: Técnicas de Barista', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(97, 'Examen final: Planificación de Eventos', 35, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(98, 'Examen final: Gestión Turística', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(99, 'Examen final: Presentación de Alimentos', 25, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(100, 'Examen final: Operaciones Hoteleras', 50, 'ADVANCED', 100, true, 70, 3, true, 100),
(101, 'Examen final: Control de Costes de Alimentos', 30, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(102, 'Examen final: Atención al Cliente en Hostelería', 20, 'BASIC', 100, false, 50, 3, true, 100),
(103, 'Examen final: Preparación de Cócteles', 25, 'INTERMEDIATE', 100, true, 65, 3, true, 100),
(104, 'Examen final: Formación Profesional', 2000, 'BASIC', 100, true, 60, 3, true, 100),
(105, 'Examen final: Estudios Secundaria', 2000, 'BASIC', 100, false, 50, 3, true, 100);

-- =====================================================
-- 7. INSERTAR BECAS (scholarships)
-- =====================================================
INSERT INTO scholarships (
    title, description, amount, active, min_xp_required,
    start_date, end_date, education_level, min_grade,
    requires_merit, max_family_income, requires_economic_proof,
    resolution_date, created_at, updated_at
) VALUES
('Beca Excelencia Universitaria',
 'Beca destinada a estudiantes con alto rendimiento académico.',
 2500.00, true, 500,
 '2026-09-01', '2027-06-30', 'university', 8.5,
 true, NULL, false,
 '2026-08-15', CURRENT_DATE, CURRENT_DATE),

('Beca Apoyo Familiar',
 'Ayuda económica para estudiantes con bajos ingresos familiares.',
 3000.00, true, 0,
 '2026-01-01', '2027-06-30', 'secondary', 0,
 false, 18000.00, false,
 '2026-08-20', CURRENT_DATE, CURRENT_DATE),

('Beca Talento Joven',
 'Beca para estudiantes con buen rendimiento y necesidades económicas.',
 4000.00, true, 400,
 '2026-09-01', '2027-06-30', 'vocational', 7.5,
 true, 25000.00, true,
 '2026-08-25', CURRENT_DATE, CURRENT_DATE);

-- =====================================================
-- 8. VERIFICAR DATOS INSERTADOS
-- =====================================================
SELECT '=== FORMACIONES ===' AS '';
SELECT COUNT(*) AS total_formations FROM formations;

SELECT '=== EXÁMENES ===' AS '';
SELECT COUNT(*) AS total_exams FROM formation_exams;

SELECT '=== BECAS ===' AS '';
SELECT COUNT(*) AS total_scholarships FROM scholarships;

SELECT '=== HABILIDADES DESBLOQUEADAS ===' AS '';
SELECT COUNT(*) AS total_skills FROM formation_skills_unlocked;

SELECT '=== STAT REWARDS ===' AS '';
SELECT COUNT(*) AS total_stat_rewards FROM formation_stat_rewards;

SELECT '=== CARRERAS PERMITIDAS ===' AS '';
SELECT COUNT(*) AS total_allowed_careers FROM formation_allowed_careers;

SELECT '=== DÍAS LABORABLES ===' AS '';
SELECT COUNT(*) AS total_working_days FROM course_working_days;