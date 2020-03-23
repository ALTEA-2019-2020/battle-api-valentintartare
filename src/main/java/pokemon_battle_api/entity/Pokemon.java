package pokemon_battle_api.entity;

import lombok.Data;

@Data
public class Pokemon {

    private PokemonType type;

    private int level;

    private int pokemonTypeId;

    private int maxHp;
    private int attack;
    private int defense;
    private int speed;
    private boolean ko;
}
