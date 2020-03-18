package pokemon_battle_api.entity;

import lombok.Data;

@Data
public class Pokemon {

    private PokemonType pokemonType;

    private int level;

    private int pokemonTypeId;
}
