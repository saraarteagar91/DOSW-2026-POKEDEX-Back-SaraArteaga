package com.pokedex.pokedex_api.core.exception;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String entidad, String campo, Object valor) {
        super("No se encontró " + entidad + " con " + campo + " = " + valor, "NOT_FOUND");
    }
}
