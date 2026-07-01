package com.pokedex.pokedex_api;

public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String entidad, String campo, Object valor) {
        super("Ya existe " + entidad + " con " + campo + " = " + valor, "DUPLICADO");
    }
}