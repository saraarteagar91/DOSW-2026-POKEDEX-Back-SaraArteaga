-- Semilla de ~34 Pokémon reales (Kanto, generación 1) con tipos, habilidades, movimientos y estadísticas,
-- suficiente para demostrar catálogo, filtros, equipos/sinergia, favoritos, minijuego y estadísticas.

INSERT INTO abilities (name) VALUES
    ('Overgrow'), ('Chlorophyll'), ('Blaze'), ('Torrent'), ('Shield Dust'), ('Keen Eye'), ('Run Away'),
    ('Static'), ('Sand Veil'), ('Flash Fire'), ('Cute Charm'), ('Inner Focus'), ('Damp'), ('Intimidate'),
    ('Water Absorb'), ('Guts'), ('Sturdy'), ('Oblivious'), ('Magnet Pull'), ('Levitate'), ('Rock Head'),
    ('Natural Cure'), ('Swift Swim'), ('Adaptability'), ('Immunity'), ('Pressure');

INSERT INTO moves (name) VALUES
    ('Tackle'), ('Vine Whip'), ('Razor Leaf'), ('Solar Beam'), ('Petal Dance'), ('Scratch'), ('Ember'),
    ('Slash'), ('Flamethrower'), ('Wing Attack'), ('Fire Blast'), ('Water Gun'), ('Bite'), ('Hydro Pump'),
    ('Skull Bash'), ('String Shot'), ('Gust'), ('Quick Attack'), ('Thunder Shock'), ('Iron Tail'),
    ('Sand Attack'), ('Thunderbolt'), ('Sing'), ('Pound'), ('Leech Life'), ('Supersonic'), ('Absorb'),
    ('Acid'), ('Confusion'), ('Bubble'), ('Hypnosis'), ('Karate Chop'), ('Low Kick'), ('Rock Throw'),
    ('Stomp'), ('Sonic Boom'), ('Lick'), ('Confuse Ray'), ('Bone Club'), ('Headbutt'), ('Double Slap'),
    ('Splash'), ('Dragon Rage'), ('Surf'), ('Ice Beam'), ('Body Slam'), ('Rest'), ('Psychic'), ('Aura Sphere');

-- 1 Bulbasaur
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (1, 'Bulbasaur', 'Pokémon Semilla. Tiene una semilla en el lomo desde que nace.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png', 1, FALSE, 1, NULL, 'Green', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 45,49,49,65,65,45 FROM pokemon WHERE national_number=1;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Grass','Poison') WHERE p.national_number=1;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Overgrow','Chlorophyll') WHERE p.national_number=1;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Vine Whip') WHERE p.national_number=1;

-- 2 Ivysaur
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (2, 'Ivysaur', 'Pokémon Semilla. La flor de su lomo crece con el sol.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png', 1, FALSE, 2, (SELECT id FROM pokemon WHERE national_number=1), 'Green', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 60,62,63,80,80,60 FROM pokemon WHERE national_number=2;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Grass','Poison') WHERE p.national_number=2;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Overgrow','Chlorophyll') WHERE p.national_number=2;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Vine Whip','Razor Leaf') WHERE p.national_number=2;

-- 3 Venusaur
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (3, 'Venusaur', 'Pokémon Semilla. Su flor libera un aroma relajante.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/3.png', 1, TRUE, 3, (SELECT id FROM pokemon WHERE national_number=2), 'Green', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 80,82,83,100,100,80 FROM pokemon WHERE national_number=3;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Grass','Poison') WHERE p.national_number=3;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Overgrow','Chlorophyll') WHERE p.national_number=3;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Solar Beam','Petal Dance') WHERE p.national_number=3;

-- 4 Charmander
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (4, 'Charmander', 'Pokémon Lagartija. La llama de su cola indica su vitalidad.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png', 1, FALSE, 1, NULL, 'Red', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 39,52,43,60,50,65 FROM pokemon WHERE national_number=4;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fire') WHERE p.national_number=4;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Blaze') WHERE p.national_number=4;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Scratch','Ember') WHERE p.national_number=4;

