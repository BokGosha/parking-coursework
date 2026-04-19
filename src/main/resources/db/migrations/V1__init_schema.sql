CREATE SEQUENCE IF NOT EXISTS places_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS users_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS rents_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS roles_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE places (
    id         BIGINT PRIMARY KEY,
    number     INTEGER NOT NULL,
    available  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE users (
    id          BIGINT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    total_time  BIGINT
);

CREATE TABLE roles (
    id    BIGINT PRIMARY KEY,
    name  VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id  BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id  BIGINT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE rents (
    id           BIGINT PRIMARY KEY,
    start_date   TIMESTAMP,
    finish_date  TIMESTAMP,
    user_id      BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    place_id     BIGINT NOT NULL REFERENCES places (id) ON DELETE CASCADE
);

CREATE INDEX idx_rents_user_id ON rents (user_id);
CREATE INDEX idx_rents_place_id ON rents (place_id);
