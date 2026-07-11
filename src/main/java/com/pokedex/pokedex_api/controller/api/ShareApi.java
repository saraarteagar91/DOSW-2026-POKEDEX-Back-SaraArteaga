package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.request.ShareRequest;
import com.pokedex.pokedex_api.controller.dto.response.ShareLinkResponse;
import com.pokedex.pokedex_api.controller.dto.response.ShareResolutionResponse;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Share", description = "RF-17: compartir ficha o equipo mediante un enlace público")
@RequestMapping("/v1/share")
public interface ShareApi {

    @Operation(summary = "Generar enlace para compartir", description = "Requiere sesión activa")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    ResponseEntity<ShareLinkResponse> create(@Valid @RequestBody ShareRequest request,
                                              @AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Resolver un enlace compartido", description = "Acceso público, sin token")
    @GetMapping("/{token}")
    ResponseEntity<ShareResolutionResponse> resolve(@PathVariable String token);
}