-- 5 Charmeleon
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (5, 'Charmeleon', 'Pokémon Lagartija. Su cola arde con más fuerza al pelear.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/5.png', 1, FALSE, 2, (SELECT id FROM pokemon WHERE national_number=4), 'Red', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 58,64,58,80,65,80 FROM pokemon WHERE national_number=5;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fire') WHERE p.national_number=5;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Blaze') WHERE p.national_number=5;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Slash','Flamethrower') WHERE p.national_number=5;

-- 6 Charizard
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (6, 'Charizard', 'Pokémon Llama. Escupe fuego capaz de fundir cualquier cosa.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png', 1, TRUE, 3, (SELECT id FROM pokemon WHERE national_number=5), 'Orange', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 78,84,78,109,85,100 FROM pokemon WHERE national_number=6;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fire','Flying') WHERE p.national_number=6;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Blaze') WHERE p.national_number=6;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Wing Attack','Fire Blast') WHERE p.national_number=6;

-- 7 Squirtle
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (7, 'Squirtle', 'Pokémon Tortuga. Se protege en su caparazón ante el peligro.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png', 1, FALSE, 1, NULL, 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 44,48,65,50,64,43 FROM pokemon WHERE national_number=7;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water') WHERE p.national_number=7;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Torrent') WHERE p.national_number=7;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Water Gun') WHERE p.national_number=7;

-- 8 Wartortle
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (8, 'Wartortle', 'Pokémon Tortuga. Su cola peluda es símbolo de longevidad.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/8.png', 1, FALSE, 2, (SELECT id FROM pokemon WHERE national_number=7), 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 59,63,80,65,80,58 FROM pokemon WHERE national_number=8;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water') WHERE p.national_number=8;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Torrent') WHERE p.national_number=8;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Bite','Water Gun') WHERE p.national_number=8;

-- 9 Blastoise
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (9, 'Blastoise', 'Pokémon Caparazón. Dispara agua a presión con sus cañones.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/9.png', 1, TRUE, 3, (SELECT id FROM pokemon WHERE national_number=8), 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 79,83,100,85,105,78 FROM pokemon WHERE national_number=9;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water') WHERE p.national_number=9;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Torrent') WHERE p.national_number=9;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Hydro Pump','Skull Bash') WHERE p.national_number=9;

-- 10 Caterpie
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (10, 'Caterpie', 'Pokémon Gusano. Devora hojas sin parar con su gran apetito.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/10.png', 1, FALSE, 1, NULL, 'Green', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 45,30,35,20,20,45 FROM pokemon WHERE national_number=10;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Bug') WHERE p.national_number=10;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Shield Dust') WHERE p.national_number=10;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','String Shot') WHERE p.national_number=10;

-- 16 Pidgey
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (16, 'Pidgey', 'Pokémon Ave Diminuta. Muy dócil, prefiere evitar peleas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/16.png', 1, FALSE, 1, NULL, 'Brown', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 40,45,40,35,35,56 FROM pokemon WHERE national_number=16;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Normal','Flying') WHERE p.national_number=16;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Keen Eye') WHERE p.national_number=16;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Gust') WHERE p.national_number=16;

-- 19 Rattata
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (19, 'Rattata', 'Pokémon Ratón. Muerde todo lo que encuentra a su paso.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/19.png', 1, FALSE, 1, NULL, 'Purple', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 30,56,35,25,35,72 FROM pokemon WHERE national_number=19;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Normal') WHERE p.national_number=19;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Run Away') WHERE p.national_number=19;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Quick Attack') WHERE p.national_number=19;

-- 25 Pikachu
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (25, 'Pikachu', 'Pokémon Ratón. Guarda electricidad en las bolsas de sus mejillas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png', 1, FALSE, 1, NULL, 'Yellow', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 35,55,40,50,50,90 FROM pokemon WHERE national_number=25;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Electric') WHERE p.national_number=25;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Static') WHERE p.national_number=25;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Thunder Shock','Quick Attack') WHERE p.national_number=25;

