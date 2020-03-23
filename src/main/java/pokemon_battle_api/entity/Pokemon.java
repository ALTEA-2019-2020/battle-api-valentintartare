package pokemon_battle_api.entity;

import lombok.Data;

@Data
public class Pokemon {

    private PokemonType type;

    private int level;

    private int pokemonTypeId;

    private Integer maxHp;
    private Integer hp;
    private Integer attack;
    private Integer defense;
    private Integer speed;
    private boolean ko;
}
