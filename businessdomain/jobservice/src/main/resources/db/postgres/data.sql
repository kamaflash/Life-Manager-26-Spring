-- ============================================
-- LIMPIAR TABLAS (PostgreSQL)
-- ============================================
TRUNCATE TABLE job_requirements CASCADE;
TRUNCATE TABLE job_vacancies CASCADE;
TRUNCATE TABLE job_positions CASCADE;
TRUNCATE TABLE companies CASCADE;

-- Reiniciar secuencias
ALTER SEQUENCE IF EXISTS job_vacancies_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS job_requirements_id_seq RESTART WITH 1;

-- ============================================
-- 1. COMPANIES
-- ============================================
INSERT INTO companies (id, name, category, description, location, website, logo_url, contact_email, phone, active, employees_count, founded_year, remote_friendly, internship_available) VALUES
(1, 'TechCorp Solutions', 'TECHNOLOGY', 'Empresa líder en desarrollo de software y soluciones tecnológicas.', 'Madrid, España', 'https://techcorp.es', 'https://example.com/logos/techcorp.png', 'rrhh@techcorp.es', '+34 912 345 678', true, 500, 2010, true, true),
(2, 'DataMind Analytics', 'TECHNOLOGY', 'Consultoría en Big Data e inteligencia artificial.', 'Barcelona, España', 'https://datamind.es', 'https://example.com/logos/datamind.png', 'jobs@datamind.es', '+34 932 345 678', true, 200, 2015, true, true),
(3, 'HealthPlus Medical', 'HEALTH', 'Red de centros médicos y servicios de telemedicina.', 'Valencia, España', 'https://healthplus.es', 'https://example.com/logos/healthplus.png', 'empleo@healthplus.es', '+34 963 456 789', true, 800, 2005, false, true),
(4, 'Wellness Care', 'HEALTH', 'Clínicas de fisioterapia y centros de bienestar.', 'Sevilla, España', 'https://wellnesscare.es', 'https://example.com/logos/wellness.png', 'rrhh@wellnesscare.es', '+34 954 567 890', true, 150, 2012, false, true),
(5, 'Global Finance Group', 'FINANCE', 'Servicios financieros y asesoría de inversiones.', 'Madrid, España', 'https://globalfinance.es', 'https://example.com/logos/globalfinance.png', 'talento@globalfinance.es', '+34 913 456 789', true, 1200, 1995, false, true),
(6, 'Inversa Capital', 'FINANCE', 'Gestión de patrimonios y planificación financiera.', 'Barcelona, España', 'https://inversacapital.es', 'https://example.com/logos/inversa.png', 'empleo@inversacapital.es', '+34 933 567 890', true, 300, 2008, true, true),
(7, 'Constructora del Sur', 'CONSTRUCTION', 'Edificación residencial y obra civil.', 'Sevilla, España', 'https://constructorasur.es', 'https://example.com/logos/construtora.png', 'obras@constructorasur.es', '+34 955 678 901', true, 600, 1988, false, false),
(8, 'Arquitectura XXI', 'CONSTRUCTION', 'Estudio de arquitectura y diseño de interiores.', 'Madrid, España', 'https://arquitecturaxxi.es', 'https://example.com/logos/arquitectura.png', 'estudio@arquitecturaxxi.es', '+34 914 567 890', true, 80, 2015, true, true),
(9, 'EduLearn Academy', 'EDUCATION', 'Plataforma de formación online y cursos profesionales.', 'Valencia, España', 'https://edulearn.es', 'https://example.com/logos/edulearn.png', 'info@edulearn.es', '+34 964 567 890', true, 120, 2016, true, true),
(10, 'Idiomas Sin Fronteras', 'EDUCATION', 'Academia de idiomas y cursos de inmersión lingüística.', 'Barcelona, España', 'https://idiomassinfronteras.es', 'https://example.com/logos/idiomas.png', 'cursos@idiomassinfronteras.es', '+34 934 678 901', true, 60, 2010, false, true),
(11, 'Creativos Asociados', 'CREATIVE', 'Agencia de publicidad y marketing digital.', 'Madrid, España', 'https://creativosasociados.es', 'https://example.com/logos/creativos.png', 'creativos@creativosasociados.es', '+34 915 678 901', true, 90, 2013, true, true),
(12, 'Estudio Visual', 'CREATIVE', 'Producción audiovisual y diseño gráfico.', 'Barcelona, España', 'https://estudiovisual.es', 'https://example.com/logos/visual.png', 'info@estudiovisual.es', '+34 935 678 901', true, 40, 2018, true, true),
(13, 'Solidaridad Social', 'SOCIAL', 'ONG de apoyo a colectivos vulnerables.', 'Madrid, España', 'https://solidaridadsocial.es', 'https://example.com/logos/solidaridad.png', 'voluntariado@solidaridadsocial.es', '+34 916 789 012', true, 50, 2000, false, true),
(14, 'Inclusión Activa', 'SOCIAL', 'Centro de inserción laboral para personas con discapacidad.', 'Sevilla, España', 'https://inclusionactiva.es', 'https://example.com/logos/inclusion.png', 'info@inclusionactiva.es', '+34 956 789 012', true, 30, 2015, false, true),
(15, 'Science Lab Research', 'SCIENCE', 'Laboratorio de investigación biotecnológica.', 'Granada, España', 'https://sciencelab.es', 'https://example.com/logos/sciencelab.png', 'lab@sciencelab.es', '+34 958 789 012', true, 45, 2012, false, true),
(16, 'BioTech Innovations', 'SCIENCE', 'Desarrollo de soluciones biotecnológicas avanzadas.', 'Barcelona, España', 'https://biotechinnovations.es', 'https://example.com/logos/biotech.png', 'rrhh@biotechinnovations.es', '+34 936 789 012', true, 70, 2014, false, true),
(17, 'Hostelería del Sol', 'HOSPITALITY', 'Cadena de hoteles y restaurantes.', 'Málaga, España', 'https://hosteleriadelsol.es', 'https://example.com/logos/hosteleria.png', 'empleo@hosteleriadelsol.es', '+34 952 789 012', true, 350, 1985, false, true),
(18, 'Gourmet Experience', 'HOSPITALITY', 'Grupo de restauración y eventos gastronómicos.', 'Madrid, España', 'https://gourmetexperience.es', 'https://example.com/logos/gourmet.png', 'rrhh@gourmetexperience.es', '+34 917 890 123', true, 180, 2008, false, true),
(19, 'Deporte Total', 'SPORTS', 'Gestión de instalaciones deportivas y eventos.', 'Madrid, España', 'https://deportetotal.es', 'https://example.com/logos/deporte.png', 'info@deportetotal.es', '+34 918 901 234', true, 120, 2010, false, true),
(20, 'Fitness & Health', 'SPORTS', 'Cadena de gimnasios y centros deportivos.', 'Barcelona, España', 'https://fitnesshealth.es', 'https://example.com/logos/fitness.png', 'empleo@fitnesshealth.es', '+34 937 890 123', true, 200, 2012, false, true),
(21, 'Arte y Cultura', 'ARTS', 'Galería de arte y gestión cultural.', 'Madrid, España', 'https://arteycultura.es', 'https://example.com/logos/arte.png', 'info@arteycultura.es', '+34 919 012 345', true, 25, 2015, true, true),
(22, 'Estudio Creativo', 'ARTS', 'Taller de arte y diseño de autor.', 'Valencia, España', 'https://estudiocreativo.es', 'https://example.com/logos/estudio.png', 'arte@estudiocreativo.es', '+34 965 890 123', true, 12, 2018, true, true),
(23, 'StudentJob España', 'OTHER', 'Plataforma de empleo para estudiantes y jóvenes.', 'Madrid, España', 'https://studentjob.es', 'https://example.com/logos/studentjob.png', 'info@studentjob.es', '+34 911 234 567', true, 25, 2015, true, true),
(24, 'Media Jornada Empleo', 'OTHER', 'Bolsa de empleo especializada en trabajos de media jornada.', 'Barcelona, España', 'https://mediajornadaempleo.es', 'https://example.com/logos/mediajornada.png', 'rrhh@mediajornadaempleo.es', '+34 932 345 678', true, 18, 2018, true, true),
(25, 'Trabajos Universitarios', 'OTHER', 'Portal de empleo exclusivo para universitarios.', 'Valencia, España', 'https://trabajosuniversitarios.es', 'https://example.com/logos/trabajosuni.png', 'info@trabajosuniversitarios.es', '+34 963 456 789', true, 15, 2019, true, true),
(26, 'Estudiantes en Prácticas', 'OTHER', 'Agencia de colocación de prácticas profesionales.', 'Sevilla, España', 'https://estudiantesenpracticas.es', 'https://example.com/logos/estudiantespracticas.png', 'empleo@estudiantesenpracticas.es', '+34 954 567 890', true, 12, 2020, true, true),
(27, 'Media Jornada Hostelería', 'OTHER', 'Especialistas en contratación de estudiantes para hostelería.', 'Málaga, España', 'https://mediajornadahosteleria.es', 'https://example.com/logos/hosteleriamedia.png', 'rrhh@mediajornadahosteleria.es', '+34 952 678 901', true, 20, 2017, false, true),
(28, 'Student Retail Jobs', 'OTHER', 'Empleos de fin de semana y media jornada en tiendas.', 'Madrid, España', 'https://studentretailjobs.es', 'https://example.com/logos/retailstudent.png', 'info@studentretailjobs.es', '+34 915 789 012', true, 30, 2016, false, true),
(29, 'Trabajos Flexibles', 'OTHER', 'Bolsa de trabajo flexible para estudiantes.', 'Bilbao, España', 'https://trabajosflexibles.es', 'https://example.com/logos/flexibles.png', 'empleo@trabajosflexibles.es', '+34 944 890 123', true, 14, 2021, true, true),
(30, 'Campus Trabajo', 'OTHER', 'Portal de empleo para estudiantes universitarios.', 'Zaragoza, España', 'https://campustrabajo.es', 'https://example.com/logos/campustrabajo.png', 'info@campustrabajo.es', '+34 976 901 234', true, 10, 2022, true, true);

