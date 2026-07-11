package com.pokedex.pokedex_api.controller.api;

import com.pokedex.pokedex_api.controller.dto.request.PokemonRequest;
import com.pokedex.pokedex_api.controller.dto.response.PokemonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.pokedex.pokedex_api.security.AuthenticatedUser;

@Tag(name = "Pokemon", description = "RF-01..04 (catálogo, búsqueda, filtros), RF-11..13 (curaduría admin), RF-19")
@RequestMapping("/v1/pokemon")
public interface PokemonApi {

    @Operation(summary = "Listar Pokémon", description = "RF-01. Paginado, acceso público.")
    @GetMapping
    ResponseEntity<Page<PokemonResponse>> findAll(
            @ParameterObject @PageableDefault(size = 20, sort = "nationalNumber") Pageable pageable);

    @Operation(summary = "Buscar y filtrar", description = "RF-02 + RF-04 combinados. Acceso público.")
    @GetMapping("/search")
    ResponseEntity<Page<PokemonResponse>> search(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer generation,
            @RequestParam(required = false) String ability,
            @RequestParam(required = false) String move,
            @RequestParam(required = false) Boolean hasMega,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer evolutionStage,
            @RequestParam(required = false) Integer minTotalStats,
            @RequestParam(required = false) Integer maxTotalStats,
            @ParameterObject @PageableDefault(size = 20, sort = "nationalNumber") Pageable pageable);

    @Operation(summary = "Pokémon del día", description = "RF-19. Acceso público.")
    @GetMapping("/of-the-day")
    ResponseEntity<PokemonResponse> pokemonOfTheDay();

    @Operation(summary = "Ver ficha de un Pokémon", description = "RF-03. Acceso público, registra la visita.")
    @GetMapping("/{id}")
    ResponseEntity<PokemonResponse> findById(@PathVariable Long id,
                                              @AuthenticationPrincipal AuthenticatedUser principal);

    @Operation(summary = "Registrar Pokémon", description = "RF-11. Solo ADMIN.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    ResponseEntity<PokemonResponse> create(@Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Editar Pokémon", description = "RF-12. Solo ADMIN, el número de Pokédex es inmutable.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{id}")
    ResponseEntity<PokemonResponse> update(@PathVariable Long id, @Valid @RequestBody PokemonRequest request);

    @Operation(summary = "Retirar Pokémon", description = "RF-13. Solo ADMIN, respeta RN-07.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}
