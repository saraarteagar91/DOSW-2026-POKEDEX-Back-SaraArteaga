package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.request.TeamRequest;
import com.pokedex.pokedex_api.controller.dto.response.TeamResponse;
import com.pokedex.pokedex_api.controller.dto.response.TeamSynergyResponse;
import com.pokedex.pokedex_api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Teams", description = "RF-08 (equipos + sinergia), RF-09 (administrar mis equipos)")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/v1/teams")
public interface TeamApi {

    @Operation(summary = "Ver mis equipos", description = "RF-09")
    @GetMapping
    ResponseEntity<List<TeamResponse>> listMine(@AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Crear equipo", description = "RF-08, máximo 6 Pokémon (RN-04)")
    @PostMapping
    ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request,
                                         @AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Editar equipo", description = "RF-09")
    @PutMapping("/{id}")
    ResponseEntity<TeamResponse> update(@PathVariable Long id, @Valid @RequestBody TeamRequest request,
                                         @AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Eliminar equipo", description = "RF-09")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Ver sinergia del equipo",
            description = "RF-08: cobertura, debilidades/resistencias y balance de estadísticas")
    @GetMapping("/{id}/synergy")
    ResponseEntity<TeamSynergyResponse> synergy(@PathVariable Long id,
                                                 @AuthenticationPrincipal AuthenticatedUser principal);
}
