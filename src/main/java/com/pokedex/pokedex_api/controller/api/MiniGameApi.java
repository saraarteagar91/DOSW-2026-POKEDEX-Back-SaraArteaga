package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.request.MiniGameAnswerRequest;
import com.pokedex.pokedex_api.controller.dto.response.MiniGameAnswerResponse;
import com.pokedex.pokedex_api.controller.dto.response.MiniGameRoundResponse;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "MiniGame", description = "RF-16: minijuego ¿Quién es ese Pokémon?")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/minigame")
public interface MiniGameApi {

    @Operation(summary = "Iniciar una ronda", description = "Elige un Pokémon al azar y devuelve su silueta")
    @PostMapping("/start")
    ResponseEntity<MiniGameRoundResponse> start(@AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Responder la ronda activa")
    @PostMapping("/answer")
    ResponseEntity<MiniGameAnswerResponse> answer(@Valid @RequestBody MiniGameAnswerRequest request,
                                                   @AuthenticationPrincipal AuthenticatedUser principal);
}