-- 26 Raichu
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (26, 'Raichu', 'Pokémon Ratón. Sus sacos de electricidad pueden dar potentes descargas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/26.png', 1, FALSE, 2, (SELECT id FROM pokemon WHERE national_number=25), 'Yellow', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 60,90,55,90,80,110 FROM pokemon WHERE national_number=26;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Electric') WHERE p.national_number=26;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Static') WHERE p.national_number=26;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Thunderbolt','Iron Tail') WHERE p.national_number=26;

-- 27 Sandshrew
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (27, 'Sandshrew', 'Pokémon Ratón. Se hace una bola para protegerse del enemigo.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/27.png', 1, FALSE, 1, NULL, 'Yellow', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 50,75,85,20,30,40 FROM pokemon WHERE national_number=27;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Ground') WHERE p.national_number=27;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Sand Veil') WHERE p.national_number=27;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Scratch','Sand Attack') WHERE p.national_number=27;

-- 37 Vulpix
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (37, 'Vulpix', 'Pokémon Zorro. Sus seis colas esconden un gran poder.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/37.png', 1, FALSE, 1, NULL, 'Brown', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 38,41,40,50,65,65 FROM pokemon WHERE national_number=37;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fire') WHERE p.national_number=37;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Flash Fire') WHERE p.national_number=37;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Ember','Quick Attack') WHERE p.national_number=37;

-- 39 Jigglypuff
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (39, 'Jigglypuff', 'Pokémon Globo. Canta una nana que hace dormir a quien la escucha.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/39.png', 1, FALSE, 1, NULL, 'Pink', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 115,45,20,45,25,20 FROM pokemon WHERE national_number=39;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Normal','Fairy') WHERE p.national_number=39;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Cute Charm') WHERE p.national_number=39;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Sing','Pound') WHERE p.national_number=39;

-- 41 Zubat
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (41, 'Zubat', 'Pokémon Murciélago. Emite ultrasonidos para orientarse en la oscuridad.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/41.png', 1, FALSE, 1, NULL, 'Purple', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 40,45,35,30,40,55 FROM pokemon WHERE national_number=41;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Poison','Flying') WHERE p.national_number=41;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Inner Focus') WHERE p.national_number=41;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Leech Life','Supersonic') WHERE p.national_number=41;

-- 43 Oddish
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (43, 'Oddish', 'Pokémon Maleza. De noche se mueve para absorber luz de luna.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/43.png', 1, FALSE, 1, NULL, 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 45,50,55,75,65,30 FROM pokemon WHERE national_number=43;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Grass','Poison') WHERE p.national_number=43;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Chlorophyll') WHERE p.national_number=43;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Absorb','Acid') WHERE p.national_number=43;

-- 54 Psyduck
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (54, 'Psyduck', 'Pokémon Pato. Sufre constantes dolores de cabeza.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/54.png', 1, FALSE, 1, NULL, 'Yellow', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 50,52,48,65,50,55 FROM pokemon WHERE national_number=54;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water') WHERE p.national_number=54;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Damp') WHERE p.national_number=54;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Water Gun','Confusion') WHERE p.national_number=54;

-- 58 Growlithe
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (58, 'Growlithe', 'Pokémon Cachorro. Muy leal, ladra a cualquier desconocido.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/58.png', 1, FALSE, 1, NULL, 'Brown', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 55,70,45,70,50,60 FROM pokemon WHERE national_number=58;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fire') WHERE p.national_number=58;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Intimidate') WHERE p.national_number=58;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Bite','Ember') WHERE p.national_number=58;

-- 60 Poliwag
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (60, 'Poliwag', 'Pokémon Renacuajo. Se le transparenta el interior del cuerpo.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/60.png', 1, FALSE, 1, NULL, 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 40,50,40,40,40,90 FROM pokemon WHERE national_number=60;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water') WHERE p.national_number=60;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Water Absorb') WHERE p.national_number=60;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Bubble','Hypnosis') WHERE p.national_number=60;

