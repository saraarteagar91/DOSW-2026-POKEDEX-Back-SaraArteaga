-- Catálogo del Modo Coleccionista (RF-15). Las insignias solo se otorgan automáticamente (RN-11).
INSERT INTO badges (code, name, description, criteria_type, threshold) VALUES
    ('first_glance', 'Primera mirada', 'Consultaste tu primera ficha de Pokémon', 'POKEMON_VIEWS', 1),
    ('explorer', 'Exploradora', 'Consultaste 10 fichas de Pokémon', 'POKEMON_VIEWS', 10),
    ('collector', 'Coleccionista', 'Guardaste 5 Pokémon en tus favoritos', 'FAVORITES_COUNT', 5),
    ('super_collector', 'Gran coleccionista', 'Guardaste 15 Pokémon en tus favoritos', 'FAVORITES_COUNT', 15),
    ('strategist', 'Estratega', 'Creaste tu primer equipo Pokémon', 'TEAMS_CREATED', 1),
    ('team_builder', 'Constructora de equipos', 'Creaste 5 equipos Pokémon', 'TEAMS_CREATED', 5),
    ('full_squad', 'Equipo completo', 'Armaste un equipo con 6 Pokémon', 'FULL_TEAM', 6),
    ('sharp_eye', 'Ojo certero', 'Alcanzaste una racha de 5 aciertos en el minijuego', 'MINIGAME_STREAK', 5),
    ('pokemon_master', 'Maestra Pokémon', 'Alcanzaste una racha de 15 aciertos en el minijuego', 'MINIGAME_STREAK', 15);
