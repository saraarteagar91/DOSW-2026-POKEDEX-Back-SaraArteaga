package com.pokedex.pokedex_api.core.exception;

/** RF-06 E1: credenciales inválidas, sin precisar qué campo falló. */
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Correo o contraseña incorrectos", "INVALID_CREDENTIALS");
    }
}
