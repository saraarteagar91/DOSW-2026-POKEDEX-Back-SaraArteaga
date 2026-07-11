package com.pokedex.pokedex_api.core.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pokedex.pokedex_api.core.exception.ForbiddenOperationException;
import java.util.List;
import org.junit.jupiter.api.Test;

class TeamValidatorTest {

    private final TeamValidator validator = new TeamValidator();

    @Test
    void validate_whenNameBlank_throws() {
        assertThrows(ForbiddenOperationException.class, () -> validator.validate("  ", List.of(1L)));
    }

    @Test
    void validate_whenMoreThanSixPokemon_throws() {
        assertThrows(ForbiddenOperationException.class,
                () -> validator.validate("Equipo", List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)));
    }

    @Test
    void validate_whenValid_doesNotThrow() {
        assertDoesNotThrow(() -> validator.validate("Equipo", List.of(1L, 2L, 3L)));
    }
}
