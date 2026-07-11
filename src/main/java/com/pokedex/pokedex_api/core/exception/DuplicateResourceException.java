package com.pokedex.pokedex_api.core.exception;

public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String entidad, String campo, Object valor) {
        super(entidad + " con " + campo + " = " + valor + " ya existe", "DUPLICATE");
    }
}
