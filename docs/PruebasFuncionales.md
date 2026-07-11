# Pruebas Funcionales — Backend PokéBloom

DOSW 2026-I · Sara Viviana Arteaga Rodríguez

## 1. ¿Qué se probó y cómo?

Estas son las pruebas que se hicieron a mano, usando la aplicación real (corriendo con Docker + PostgreSQL + MongoDB), probando cada funcionalidad desde Swagger UI. La idea era confirmar que cada requerimiento funciona de principio a fin, tal como lo usaría una entrenadora o un administrador.

Esto es diferente a las pruebas automáticas (`mvn test`), que revisan el código por partes usando datos simulados. Estas pruebas de acá revisan la aplicación completa, funcionando de verdad.

**Todas las pruebas dieron el resultado esperado (✅).**

## 2. Casos de prueba

### Catálogo y búsqueda

| # | Qué se probó | Resultado |
|---|---|---|
| 1 | Ver la lista de Pokémon sin necesitar cuenta | ✅ |
| 2 | Ver la ficha completa de un Pokémon (Pikachu) | ✅ |
| 3 | Buscar un Pokémon por nombre | ✅ |
| 4 | Filtrar por tipo (ej. Fire) | ✅ |
| 5 | Filtrar por color, habilidad, movimiento, región, generación, si tiene mega evolución, etapa de evolución y rango de estadísticas | ✅ |
| 6 | Combinar varios filtros al mismo tiempo | ✅ |
| 7 | Ver el "Pokémon del día" y confirmar que es el mismo si se pide de nuevo el mismo día | ✅ |
| 8 | Ver la ficha de un Pokémon suma una visita en las estadísticas | ✅ |
| 9 | Solo *buscar* un Pokémon (sin abrir su ficha) no cuenta como visita | ✅ |

### Cuentas y login

| # | Qué se probó | Resultado |
|---|---|---|
| 10 | Crear una cuenta nueva | ✅ |
| 11 | No dejar crear dos cuentas con el mismo correo | ✅ |
| 12 | Iniciar sesión y recibir un token | ✅ |
| 13 | Sin token, no se puede entrar a rutas privadas (favoritos, equipos, etc.) | ✅ |
| 14 | Con el token puesto en Swagger (botón Authorize), sí se puede | ✅ |

### Favoritos

| # | Qué se probó | Resultado |
|---|---|---|
| 15 | Marcar un Pokémon como favorito | ✅ |
| 16 | Ver la lista de mis favoritos | ✅ |
| 17 | Quitar un favorito (el mismo botón sirve para marcar y desmarcar) | ✅ |

### Equipos

| # | Qué se probó | Resultado |
|---|---|---|
| 18 | Crear un equipo con 3 Pokémon | ✅ |
| 19 | Ver la sinergia del equipo (a qué tipos es débil, a cuáles resiste, stats promedio) | ✅ |
| 20 | No dejar crear un equipo con más de 6 Pokémon | ✅ |
| 21 | No dejar ver un equipo que es de otra cuenta | ✅ |

### Estadísticas de la comunidad

| # | Qué se probó | Resultado |
|---|---|---|
| 22 | Ver el panel de estadísticas (Pokémon más vistos, más elegidos en equipos) | ✅ |
| 23 | Las estadísticas se pueden ver con cuenta de entrenadora o de administrador | ✅ |

### Modo Coleccionista (insignias)

| # | Qué se probó | Resultado |
|---|---|---|
| 24 | Ver el catálogo completo de insignias | ✅ |
| 25 | Se desbloquea sola una insignia al ver una ficha por primera vez | ✅ |
| 26 | Se desbloquea sola una insignia al crear el primer equipo | ✅ |

### Minijuego ¿Quién es ese Pokémon?

| # | Qué se probó | Resultado |
|---|---|---|
| 27 | Empezar una ronda y recibir la imagen de la silueta | ✅ |
| 28 | Responder bien: sube la racha | ✅ |
| 29 | Responder mal: la racha vuelve a 0 (pero se guarda la mejor racha) | ✅ |
| 30 | Responder sin haber empezado una ronda da error | ✅ |

### Diario

| # | Qué se probó | Resultado |
|---|---|---|
| 31 | Escribir una nota personal sobre un Pokémon | ✅ |
| 32 | Leer esa misma nota después | ✅ |
| 33 | No dejar guardar una nota vacía | ✅ |

### Compartir

| # | Qué se probó | Resultado |
|---|---|---|
| 34 | Generar un enlace para compartir un Pokémon | ✅ |
| 35 | Abrir ese enlace sin tener sesión iniciada (debe funcionar igual, es público) | ✅ |

### Administrador

| # | Qué se probó | Resultado |
|---|---|---|
| 36 | Una cuenta normal (TRAINER) no puede crear Pokémon | ✅ |
| 37 | Una cuenta normal no puede ver la lista de usuarios | ✅ |
| 38 | Con cuenta ADMIN sí se puede crear un Pokémon nuevo | ✅ |
| 39 | No se puede crear dos Pokémon con el mismo número de Pokédex | ✅ |
| 40 | Editar un Pokémon existente | ✅ |
| 41 | El número de Pokédex no se puede cambiar aunque se intente en la edición | ✅ |
| 42 | Borrar un Pokémon que no está en uso | ✅ |
| 43 | No dejar borrar un Pokémon que sí está en un equipo o en favoritos | ✅ |
| 44 | Ver la lista de cuentas de la comunidad | ✅ |
| 45 | No dejar quitarle el rol de administrador a la única cuenta ADMIN que existe | ✅ |

## 3. Resumen

45 casos probados, 45 con el resultado esperado.

## 4. Errores que se encontraron y se corrigieron

Estos son errores reales que aparecieron mientras se hacían estas pruebas (no se habrían visto solo con las pruebas automáticas, porque esas usan datos simulados y no la aplicación completa corriendo):

| Error | Qué pasaba | Cómo se arregló |
|---|---|---|
| Al ver la ficha completa de un Pokémon, daba error 500 | Un problema de la librería que maneja la base de datos (Hibernate) al traer varias listas relacionadas a la vez | Se cambió el tipo de esas listas en el código |
| Los endpoints de administrador daban error 401 en vez de 403 cuando una cuenta normal intentaba usarlos | Un detalle de configuración de seguridad no dejaba pasar una redirección interna | Se corrigió la configuración |
| Swagger no cargaba | La versión de la librería de Swagger no era compatible con la versión de Spring que se estaba usando | Se actualizó la librería |
| Al crear un Pokémon con estadísticas, la respuesta mostraba las estadísticas vacías (aunque sí se habían guardado bien) | Otro detalle de Hibernate con relaciones uno a uno | Se corrigió en el código que guarda el Pokémon |
| Buscar con un número muy largo en el campo de búsqueda daba error 500 | El código no esperaba números tan grandes | Se agregó el manejo de ese caso |
| Buscar en Swagger daba error 500 si se dejaba el campo de "ordenar por" con el texto de ejemplo que trae por defecto | El sistema no sabía qué hacer con un nombre de campo que no existe, y respondía con un error genérico | Ahora responde con un mensaje claro explicando cuál es el problema |

Todos estos errores se volvieron a probar después de corregirlos y ya no aparecen.
