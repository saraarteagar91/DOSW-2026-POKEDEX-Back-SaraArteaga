package com.pokedex.pokedex_api.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(@NotNull(message = "El estado es obligatorio") Boolean enabled) {
}
