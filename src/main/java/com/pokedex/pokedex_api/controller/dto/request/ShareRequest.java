package com.pokedex.pokedex_api.controller.dto.request;

import com.pokedex.pokedex_api.core.model.ShareType;
import jakarta.validation.constraints.NotNull;

public record ShareRequest(
        @NotNull(message = "El tipo de elemento a compartir es obligatorio") ShareType type,
        @NotNull(message = "El identificador del elemento es obligatorio") Long refId
) {
}
