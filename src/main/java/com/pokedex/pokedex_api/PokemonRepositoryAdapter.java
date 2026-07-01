package com.pokedex.pokedex_api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PokemonRepositoryAdapter implements PokemonRepositoryPort {
    private final PokemonJpaRepository jpaRepository;
    @Override
    public Optional<Pokemon> obtenerPorId(Long id) {
        return jpaRepository.findById(id)
                            .map(this::aModelo);
    }
    @Override
    public List<Pokemon> obtenerTodos() {
        return jpaRepository.findAll()
                            .stream()
                            .map(this::aModelo)
                            .toList();
    }
    @Override
    public boolean existePorPokedexNumber(Integer pokedexNumber) {
        return jpaRepository.existsByNationalNumber(pokedexNumber);
    }
    @Override
    public Pokemon guardar(Pokemon pokemon) {
        PokemonEntity entidad = aEntidad(pokemon);
        return aModelo(jpaRepository.save(entidad));
    }
    @Override
    public void eliminarPorId(Long id) {
        jpaRepository.deleteById(id);
    }
    private Pokemon aModelo(PokemonEntity e) {
        return Pokemon.builder()
                .id(e.getId())
                .pokedexNumber(e.getNationalNumber())
                .name(e.getName())
                .imageUrl(e.getImageUrl())
                .generation(e.getGeneration())
                .build();
    }
    private PokemonEntity aEntidad(Pokemon p) {
        return PokemonEntity.builder()
                .nationalNumber(p.getPokedexNumber())
                .name(p.getName())
                .imageUrl(p.getImageUrl())
                .generation(p.getGeneration())
                .build();
    }
}