-- 66 Machop
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (66, 'Machop', 'Pokémon Superpoder. Entrena todos los días para ser más fuerte.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/66.png', 1, FALSE, 1, NULL, 'Grey', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 70,80,50,35,35,35 FROM pokemon WHERE national_number=66;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fighting') WHERE p.national_number=66;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Guts') WHERE p.national_number=66;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Karate Chop','Low Kick') WHERE p.national_number=66;

-- 74 Geodude
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (74, 'Geodude', 'Pokémon Roca. Suele confundirse con una piedra cualquiera.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/74.png', 1, FALSE, 1, NULL, 'Brown', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 40,80,100,30,30,20 FROM pokemon WHERE national_number=74;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Rock','Ground') WHERE p.national_number=74;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Sturdy') WHERE p.national_number=74;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Rock Throw') WHERE p.national_number=74;

-- 77 Ponyta
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (77, 'Ponyta', 'Pokémon Caballo Fuego. Sus pezuñas se endurecen tras correr mucho.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/77.png', 1, FALSE, 1, NULL, 'Orange', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 50,85,55,65,65,90 FROM pokemon WHERE national_number=77;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Fire') WHERE p.national_number=77;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Flash Fire') WHERE p.national_number=77;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Ember','Stomp') WHERE p.national_number=77;

-- 79 Slowpoke
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (79, 'Slowpoke', 'Pokémon Bobo. Tarda cinco segundos en sentir el dolor.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/79.png', 1, FALSE, 1, NULL, 'Pink', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 90,65,65,40,40,15 FROM pokemon WHERE national_number=79;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water','Psychic') WHERE p.national_number=79;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Oblivious') WHERE p.national_number=79;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Confusion','Water Gun') WHERE p.national_number=79;

-- 81 Magnemite
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (81, 'Magnemite', 'Pokémon Imán. Flota usando electromagnetismo.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/81.png', 1, FALSE, 1, NULL, 'Grey', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 25,35,70,95,55,45 FROM pokemon WHERE national_number=81;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Electric','Steel') WHERE p.national_number=81;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Magnet Pull') WHERE p.national_number=81;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Thunder Shock','Sonic Boom') WHERE p.national_number=81;

-- 92 Gastly
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (92, 'Gastly', 'Pokémon Gas. Un cuerpo de gas envuelve un pequeño núcleo.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/92.png', 1, FALSE, 1, NULL, 'Purple', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 30,35,30,100,35,80 FROM pokemon WHERE national_number=92;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Ghost','Poison') WHERE p.national_number=92;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Levitate') WHERE p.national_number=92;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Lick','Confuse Ray') WHERE p.national_number=92;

-- 95 Onix
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (95, 'Onix', 'Pokémon Serpiente Roca. Crece excavando bajo tierra.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/95.png', 1, FALSE, 1, NULL, 'Grey', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 35,45,160,30,45,70 FROM pokemon WHERE national_number=95;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Rock','Ground') WHERE p.national_number=95;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Rock Head') WHERE p.national_number=95;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Rock Throw') WHERE p.national_number=95;

-- 104 Cubone
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (104, 'Cubone', 'Pokémon Rata Solitaria. Lleva puesto el cráneo de su madre.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/104.png', 1, FALSE, 1, NULL, 'Brown', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 50,50,95,40,50,35 FROM pokemon WHERE national_number=104;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Ground') WHERE p.national_number=104;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Rock Head') WHERE p.national_number=104;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Bone Club','Headbutt') WHERE p.national_number=104;

-- 113 Chansey
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (113, 'Chansey', 'Pokémon Huevo. Reparte sus huevos nutritivos a quien encuentra.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/113.png', 1, FALSE, 1, NULL, 'Pink', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 250,5,5,35,105,50 FROM pokemon WHERE national_number=113;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Normal') WHERE p.national_number=113;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Natural Cure') WHERE p.national_number=113;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Pound','Double Slap') WHERE p.national_number=113;