-- ============================================
-- 2. JOB POSITIONS
-- ============================================
INSERT INTO job_positions (id, title, level, description, category, company_id, active, career_path) VALUES
(1, 'Desarrollador Full Stack', 'JUNIOR', 'Desarrollo de aplicaciones web con React y Spring Boot', 'TECHNOLOGY', 1, true, 'TECHNOLOGY'),
(2, 'Desarrollador Senior', 'SENIOR', 'Liderazgo técnico y arquitectura de soluciones', 'TECHNOLOGY', 1, true, 'TECHNOLOGY'),
(3, 'Data Scientist', 'SEMI_SENIOR', 'Análisis de datos y modelos predictivos', 'TECHNOLOGY', 2, true, 'TECHNOLOGY'),
(4, 'Médico General', 'SENIOR', 'Atención primaria y consulta médica', 'HEALTH', 3, true, 'HEALTH'),
(5, 'Enfermero', 'JUNIOR', 'Cuidado de pacientes y asistencia médica', 'HEALTH', 3, true, 'HEALTH'),
(6, 'Fisioterapeuta', 'JUNIOR', 'Rehabilitación y terapia física', 'HEALTH', 4, true, 'HEALTH'),
(7, 'Asesor Financiero', 'JUNIOR', 'Asesoramiento a clientes y gestión de carteras', 'FINANCE', 5, true, 'BUSINESS'),
(8, 'Analista de Riesgos', 'SEMI_SENIOR', 'Evaluación y gestión de riesgos financieros', 'FINANCE', 5, true, 'BUSINESS'),
(9, 'Gestor de Patrimonios', 'SENIOR', 'Planificación financiera y gestión de inversiones', 'FINANCE', 6, true, 'BUSINESS'),
(10, 'Ingeniero de Obra', 'SEMI_SENIOR', 'Supervisión de obras y gestión de proyectos', 'CONSTRUCTION', 7, true, 'CONSTRUCTION'),
(11, 'Arquitecto', 'SENIOR', 'Diseño y planificación de proyectos arquitectónicos', 'CONSTRUCTION', 8, true, 'CONSTRUCTION'),
(12, 'Diseñador de Interiores', 'JUNIOR', 'Diseño de espacios y selección de materiales', 'CREATIVE', 8, true, 'CREATIVE'),
(13, 'Profesor de Programación', 'JUNIOR', 'Impartición de cursos de desarrollo web', 'EDUCATION', 9, true, 'SOCIAL'),
(14, 'Profesor de Inglés', 'JUNIOR', 'Clases de inglés para adultos y empresas', 'EDUCATION', 10, true, 'SOCIAL'),
(15, 'Diseñador Gráfico', 'JUNIOR', 'Diseño de materiales visuales y branding', 'CREATIVE', 11, true, 'CREATIVE'),
(16, 'Editor de Video', 'JUNIOR', 'Edición y postproducción de contenido audiovisual', 'CREATIVE', 12, true, 'CREATIVE'),
(17, 'Trabajador Social', 'JUNIOR', 'Atención a colectivos vulnerables', 'SOCIAL', 13, true, 'SOCIAL'),
(18, 'Coordinador de Proyectos Sociales', 'SEMI_SENIOR', 'Gestión de proyectos de inclusión social', 'SOCIAL', 14, true, 'SOCIAL'),
(19, 'Técnico de Laboratorio', 'JUNIOR', 'Análisis y experimentación en laboratorio', 'SCIENCE', 15, true, 'SCIENCE'),
(20, 'Investigador', 'SENIOR', 'Investigación y desarrollo de proyectos científicos', 'SCIENCE', 16, true, 'SCIENCE'),
(21, 'Camarero', 'JUNIOR', 'Atención al cliente en restaurante', 'HOSPITALITY', 17, true, 'BUSINESS'),
(22, 'Cocinero', 'JUNIOR', 'Preparación de platos en cocina', 'HOSPITALITY', 17, true, 'BUSINESS'),
(23, 'Recepcionista', 'JUNIOR', 'Atención al público y gestión de reservas', 'HOSPITALITY', 18, true, 'BUSINESS'),
(24, 'Monitor Deportivo', 'JUNIOR', 'Coordinación de actividades deportivas', 'SPORTS', 19, true, 'SPORTS'),
(25, 'Entrenador Personal', 'SEMI_SENIOR', 'Entrenamiento personalizado y planes de ejercicio', 'SPORTS', 20, true, 'SPORTS'),
(26, 'Curador de Arte', 'SEMI_SENIOR', 'Gestión de exposiciones y colecciones artísticas', 'ARTS', 21, true, 'CREATIVE'),
(27, 'Artista Plástico', 'JUNIOR', 'Creación de obras de arte', 'ARTS', 22, true, 'CREATIVE'),
(28, 'Asesor de Empleo', 'JUNIOR', 'Orientación laboral para estudiantes', 'OTHER', 23, true, 'SOCIAL'),
(29, 'Técnico de Selección', 'JUNIOR', 'Procesos de selección para media jornada', 'OTHER', 24, true, 'BUSINESS'),
(30, 'Community Manager', 'JUNIOR', 'Gestión de redes sociales y contenido digital', 'OTHER', 25, true, 'CREATIVE'),
(31, 'Gestor de Prácticas', 'JUNIOR', 'Coordinación de programas de prácticas profesionales', 'OTHER', 26, true, 'SOCIAL'),
(32, 'Ayudante de Cocina', 'JUNIOR', 'Apoyo en cocina para media jornada', 'OTHER', 27, true, 'BUSINESS'),
(33, 'Dependiente', 'JUNIOR', 'Atención al cliente en tienda', 'OTHER', 28, true, 'BUSINESS'),
(34, 'Asistente Virtual', 'JUNIOR', 'Soporte administrativo remoto', 'OTHER', 29, true, 'BUSINESS'),
(35, 'Becario Universitario', 'INTERN', 'Prácticas en administración y gestión', 'OTHER', 30, true, 'BUSINESS'),
(36, 'Repartidor', 'JUNIOR', 'Reparto de pedidos en zona urbana', 'OTHER', 24, true, 'BUSINESS'),
(37, 'Fregaplatos', 'JUNIOR', 'Limpieza y organización de cocina', 'OTHER', 27, true, 'BUSINESS'),
(38, 'Repartidor de Publicidad', 'JUNIOR', 'Reparto de folletos y publicidad', 'OTHER', 26, true, 'BUSINESS'),
(39, 'Ayudante de Reparto', 'JUNIOR', 'Ayuda en carga y reparto de mercancía', 'OTHER', 24, true, 'BUSINESS'),
(40, 'Limpiador', 'JUNIOR', 'Limpieza de instalaciones', 'OTHER', 27, true, 'BUSINESS'),
(41, 'Mozo de Almacén', 'JUNIOR', 'Preparación de pedidos y organización de almacén', 'OTHER', 29, true, 'BUSINESS'),
(42, 'Camarero de Eventos', 'JUNIOR', 'Servicio en eventos y banquetes', 'HOSPITALITY', 18, true, 'BUSINESS');

