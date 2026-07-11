CREATE TABLE regions (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE types (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE abilities (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE moves (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE pokemon (
    id              BIGSERIAL PRIMARY KEY,
    national_number INTEGER      NOT NULL UNIQUE,
    name            VARCHAR(100) NOT NULL,
    description     VARCHAR(1000),
    image_url       VARCHAR(500),
    generation      INTEGER      NOT NULL,
    has_mega        BOOLEAN      NOT NULL DEFAULT FALSE,
    evolution_stage INTEGER      NOT NULL DEFAULT 1,
    evolves_from_id BIGINT REFERENCES pokemon (id),
    color           VARCHAR(30),
    region_id       BIGINT REFERENCES regions (id),
    created_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX idx_pokemon_number ON pokemon (national_number);

CREATE TABLE pokemon_stats (
    id               BIGSERIAL PRIMARY KEY,
    pokemon_id       BIGINT  NOT NULL UNIQUE REFERENCES pokemon (id) ON DELETE CASCADE,
    hp               INTEGER NOT NULL,
    attack           INTEGER NOT NULL,
    defense          INTEGER NOT NULL,
    special_attack   INTEGER NOT NULL,
    special_defense  INTEGER NOT NULL,
    speed            INTEGER NOT NULL
);

CREATE TABLE pokemon_type (
    pokemon_id BIGINT NOT NULL REFERENCES pokemon (id) ON DELETE CASCADE,
    type_id    BIGINT NOT NULL REFERENCES types (id) ON DELETE RESTRICT,
    PRIMARY KEY (pokemon_id, type_id)
);

CREATE TABLE pokemon_ability (
    pokemon_id BIGINT NOT NULL REFERENCES pokemon (id) ON DELETE CASCADE,
    ability_id BIGINT NOT NULL REFERENCES abilities (id) ON DELETE RESTRICT,
    PRIMARY KEY (pokemon_id, ability_id)
);

CREATE TABLE pokemon_move (
    pokemon_id BIGINT NOT NULL REFERENCES pokemon (id) ON DELETE CASCADE,
    move_id    BIGINT NOT NULL REFERENCES moves (id) ON DELETE RESTRICT,
    PRIMARY KEY (pokemon_id, move_id)
);