-- 129 Magikarp
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (129, 'Magikarp', 'Pokémon Pez. Casi no tiene poder de combate ni utilidad.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/129.png', 1, FALSE, 1, NULL, 'Red', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 20,10,55,15,20,80 FROM pokemon WHERE national_number=129;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water') WHERE p.national_number=129;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Swift Swim') WHERE p.national_number=129;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Splash','Tackle') WHERE p.national_number=129;

-- 130 Gyarados
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (130, 'Gyarados', 'Pokémon Atroz. Muy violento, destruye todo lo que encuentra.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/130.png', 1, TRUE, 2, (SELECT id FROM pokemon WHERE national_number=129), 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 95,125,79,60,100,81 FROM pokemon WHERE national_number=130;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water','Flying') WHERE p.national_number=130;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Intimidate') WHERE p.national_number=130;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Hydro Pump','Dragon Rage') WHERE p.national_number=130;

-- 131 Lapras
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (131, 'Lapras', 'Pokémon Transporte. Lleva viajeros sobre su lomo por el mar.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/131.png', 1, FALSE, 1, NULL, 'Blue', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 130,85,80,85,95,60 FROM pokemon WHERE national_number=131;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Water','Ice') WHERE p.national_number=131;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Water Absorb') WHERE p.national_number=131;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Surf','Ice Beam') WHERE p.national_number=131;

-- 133 Eevee
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (133, 'Eevee', 'Pokémon Evolución. Su estructura genética inestable le permite evolucionar de muchas formas.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/133.png', 1, FALSE, 1, NULL, 'Brown', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 55,55,50,45,65,55 FROM pokemon WHERE national_number=133;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Normal') WHERE p.national_number=133;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Adaptability') WHERE p.national_number=133;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Tackle','Quick Attack') WHERE p.national_number=133;

-- 143 Snorlax
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (143, 'Snorlax', 'Pokémon Dormilón. Solo se mueve para comer y volver a dormir.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/143.png', 1, FALSE, 1, NULL, 'Black', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 160,110,65,65,110,30 FROM pokemon WHERE national_number=143;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Normal') WHERE p.national_number=143;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Immunity') WHERE p.national_number=143;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Body Slam','Rest') WHERE p.national_number=143;

-- 144 Articuno
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (144, 'Articuno', 'Pokémon Ave Congelante. Dicen que trae la nieve al volar.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/144.png', 1, FALSE, 1, NULL, 'White', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 90,85,100,95,125,85 FROM pokemon WHERE national_number=144;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Ice','Flying') WHERE p.national_number=144;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Pressure') WHERE p.national_number=144;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Ice Beam','Gust') WHERE p.national_number=144;

-- 150 Mewtwo
INSERT INTO pokemon (national_number, name, description, image_url, generation, has_mega, evolution_stage, evolves_from_id, color, region_id)
VALUES (150, 'Mewtwo', 'Pokémon Genético. Creado por manipulación genética con fines científicos.', 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/150.png', 1, TRUE, 1, NULL, 'Purple', (SELECT id FROM regions WHERE name = 'Kanto'));
INSERT INTO pokemon_stats (pokemon_id, hp, attack, defense, special_attack, special_defense, speed) SELECT id, 106,110,90,154,90,130 FROM pokemon WHERE national_number=150;
INSERT INTO pokemon_type (pokemon_id, type_id) SELECT p.id, t.id FROM pokemon p JOIN types t ON t.name IN ('Psychic') WHERE p.national_number=150;
INSERT INTO pokemon_ability (pokemon_id, ability_id) SELECT p.id, a.id FROM pokemon p JOIN abilities a ON a.name IN ('Pressure') WHERE p.national_number=150;
INSERT INTO pokemon_move (pokemon_id, move_id) SELECT p.id, m.id FROM pokemon p JOIN moves m ON m.name IN ('Psychic','Aura Sphere') WHERE p.national_number=150;
