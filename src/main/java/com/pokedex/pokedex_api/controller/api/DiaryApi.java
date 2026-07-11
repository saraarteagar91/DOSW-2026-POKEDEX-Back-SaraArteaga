package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.request.DiaryNoteRequest;
import com.pokedex.pokedex_api.controller.dto.response.DiaryNoteResponse;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Diary", description = "RF-18, RN-12: diario personal privado de notas por Pokémon")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/diary")
public interface DiaryApi {

    @Operation(summary = "Ver mi nota sobre un Pokémon")
    @GetMapping("/{pokemonId}")
    ResponseEntity<DiaryNoteResponse> getNote(@PathVariable Long pokemonId,
                                               @AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Escribir o editar mi nota sobre un Pokémon")
    @PutMapping("/{pokemonId}")
    ResponseEntity<DiaryNoteResponse> upsertNote(@PathVariable Long pokemonId,
                                                  @Valid @RequestBody DiaryNoteRequest request,
                                                  @AuthenticationPrincipal AuthenticatedUser principal);
}
