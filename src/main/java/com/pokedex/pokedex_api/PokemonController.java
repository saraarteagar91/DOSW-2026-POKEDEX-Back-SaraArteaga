package com.pokedex.pokedex_api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/pokemon")
@RequiredArgsConstructor
public class PokemonController {
    private final PokemonService pokemonService;
    private final PokemonDtoMapper mapper;
    @GetMapping
    public ResponseEntity<List<PokemonResponse>> listar() {
        List<PokemonResponse> respuesta = pokemonService.listar()
                                                        .stream()
                .map(mapper::aRespuesta)
                .toList();
        return ResponseEntity.ok(respuesta);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponse> obtenerPorId(@PathVariable Long id) {
        Pokemon pokemon = pokemonService.obtenerPorId(id);
        return ResponseEntity.ok(mapper.aRespuesta(pokemon));
    }
    @PostMapping
    public ResponseEntity<PokemonResponse> registrar(@Valid @RequestBody PokemonRequest request) {
        Pokemon creado = pokemonService.registrar(mapper.aModelo(request));
        return ResponseEntity.ok(mapper.aRespuesta(creado));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pokemonService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}