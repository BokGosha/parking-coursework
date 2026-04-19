INSERT INTO places (id, number, available)
VALUES
    (1, 1, true),
    (2, 2, true),
    (3, 3, true),
    (4, 4, true),
    (5, 5, true),
    (6, 6, true),
    (7, 7, true),
    (8, 8, true),
    (9, 9, true),
    (10, 10, true);

SELECT setval('places_sequence', (SELECT COALESCE(MAX(id), 0) FROM places));

INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

SELECT setval('roles_seq', (SELECT COALESCE(MAX(id), 0) FROM roles));
