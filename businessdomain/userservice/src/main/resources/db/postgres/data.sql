-- data.sql
-- Insertar usuario con la contraseña ya encriptada (BCrypt)

-- data.sql - Insertar 3 usuarios
INSERT INTO users (id, username, email, password, created_at, status)
VALUES
    (
        1,
        'toni@toni.com',
        'greenpower89@gmail.com',
        '$2a$10$VhPVHRhJBOHCq8U.hZdmJe2r8MBlzGO0n5a4NqQ1caFHGrxLokiQu',
        '2026-04-28T18:07:58.7761793',
        true
    ),
    (
        2,
        'Kamaflash',
        'kamaflash@gmail.com',
        '$2a$10$VhPVHRhJBOHCq8U.hZdmJe2r8MBlzGO0n5a4NqQ1caFHGrxLokiQu',
        '2026-04-28T18:10:00.0000000',
        true
    ),
    (
        3,
        'artadapt',
        'artadapt@gmail.com',
        '$2a$10$VhPVHRhJBOHCq8U.hZdmJe2r8MBlzGO0n5a4NqQ1caFHGrxLokiQu',
        '2026-04-28T18:12:00.0000000',
        true
    );

-- Si quieres que el id sea autogenerado, usa:
-- INSERT INTO users (username, email, password, created_at, status)
-- VALUES ('toni@toni.com', 'greenpower89@gmail.com', '$2a$10$VhPVHRhJBOHCq8U.hZdmJe2r8MBlzGO0n5a4NqQ1caFHGrxLokiQu', '2026-04-28T18:07:58.7761793', true);