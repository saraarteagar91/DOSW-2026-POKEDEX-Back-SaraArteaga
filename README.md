# PokéBloom — Back Readme

## Descripción del proyecto

PokéBloom es una Pokédex web con un estilo pastel y amigable. Permite explorar el catálogo de Pokémon, buscar, ver la ficha completa de cada uno, filtrar la colección, guardar favoritos, armar equipos y ver su sinergia, consultar estadísticas de la comunidad, y contiene funciones propias como un modo coleccionista con insignias, un minijuego de adivinar Pokémon, la opción de compartir fichas o equipos, un diario personal de notas, y un Pokémon destacado cada día. También existe un rol de administrador encargado de mantener el catálogo y de gestionar las cuentas de la comunidad.

Este repositorio contiene el **backend** de la aplicación: Java 21, Spring Boot 3.5, PostgreSQL (datos relacionales) + MongoDB (vistas/estadísticas), JWT + OAuth2 (Google), organizado en 5 capas independientes.

## Documentos del proyecto

- **Requerimientos:** [Correccion_analisis_de_requerimientos_Pokebloom.pdf](docs/Correccion_analisis%20_de%20_requerimientos_Pokebloom.pdf) — funcionales, no funcionales y reglas de negocio.
- **Documento de Arquitectura:** [docs/DocumentoArquitectura.md](docs/DocumentoArquitectura.md) — estilo arquitectónico, responsabilidad de cada capa y por qué, decisiones de diseño y sus alternativas descartadas.
- **Pruebas Funcionales:** [docs/PruebasFuncionales.md](docs/PruebasFuncionales.md) — 48 casos de prueba manuales ejecutados contra la aplicación real, con los defectos encontrados y corregidos en el proceso.
- **Plan de trabajo base:** `POKEDEX_DOSW_S1.docx.pdf` (raíz del repo) — guía de arquitectura por capas que sigue este backend.

---

## 1. Arquitectura por capas

```
src/main/java/com/pokedex/pokedex_api/
├── controller/     → api/ (interfaces) · impl/ (@RestController) · dto/request · dto/response
│                     mapper/ (MapStruct + mappers manuales) · handler/ (GlobalExceptionHandler)
│                     swagger/ (OpenApiConfig)
├── core/           → service/interfaces · service/impl (lógica de negocio)
│                     model/ (POJOs puros, SIN JPA) · exception/ · validator/ · util/
├── persistence/    → entity/relational (JPA) · entity/document (Mongo)
│                     repository/relational · repository/document
│                     mapper/ (MapStruct) · adapter/ (implementa los puertos definidos en core)
├── config/         → AppConfig · CorsConfig · AsyncConfig (transversal)
└── security/       → SecurityConfig · JwtAuthFilter · JwtService · OAuth2SuccessHandler ·
                       UserDetailsServiceImpl · AuthenticatedUser (transversal)
```

**Regla de dependencias:** `controller → core (solo interfaces) → persistence (vía adapters)`. `config` y `security` son transversales. Nunca `controller → persistence` directo, ni `core → entidad JPA`.

**Desviación del paquete base:** se usa `com.pokedex.pokedex_api` (coincide con el `groupId`/`artifactId` de `pom.xml`) en vez de `com.pokedex` — única diferencia cosmética respecto al documento guía.

### Decisiones de alcance (documentadas a propósito)

