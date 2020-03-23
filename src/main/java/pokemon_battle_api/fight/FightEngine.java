package pokemon_battle_api.fight;

import pokemon_battle_api.entity.Pokemon;

public class FightEngine {
    public static Pokemon getPokemonWithStats(Pokemon pokemon){
        Integer level = pokemon.getLevel();
        Integer attack = getRealStats(pokemon.getType().getStats().getAttack(),level);
        Integer speed = getRealStats(pokemon.getType().getStats().getSpeed(),level);
        Integer defense = getRealStats(pokemon.getType().getStats().getDefense(),level);

        Integer hp = getRealHp(pokemon.getType().getStats().getHp(),level);

        pokemon.setAttack(attack);
        pokemon.setDefense(defense);
        pokemon.setSpeed(speed);

        pokemon.setMaxHp(hp);

        return pokemon;
    }

    private static Integer getRealStats(Integer baseStat, Integer level){
        return 5 + ( baseStat * (level/50));
    }

    private static Integer getRealHp(Integer hp, Integer level){
        return 10 + level + ( hp * (level/50));
    }
}
