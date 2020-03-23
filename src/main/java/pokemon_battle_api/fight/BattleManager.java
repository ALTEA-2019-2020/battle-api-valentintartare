package pokemon_battle_api.fight;


import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pokemon_battle_api.constants.Constants;
import pokemon_battle_api.entity.Battle;
import pokemon_battle_api.entity.Pokemon;
import pokemon_battle_api.entity.Trainer;
import pokemon_battle_api.exception.ApplicationException;

import java.util.*;

@Component
@Data
public class BattleManager {

    private Map<UUID, Battle> battleList = new HashMap<>();

    public Battle createBattle(Trainer trainer1, Trainer trainer2) {
        List<Pokemon> pokemonsTrainer1 = setStatsAndPushIntoList(trainer1);
        List<Pokemon> pokemonsTrainer2 = setStatsAndPushIntoList(trainer2);
        trainer1.setTeam(pokemonsTrainer1);
        trainer2.setTeam(pokemonsTrainer2);
        trainer1.setNextTurn(true);
        trainer2.setNextTurn(false);
        Battle battle = Battle.builder()
                .trainer(trainer1)
                .opponent(trainer2)
                .build();
        UUID uuid = UUID.randomUUID();
        battle.setUuid(uuid);
        battleList.put(uuid, battle);
        return battle;
    }

    private List<Pokemon> setStatsAndPushIntoList(Trainer trainer) {
        List<Pokemon> pokemonsTrainer = new ArrayList<>();
        trainer.getTeam().forEach(pokemon -> {
            pokemonsTrainer.add(FightEngine.getPokemonWithStats(pokemon));
        });
        return pokemonsTrainer;
    }

    public Battle getBattleState(UUID uuid) {
        if (this.battleList.containsKey(uuid)) {
            return battleList.get(uuid);
        }else
            throw new ApplicationException(HttpStatus.BAD_REQUEST, Constants.WRONG_UUID);
    }

    public Battle attackOnBattle(UUID uuid, String trainerName) {
        if (this.battleList.containsKey(uuid)) {
            this.getBattleState(uuid).processBattle(trainerName);
            Battle battle = this.getBattleState(uuid);
            if (battle.getWinner() != null) {
                this.battleList.remove(uuid);
            }
            return battle;
        }else
            throw new ApplicationException(HttpStatus.BAD_REQUEST, Constants.WRONG_UUID);
    }
}
