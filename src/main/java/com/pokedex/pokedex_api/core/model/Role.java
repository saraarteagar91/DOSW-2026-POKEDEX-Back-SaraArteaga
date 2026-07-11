package com.pokedex.pokedex_api.core.model;

/**
 * Roles autenticados del sistema. GUEST no se persiste: es el visitante sin sesión (RF-01..04, RF-19).
 */
public enum Role {
    TRAINER,
    ADMIN
}
