package com.pokedex.pokedex_api.core.exception;

/**
 * Operación rechazada por una regla de negocio (no por falta de rol): RN-07 (retirar Pokémon en uso),
 * el último administrador (RF-14), límite de equipo (RN-04), etc.
 */
public class ForbiddenOperationException extends BusinessException {
    public ForbiddenOperationException(String message) {
        super(message, "FORBIDDEN_OPERATION");
    }
}
