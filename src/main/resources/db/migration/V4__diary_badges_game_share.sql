CREATE TABLE diary_notes (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    pokemon_id BIGINT      NOT NULL REFERENCES pokemon (id) ON DELETE RESTRICT,
    text       VARCHAR(2000) NOT NULL,
    updated_at TIMESTAMP,
    UNIQUE (user_id, pokemon_id)
);

CREATE TABLE badges (
    id            BIGSERIAL PRIMARY KEY,
    code          VARCHAR(40)  NOT NULL UNIQUE,
    name          VARCHAR(80)  NOT NULL,
    description   VARCHAR(300) NOT NULL,
    criteria_type VARCHAR(30)  NOT NULL,
    threshold     INTEGER      NOT NULL
);

CREATE TABLE user_badges (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT    NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    badge_id    BIGINT    NOT NULL REFERENCES badges (id) ON DELETE CASCADE,
    unlocked_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (user_id, badge_id)
);

CREATE TABLE user_game_stats (
    id                       BIGSERIAL PRIMARY KEY,
    user_id                  BIGINT  NOT NULL UNIQUE REFERENCES users (id) ON DELETE CASCADE,
    current_streak           INTEGER NOT NULL DEFAULT 0,
    best_streak              INTEGER NOT NULL DEFAULT 0,
    current_round_pokemon_id BIGINT REFERENCES pokemon (id)
);

CREATE TABLE share_links (
    id            BIGSERIAL PRIMARY KEY,
    token         VARCHAR(40) NOT NULL UNIQUE,
    type          VARCHAR(20) NOT NULL,
    ref_id        BIGINT      NOT NULL,
    owner_user_id BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at    TIMESTAMP   NOT NULL DEFAULT now()
);