- **Compartir (RF-17):** el backend genera el enlace público (`ShareLink`) y los datos estructurados de la tarjeta; la imagen descargable la construye el frontend con esos datos.
- **Pokémon del día (RF-19):** se calcula de forma determinística a partir de la fecha (`PokemonOfDaySelector`), sin tabla nueva — misma fecha siempre da el mismo resultado.
- **Tasa de elección (RF-10):** se calcula en vivo con `COUNT` sobre la tabla relacional `team_pokemon`. MongoDB se usa para el conteo de vistas/consultas (`pokemon_views`).
- **Minijuego (RF-16):** el estado de la ronda activa (Pokémon esperado) vive en `user_game_stats` junto con la racha; no hay historial de partidas.
- **Colecciones `types`/`abilities`/`moves` como `Set`** (no `List`) en `PokemonEntity`: Hibernate no permite hacer *fetch join* de más de una colección tipo `List` a la vez (`MultipleBagFetchException`); con `Set` sí, y así `@EntityGraph` puede traer todo en una sola consulta.
- **`@Setter` en `PokemonEntity`/`TeamEntity`** (desviación del ejemplo "solo getters" del documento base): RF-12 exige edición real del catálogo, y mutar en el sitio la entidad ya gestionada por Hibernate es más simple y correcto que reconstruirla por completo en cada actualización. Los setters solo se usan desde su adapter, dentro de `persistence`.
- **Mappers manuales vs MapStruct:** `PokemonDtoMapper`, `UserDtoMapper`, `PokemonPersistenceMapper` y `UserPersistenceMapper` usan MapStruct (relaciones no triviales). El resto (`TeamDtoMapper`, `BadgeDtoMapper`, `StatsDtoMapper`, `MiniGameDtoMapper`, `ShareDtoMapper`) son clases `@Component` escritas a mano — la relación de campos es directa y no justifica generar código adicional.

---

## 2. Stack técnico

Java 21 · Spring Boot 3.5 · Spring Data JPA (PostgreSQL) · Spring Data MongoDB · Spring Security 6 (JWT + OAuth2 Client) · MapStruct 1.6 · Lombok · Flyway · springdoc-openapi (Swagger) · JUnit 5 + Mockito · JaCoCo · Docker Compose.

## 3. Puesta en marcha

```bash
# 1. Levantar PostgreSQL + MongoDB
docker compose up -d

# 2. Variables de entorno mínimas para desarrollo local
#    (ya provistas en src/main/resources/application-dev.yaml, gitignored)
#    En producción son obligatorias: JWT_SECRET, GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, DB_*, MONGODB_URI

# 3. Ejecutar con el perfil dev (aplica migraciones Flyway + seed de ~34 Pokémon reales, 18 tipos, insignias)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# 4. Swagger UI
http://localhost:8080/api/swagger-ui/index.html
```

### Pruebas y cobertura

```bash
./mvnw test                       # 149 pruebas: unitarias (Mockito) + @WebMvcTest
# Reporte: target/site/jacoco/index.html  (línea ≥70%, exigido por el plugin en la fase verify)
./mvnw verify                     # compila + pruebas + gate de cobertura + checkstyle (modo reporte)
```

---

## 4. Diagramas

