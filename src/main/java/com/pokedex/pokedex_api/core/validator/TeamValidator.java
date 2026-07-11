package com.pokedex.pokedex_api.core.validator;

import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Reglas de negocio de equipos (RN-04): máximo 6 Pokémon y nombre obligatorio.
 */
@Component
public class TeamValidator {

    private static final int MAX_TEAM_SIZE = 6;

    public void validate(String name, List<Long> pokemonIds) {
        if (name == null || name.isBlank()) {
            throw new ForbiddenOperationException("El equipo debe tener un nombre");
        }
        if (pokemonIds != null && pokemonIds.size() > MAX_TEAM_SIZE) {
            throw new ForbiddenOperationException(
                    "Un equipo puede tener como máximo " + MAX_TEAM_SIZE + " Pokémon");
        }
    }
}
