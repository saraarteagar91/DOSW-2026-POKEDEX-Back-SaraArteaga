package com.pokedex.pokedex_api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PokemonServiceImpl implements PokemonService {
    private final PokemonRepositoryPort pokemonRepository;
    @Override
    public List<Pokemon> listar() {
        log.debug("PokeBloom: Consultando la colección completa de Pokémon");
        return pokemonRepository.obtenerTodos();
    }
    @Override
    public Pokemon obtenerPorId(Long id) {
        log.debug("PokeBloom: Buscando el Pokémon con id {}", id);
        return pokemonRepository.obtenerPorId(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Pokemon", "id", id));
    }
    @Override
    public Pokemon registrar(Pokemon pokemon) {
        log.info("PokeBloom: Registrando el Pokémon {}", pokemon.getName());
        if (pokemonRepository.existePorPokedexNumber(pokemon.getPokedexNumber())) {
            throw new DuplicateResourceException("Pokémon", "pokedexNumber", pokemon.getPokedexNumber());
        }
        return pokemonRepository.guardar(pokemon);
    }
    @Override
    public void eliminar(Long id) {
        log.info("PokeBloom: Retirando de la colección el Pokémon con id {}", id);
        obtenerPorId(id);
        pokemonRepository.eliminarPorId(id);
    }
}