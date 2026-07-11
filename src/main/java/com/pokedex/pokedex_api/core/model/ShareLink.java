package com.pokedex.pokedex_api.core.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

/**
 * Enlace público para compartir una ficha o un equipo (RF-17). La imagen descargable la genera el frontend
 * a partir de los datos que resuelve este enlace.
 */
@Value
@Builder
public class ShareLink {
    Long id;
    String token;
    ShareType type;
    Long refId;
    Long ownerUserId;
    LocalDateTime createdAt;
}