Los diagramas técnicos están en **XML de draw.io** (`docs/Diagramas/*.drawio`), editables abriéndolos directamente en [app.diagrams.net](https://app.diagrams.net) (Archivo → Abrir de... → Dispositivo). Los diagramas de casos de uso, clases y contexto ya existentes como PNG se conservan en las carpetas de abajo.

### 4.1 Diagrama de Contexto (C4 — Nivel 1)
[`docs/Diagramas/01-contexto.drawio`](docs/Diagramas/01-contexto.drawio)
El sistema PokéBloom API como caja negra: visitantes, entrenadoras y administradores acceden vía el frontend; el backend se integra con Google OAuth2, PostgreSQL y MongoDB.

### 4.2 Diagrama de Componentes General (C4 — Nivel 2)
[`docs/Diagramas/02-componentes-general.drawio`](docs/Diagramas/02-componentes-general.drawio)
Las 5 capas (`controller`, `core`, `persistence`, `config`, `security`) y la regla de dependencias entre ellas.

### 4.3 Diagrama de Componentes Específico (flujo `GET /api/v1/pokemon/{id}`)
[`docs/Diagramas/03-componentes-flujo-pokemon.drawio`](docs/Diagramas/03-componentes-flujo-pokemon.drawio)
Recorrido completo de una petición a través de `JwtAuthFilter` → `PokemonController` → `PokemonService` → adapters de PostgreSQL y MongoDB, incluyendo la evaluación asíncrona de insignias.

### 4.4 Diagrama de Clases (foco en `core`)
[`docs/Diagramas/04-clases.drawio`](docs/Diagramas/04-clases.drawio)
También disponible como imagen ya renderizada: `docs/DiagramaClases/DiagramaDeClases.png`.

### 4.5 Diagrama Entidad-Relación (PostgreSQL) y Documentos (MongoDB)
[`docs/Diagramas/05-er-postgresql.drawio`](docs/Diagramas/05-er-postgresql.drawio) — 17 tablas relacionales.
[`docs/Diagramas/06-documentos-mongodb.drawio`](docs/Diagramas/06-documentos-mongodb.drawio) — colección `pokemon_views`.

### Diagramas de casos de uso por requerimiento funcional (ya existentes)

RF-01 a RF-19: ver `docs/DiagramasCasosDeUso/rf1.png` … `rf19.png`.

### Diagrama de contexto (imagen ya renderizada)

`docs/DiagramaContexto/DiagramaDeContexto.png`

---

## 5. Dominio y roles

| Rol | Permisos |
|---|---|
| GUEST (sin token) | Ver catálogo, buscar, filtrar, ficha, Pokémon del día, resolver enlaces compartidos |
| TRAINER | Todo lo de GUEST + favoritos, equipos, diario, minijuego, insignias, compartir, estadísticas |
| ADMIN | Todo lo de TRAINER + CRUD de Pokémon + gestión de cuentas de la comunidad |

## 6. Endpoints principales

| Método | Ruta (`/api/v1/...`) | Rol | RF |
|---|---|---|---|
| POST | `/auth/register` | Público | RF-05 |
| POST | `/auth/login` | Público | RF-06 |
| GET | `/oauth2/authorization/google` | Público | RF-06 (inicia login con Gmail) |
| GET | `/pokemon` | Público | RF-01 |
| GET | `/pokemon/search` | Público | RF-02 + RF-04 (búsqueda y filtros combinados) |
| GET | `/pokemon/of-the-day` | Público | RF-19 |
| GET | `/pokemon/{id}` | Público | RF-03 (registra visita) |
| POST/PUT/DELETE | `/pokemon`, `/pokemon/{id}` | ADMIN | RF-11, RF-12, RF-13 |
| GET | `/favorites` | TRAINER/ADMIN | RF-07 |
| POST | `/favorites/{pokemonId}` | TRAINER/ADMIN | RF-07 (toggle) |
| GET/POST/PUT/DELETE | `/teams` | TRAINER/ADMIN | RF-08, RF-09 |
| GET | `/teams/{id}/synergy` | TRAINER/ADMIN | RF-08 |
| GET | `/stats` | TRAINER/ADMIN | RF-10 |
| GET | `/badges`, `/badges/me` | TRAINER/ADMIN | RF-15 |
| POST | `/minigame/start`, `/minigame/answer` | TRAINER/ADMIN | RF-16 |
| GET/PUT | `/diary/{pokemonId}` | TRAINER/ADMIN | RF-18 |
| POST | `/share` | TRAINER/ADMIN | RF-17 |
| GET | `/share/{token}` | Público | RF-17 |
| GET | `/admin/users` | ADMIN | RF-14 |
| PATCH | `/admin/users/{id}/role`, `/status` | ADMIN | RF-14 |

Especificación completa e interactiva en Swagger UI.

## 7. Reglas de negocio implementadas

RN-01 (correo único) · RN-02 (solo ADMIN cura el catálogo) · RN-03 (OAuth2 solo Gmail) · RN-04 (equipo ≤ 6, `TeamValidator`) · RN-05 (múltiples equipos por entrenadora) · RN-06 (número de Pokédex inmutable) · RN-07 (bloqueo de borrado si el Pokémon está en uso) · RN-08 (estadísticas visibles para TRAINER y ADMIN) · RN-09 (gestión de cuentas solo ADMIN, protección del último administrador) · RN-10 (contraseña mínima 8 caracteres) · RN-11 (insignias solo automáticas) · RN-12 (favoritos y diario privados).