-- ============================================
-- 3. JOB VACANCIES (CORREGIDO - DATOS REALISTAS)
-- ============================================
INSERT INTO job_vacancies (id, position_id, min_salary, max_salary, contract_type, work_modality, available_slots, weekly_hours, active, visa_sponsorship, opening_date, start_time, end_time, description) VALUES
-- ===== TRABAJOS PROFESIONALES (FULL_TIME - 40h) =====
(1, 1, 24000, 32000, 'FULL_TIME', 'HYBRID', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Desarrollador Full Stack con React y Spring Boot. Proyectos innovadores en equipo ágil.'),
(2, 2, 42000, 55000, 'FULL_TIME', 'REMOTE', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Desarrollador Senior con liderazgo técnico. Experiencia en arquitectura de microservicios.'),
(3, 3, 32000, 42000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Data Scientist para análisis predictivo con Python y SQL.'),
(4, 4, 50000, 65000, 'FULL_TIME', 'ONSITE', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Médico General para atención primaria en centro de salud público.'),
(5, 5, 24000, 30000, 'FULL_TIME', 'ONSITE', 5, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Enfermero/a para planta de hospitalización. Turnos rotativos.'),
(6, 6, 22000, 28000, 'FULL_TIME', 'ONSITE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '15:00:00', 'Fisioterapeuta en clínica privada. Jornada intensiva de mañana.'),
(7, 7, 24000, 32000, 'FULL_TIME', 'ONSITE', 4, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Asesor Financiero para oficina bancaria. Atención al cliente y gestión de carteras.'),
(8, 8, 32000, 42000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Analista de Riesgos financieros. Conocimientos en modelos estadísticos.'),
(9, 9, 40000, 55000, 'FULL_TIME', 'HYBRID', 1, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Gestor de Patrimonios para banca privada. Experiencia mínima 5 años.'),
(10, 10, 30000, 40000, 'FULL_TIME', 'ONSITE', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Ingeniero de Obra para supervisión de proyectos de construcción residencial.'),
(11, 11, 32000, 42000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Arquitecto para estudio de arquitectura. Proyectos residenciales y comerciales.'),
(12, 12, 20000, 26000, 'FULL_TIME', 'REMOTE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Diseñador de Interiores con visitas puntuales a obra. Portfolio requerido.'),
(13, 13, 22000, 28000, 'FULL_TIME', 'REMOTE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Profesor de Programación para academia online. Clases de Java y Python.'),
(14, 14, 20000, 26000, 'FULL_TIME', 'ONSITE', 4, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Profesor de Inglés nativo o bilingüe para empresa de formación.'),
(15, 15, 20000, 26000, 'FULL_TIME', 'REMOTE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Diseñador Gráfico con dominio de Adobe Suite para agencia de publicidad.'),
(16, 16, 20000, 26000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Editor de Video para productora audiovisual. Premiere y After Effects.'),
(17, 17, 20000, 26000, 'FULL_TIME', 'ONSITE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Trabajador Social para ONG. Atención a colectivos en riesgo de exclusión.'),
(18, 18, 26000, 32000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Coordinador de Proyectos Sociales. Experiencia en gestión de equipos.'),
(19, 19, 18000, 24000, 'FULL_TIME', 'ONSITE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '15:00:00', 'Técnico de Laboratorio para análisis clínicos. Jornada intensiva.'),
(20, 20, 30000, 42000, 'FULL_TIME', 'ONSITE', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Investigador en biotecnología. Doctorado valorado. Contrato por proyecto.'),
(21, 21, 12000, 16000, 'PART_TIME', 'ONSITE', 6, 20, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Camarero/a para restaurante. Turno de mañana. Experiencia no requerida.'),
(22, 22, 18000, 24000, 'FULL_TIME', 'ONSITE', 4, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Cocinero/a con experiencia en cocina mediterránea.'),
(23, 23, 16000, 20000, 'FULL_TIME', 'ONSITE', 4, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Recepcionista de hotel. Se valora inglés y atención al cliente.'),
(24, 24, 10000, 14000, 'PART_TIME', 'ONSITE', 4, 20, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Monitor Deportivo para actividades dirigidas en gimnasio.'),
(25, 25, 22000, 30000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Entrenador Personal certificado. Planes de entrenamiento individualizados.'),
(26, 26, 24000, 32000, 'FULL_TIME', 'ONSITE', 1, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Curador de Arte para galería. Gestión de exposiciones temporales.'),
(27, 27, 14000, 18000, 'PART_TIME', 'REMOTE', 2, 20, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Artista Plástico freelance para proyectos por encargo. Portfolio requerido.'),
(28, 28, 18000, 24000, 'FULL_TIME', 'REMOTE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Asesor de Empleo para orientación laboral de estudiantes.'),
(29, 29, 18000, 24000, 'FULL_TIME', 'REMOTE', 3, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Técnico de Selección para agencia de empleo temporal.'),
(30, 30, 18000, 24000, 'FULL_TIME', 'REMOTE', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Community Manager para gestión de redes sociales de pymes.'),
(31, 31, 20000, 26000, 'FULL_TIME', 'HYBRID', 2, 40, true, false, CURRENT_DATE, '08:00:00', '17:00:00', 'Gestor de Prácticas universitarias. Coordinación con empresas y universidades.'),

-- ===== TRABAJOS DE FIN DE SEMANA (PART_TIME - 15-20h) =====
-- Salarios proporcionales: 15-20h/sem ≈ 37.5%-50% del salario de 40h
(32, 32, 7000, 9000, 'PART_TIME', 'ONSITE', 4, 16, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Ayudante de Cocina para restaurante. Turno de mañana sábados y domingos. Ideal para estudiantes.'),
(33, 33, 7500, 10000, 'PART_TIME', 'ONSITE', 5, 16, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Dependiente para tienda de ropa. Sábados y domingos por la mañana. Sin experiencia requerida.'),
(34, 34, 8000, 11000, 'PART_TIME', 'REMOTE', 3, 16, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Asistente Virtual administrativo. Trabajo remoto sábados y domingos por la mañana.'),
(35, 35, 6000, 8000, 'INTERNSHIP', 'HYBRID', 4, 16, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Becario Universitario para apoyo administrativo. Sábados y domingos. Convenio de prácticas.'),
(36, 36, 8000, 11000, 'PART_TIME', 'ONSITE', 8, 20, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Repartidor con bici o moto propia. Zona centro ciudad. Fines de semana. Pago por entrega + base.'),
(37, 37, 6500, 8500, 'PART_TIME', 'ONSITE', 6, 16, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Ayudante de cocina (fregaplatos) para hotel. Sábados y domingos. Comida incluida.'),
(38, 38, 5500, 7500, 'PART_TIME', 'ONSITE', 10, 15, true, false, CURRENT_DATE, '08:00:00', '11:00:00', 'Reparto de folletos publicitarios. 3 horas sábados y domingos. Ideal para estudiantes.'),
(39, 39, 7500, 10000, 'PART_TIME', 'ONSITE', 5, 20, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Ayudante de Reparto para empresa de mensajería. Carga y descarga. Fines de semana.'),
(40, 40, 7000, 9000, 'PART_TIME', 'ONSITE', 4, 15, true, false, CURRENT_DATE, '08:00:00', '11:00:00', 'Limpieza de oficinas. Sábados y domingos temprano. 3 horas/día. Formación incluida.'),
(41, 41, 8000, 11000, 'PART_TIME', 'ONSITE', 6, 20, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Mozo de Almacén para preparación de pedidos online. Sábados y domingos.'),
(42, 42, 7000, 9000, 'PART_TIME', 'ONSITE', 8, 16, true, false, CURRENT_DATE, '08:00:00', '12:00:00', 'Camarero/a para eventos y banquetes. Sábados y domingos. Formación incluida. Sin experiencia.');
-- ============================================
-- CREAR TABLA PARA working_days (ElementCollection)
-- ============================================
CREATE TABLE IF NOT EXISTS job_vacancies_working_days (
    job_vacancy_id BIGINT NOT NULL,
    working_days VARCHAR(50),
    FOREIGN KEY (job_vacancy_id) REFERENCES job_vacancies(id) ON DELETE CASCADE
);

-- Crear índice para mejor rendimiento
CREATE INDEX IF NOT EXISTS idx_job_vacancies_working_days_vacancy_id ON job_vacancies_working_days(job_vacancy_id);

-- ============================================
-- INSERT PARA working_days (para los registros 32-42)
-- ============================================
INSERT INTO job_vacancies_working_days (job_vacancy_id, working_days) VALUES
(32, 'SATURDAY'),
(32, 'SUNDAY'),
(33, 'SATURDAY'),
(33, 'SUNDAY'),
(34, 'SATURDAY'),
(34, 'SUNDAY'),
(35, 'SATURDAY'),
(35, 'SUNDAY'),
(36, 'SATURDAY'),
(36, 'SUNDAY'),
(37, 'SATURDAY'),
(37, 'SUNDAY'),
(38, 'SATURDAY'),
(38, 'SUNDAY'),
(39, 'SATURDAY'),
(39, 'SUNDAY'),
(40, 'SATURDAY'),
(40, 'SUNDAY'),
(41, 'SATURDAY'),
(41, 'SUNDAY'),
(42, 'SATURDAY'),
(42, 'SUNDAY');

-- ============================================
-- 4. JOB REQUIREMENTS
-- ============================================
INSERT INTO job_requirements (vacancy_id, type, skill_key, min_value, mandatory) VALUES
(1, 'SKILL', 'basic_programming', 2, true),
(1, 'SKILL', 'logic_thinking', 1, true),
(1, 'STAT', 'intelligence', 30, true),
(1, 'EDUCATION', 'high_school', 1, true),
(2, 'SKILL', 'advanced_programming', 3, true),
(2, 'SKILL', 'java_advanced', 2, true),
(2, 'EXPERIENCE_YEARS', 'experience', 3, true),
(2, 'STAT', 'intelligence', 50, true),
(3, 'SKILL', 'data_analysis', 2, true),
(3, 'SKILL', 'statistics', 2, true),
(3, 'STAT', 'intelligence', 40, true),
(4, 'SKILL', 'medical_diagnostics', 3, true),
(4, 'EDUCATION', 'university', 1, true),
(4, 'EXPERIENCE_YEARS', 'experience', 2, true),
(5, 'SKILL', 'nursing_basics', 2, true),
(5, 'SKILL', 'first_aid', 2, true),
(5, 'EDUCATION', 'vocational', 1, true),
(6, 'SKILL', 'physiotherapy_basics', 2, true),
(6, 'SKILL', 'rehabilitation', 1, true),
(6, 'EDUCATION', 'vocational', 1, true),
(7, 'SKILL', 'financial_other', 2, true),
(7, 'STAT', 'charisma', 30, true),
(7, 'EDUCATION', 'high_school', 1, true),
(8, 'SKILL', 'statistics', 3, true),
(8, 'SKILL', 'big_data', 2, true),
(8, 'EDUCATION', 'university', 1, true),
(9, 'SKILL', 'business_strategy', 3, true),
(9, 'SKILL', 'negotiation', 3, true),
(9, 'EXPERIENCE_YEARS', 'experience', 5, true),
(10, 'SKILL', 'construction_basics', 3, true),
(10, 'SKILL', 'blueprint_reading', 2, true),
(10, 'EXPERIENCE_YEARS', 'experience', 2, true),
(11, 'SKILL', 'technical_drawing', 3, true),
(11, 'SKILL', 'project_management', 2, true),
(11, 'EDUCATION', 'university', 1, true),
(12, 'SKILL', 'interior_design', 2, true),
(12, 'SKILL', 'basic_graphic_design', 1, true),
(12, 'STAT', 'creativity', 30, true),
(13, 'SKILL', 'basic_programming', 2, true),
(13, 'SKILL', 'communication', 2, true),
(13, 'EDUCATION', 'high_school', 1, true),
(14, 'SKILL', 'languages', 3, true),
(14, 'SKILL', 'communication', 2, true),
(14, 'EDUCATION', 'high_school', 1, true),
(15, 'SKILL', 'basic_graphic_design', 2, true),
(15, 'STAT', 'creativity', 30, true),
(16, 'SKILL', 'video_editing', 2, true),
(16, 'SKILL', 'creative_writing', 1, true),
(17, 'SKILL', 'communication', 2, true),
(17, 'SKILL', 'community_work_basic', 1, true),
(18, 'SKILL', 'project_management', 2, true),
(18, 'SKILL', 'leadership', 2, true),
(18, 'EXPERIENCE_YEARS', 'experience', 2, true),
(19, 'SKILL', 'basic_chemistry', 2, true),
(19, 'SKILL', 'lab_techniques', 1, true),
(20, 'SKILL', 'research_methods', 3, true),
(20, 'EDUCATION', 'university', 1, true),
(21, 'SKILL', 'customer_service', 2, true),
(22, 'SKILL', 'basic_cooking', 2, true),
(23, 'SKILL', 'customer_service', 2, true),
(23, 'SKILL', 'communication', 1, true),
(24, 'SKILL', 'sports', 2, true),
(24, 'SKILL', 'teamwork', 2, true),
(25, 'SKILL', 'fitness', 3, true),
(25, 'SKILL', 'leadership', 2, true),
(26, 'SKILL', 'art', 3, true),
(26, 'SKILL', 'management', 2, true),
(27, 'SKILL', 'art', 2, true),
(27, 'STAT', 'creativity', 40, true),
(28, 'SKILL', 'communication', 2, true),
(28, 'SKILL', 'counseling_skills', 1, true),
(29, 'SKILL', 'recruitment', 2, true),
(29, 'SKILL', 'communication', 2, true),
(30, 'SKILL', 'social_media_management', 2, true),
(30, 'SKILL', 'creative_writing', 2, true),
(31, 'SKILL', 'project_management', 2, true),
(31, 'SKILL', 'organization', 2, true),
(32, 'SKILL', 'basic_cooking', 1, true),
(33, 'SKILL', 'customer_service', 2, true),
(34, 'SKILL', 'office', 2, true),
(34, 'SKILL', 'communication', 2, true),
(35, 'SKILL', 'office', 1, false),
(35, 'EDUCATION', 'high_school', 1, false);

-- ============================================
-- IRPF - SOLO TABLAS (SIN FUNCIONES NI TRIGGERS)
-- ============================================

-- 5. TABLA DE RETENCIONES
CREATE TABLE IF NOT EXISTS tax_withholdings (
    id BIGSERIAL PRIMARY KEY,
    payroll_id BIGINT NOT NULL,
    character_id BIGINT NOT NULL,
    account_id BIGINT,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    gross_income DECIMAL(12,2) NOT NULL,
    irpf_rate DECIMAL(5,2) NOT NULL,
    irpf_withheld DECIMAL(12,2) NOT NULL,
    accumulated_tax_base DECIMAL(12,2),
    accumulated_withheld DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_withholdings_character_year ON tax_withholdings(character_id, year);

-- 6. TABLA DE DECLARACIONES
CREATE TABLE IF NOT EXISTS tax_filings (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    account_id BIGINT,
    tax_year INTEGER NOT NULL,
    total_gross_income DECIMAL(12,2) DEFAULT 0,
    total_deductions DECIMAL(12,2) DEFAULT 0,
    taxable_base DECIMAL(12,2) DEFAULT 0,
    calculated_tax DECIMAL(12,2) DEFAULT 0,
    total_withheld DECIMAL(12,2) DEFAULT 0,
    result DECIMAL(12,2) DEFAULT 0,
    result_type VARCHAR(20) DEFAULT 'DRAFT',
    status VARCHAR(20) DEFAULT 'DRAFT',
    filing_date DATE,
    payment_deadline DATE,
    amount_to_pay DECIMAL(12,2) DEFAULT 0,
    amount_to_receive DECIMAL(12,2) DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_fractionated BOOLEAN DEFAULT FALSE,
    number_of_installments INTEGER,
    installment_amount DECIMAL(12,2),
    submitted_at TIMESTAMP,
    submitted_by VARCHAR(100),
    processed_at TIMESTAMP,
    payment_processed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_filings_character_year ON tax_filings(character_id, tax_year);

-- 7. TABLA DE PERÍODOS DE DECLARACIÓN
CREATE TABLE IF NOT EXISTS tax_filing_periods (
    id BIGSERIAL PRIMARY KEY,
    tax_year INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    filing_start_date TIMESTAMP NOT NULL,
    filing_end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'UPCOMING',
    minimum_taxable DECIMAL(12,2) DEFAULT 22000,
    late_filing_allowed BOOLEAN DEFAULT TRUE,
    late_filing_penalty DECIMAL(5,2) DEFAULT 5,
    applicable_rules TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_filing_periods_status ON tax_filing_periods(status);

-- 8. TABLA DE NOTIFICACIONES
CREATE TABLE IF NOT EXISTS tax_notifications (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    tax_filing_id BIGINT,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    amount DECIMAL(12,2),
    scheduled_at TIMESTAMP,
    sent_at TIMESTAMP,
    read_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_notifications_character ON tax_notifications(character_id);

-- 9. CAMPAÑA ACTIVA (INSERT SIMPLE, SIN ON CONFLICT)
INSERT INTO tax_filing_periods (
    tax_year, name, filing_start_date, filing_end_date, status,
    minimum_taxable, late_filing_allowed, late_filing_penalty,
    applicable_rules, created_at, updated_at
) VALUES (
    2026, 'Campaña Renta 2026',
    '2026-04-01 00:00:00', '2026-06-30 23:59:59',
    'ACTIVE', 22000, true, 5.00,
    '{"tramos": [[0,12450,19], [12451,20200,24], [20201,35200,30], [35201,60000,37], [60001,300000,45]]}',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- 10. CAMPAÑAS PASADAS (INSERT SIMPLE)
INSERT INTO tax_filing_periods (
    tax_year, name, filing_start_date, filing_end_date, status,
    minimum_taxable, late_filing_allowed, late_filing_penalty,
    applicable_rules, created_at, updated_at
) VALUES
(2025, 'Campaña Renta 2025', '2025-04-01 00:00:00', '2025-06-30 23:59:59', 'CLOSED', 22000, true, 5.00, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2024, 'Campaña Renta 2024', '2024-04-01 00:00:00', '2024-06-30 23:59:59', 'CLOSED', 22000, true, 5.00, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- IRPF - SOLO TABLAS (SIN FUNCIONES NI TRIGGERS)
-- ============================================

-- 5. TABLA DE RETENCIONES
CREATE TABLE IF NOT EXISTS tax_withholdings (
    id BIGSERIAL PRIMARY KEY,
    payroll_id BIGINT NOT NULL,
    character_id BIGINT NOT NULL,
    account_id BIGINT,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    gross_income DECIMAL(12,2) NOT NULL,
    irpf_rate DECIMAL(5,2) NOT NULL,
    irpf_withheld DECIMAL(12,2) NOT NULL,
    accumulated_tax_base DECIMAL(12,2),
    accumulated_withheld DECIMAL(12,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_withholdings_character_year ON tax_withholdings(character_id, year);

-- 6. TABLA DE DECLARACIONES
CREATE TABLE IF NOT EXISTS tax_filings (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    account_id BIGINT,
    tax_year INTEGER NOT NULL,
    total_gross_income DECIMAL(12,2) DEFAULT 0,
    total_deductions DECIMAL(12,2) DEFAULT 0,
    taxable_base DECIMAL(12,2) DEFAULT 0,
    calculated_tax DECIMAL(12,2) DEFAULT 0,
    total_withheld DECIMAL(12,2) DEFAULT 0,
    result DECIMAL(12,2) DEFAULT 0,
    result_type VARCHAR(20) DEFAULT 'DRAFT',
    status VARCHAR(20) DEFAULT 'DRAFT',
    filing_date DATE,
    payment_deadline DATE,
    amount_to_pay DECIMAL(12,2) DEFAULT 0,
    amount_to_receive DECIMAL(12,2) DEFAULT 0,
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_fractionated BOOLEAN DEFAULT FALSE,
    number_of_installments INTEGER,
    installment_amount DECIMAL(12,2),
    submitted_at TIMESTAMP,
    submitted_by VARCHAR(100),
    processed_at TIMESTAMP,
    payment_processed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_filings_character_year ON tax_filings(character_id, tax_year);

-- 7. TABLA DE PERÍODOS DE DECLARACIÓN
CREATE TABLE IF NOT EXISTS tax_filing_periods (
    id BIGSERIAL PRIMARY KEY,
    tax_year INTEGER NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    filing_start_date TIMESTAMP NOT NULL,
    filing_end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'UPCOMING',
    minimum_taxable DECIMAL(12,2) DEFAULT 22000,
    late_filing_allowed BOOLEAN DEFAULT TRUE,
    late_filing_penalty DECIMAL(5,2) DEFAULT 5,
    applicable_rules TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_filing_periods_status ON tax_filing_periods(status);

-- 8. TABLA DE NOTIFICACIONES
CREATE TABLE IF NOT EXISTS tax_notifications (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    tax_filing_id BIGINT,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    amount DECIMAL(12,2),
    scheduled_at TIMESTAMP,
    sent_at TIMESTAMP,
    read_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_tax_notifications_character ON tax_notifications(character_id);

-- 9. CAMPAÑAS (INSERTAR SOLO UNA VEZ)
-- Primero, eliminar si existen datos previos (para pruebas limpias)
DELETE FROM tax_filing_periods WHERE tax_year IN (2024, 2025, 2026);

-- Insertar todas las campañas de una vez
INSERT INTO tax_filing_periods (
    tax_year, name, filing_start_date, filing_end_date, status,
    minimum_taxable, late_filing_allowed, late_filing_penalty,
    applicable_rules, created_at, updated_at
) VALUES
(2024, 'Campaña Renta 2024', '2024-04-01 00:00:00', '2024-06-30 23:59:59', 'CLOSED', 22000, true, 5.00, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2025, 'Campaña Renta 2025', '2025-04-01 00:00:00', '2025-06-30 23:59:59', 'CLOSED', 22000, true, 5.00, '{}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2026, 'Campaña Renta 2026', '2026-04-01 00:00:00', '2026-07-31 23:59:59', 'ACTIVE', 22000, true, 5.00, '{"tramos": [[0,12450,19], [12451,20200,24], [20201,35200,30], [35201,60000,37], [60001,300000,45]]}', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);