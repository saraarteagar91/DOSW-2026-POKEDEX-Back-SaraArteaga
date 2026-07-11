CREATE TABLE teams (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name       VARCHAR(60) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    UNIQUE (user_id, name)
);

-- Tabla generada por @ElementCollection + @OrderColumn de TeamEntity.pokemonIds (RN-04: máximo 6, validado en código)
CREATE TABLE team_pokemon (
    team_id    BIGINT  NOT NULL REFERENCES teams (id) ON DELETE CASCADE,
    position   INTEGER NOT NULL,
    pokemon_id BIGINT  NOT NULL REFERENCES pokemon (id) ON DELETE RESTRICT,
    PRIMARY KEY (team_id, position)
);

CREATE TABLE favorites (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    pokemon_id BIGINT    NOT NULL REFERENCES pokemon (id) ON DELETE RESTRICT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (user_id, pokemon_id)
);
