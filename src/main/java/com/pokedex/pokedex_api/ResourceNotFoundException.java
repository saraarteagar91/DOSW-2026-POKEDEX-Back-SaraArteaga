package com.pokedex.pokedex_api;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String entidad, String campo, Object valor) {
        super("No se encontró " + entidad + " con " + campo + " = " + valor, "NO_ENCONTRADO");
    }
}