package com.pokedex.pokedex_api;
import lombok.Value;
import lombok.Builder;

@Value
@Builder
public class PokemonStats {
    Integer hp;
    Integer attack;
    Integer defense;
    Integer specialAttack;
    Integer specialDefense;
    Integer speed;
    public Integer getBaseStatTotal() {
        return hp + attack + defense + specialAttack + specialDefense + speed;
    }
}