CREATE TABLE users (
    id                    BIGSERIAL PRIMARY KEY,
    username              VARCHAR(30)  NOT NULL UNIQUE,
    email                 VARCHAR(150) NOT NULL UNIQUE,
    password_hash         VARCHAR(255),
    role                  VARCHAR(20)  NOT NULL,
    provider              VARCHAR(20)  NOT NULL,
    avatar_url            VARCHAR(500),
    enabled               BOOLEAN      NOT NULL DEFAULT TRUE,
    viewed_pokemon_count  INTEGER      NOT NULL DEFAULT 0,
    created_at            TIMESTAMP    NOT NULL DEFAULT now()
);
