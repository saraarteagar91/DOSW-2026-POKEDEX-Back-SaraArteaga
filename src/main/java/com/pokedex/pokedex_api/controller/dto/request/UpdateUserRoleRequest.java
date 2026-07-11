package com.pokedex.pokedex_api.controller.dto.request;

import com.pokedex.pokedex_api.core.model.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(@NotNull(message = "El rol es obligatorio") Role role) {
}
