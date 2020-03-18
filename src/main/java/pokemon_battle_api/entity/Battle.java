package pokemon_battle_api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import pokemon_battle_api.constants.Constants;
import pokemon_battle_api.exception.ApplicationException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Battle {
    private UUID uuid;
    private Trainer trainer;
    private Trainer opponent;
    private boolean nextTurn;
    private Trainer winner;

    public void processBattle(String trainerName) {
        if (winner == null) {
            Map<String, Pokemon> pokemonMap = involvePokemonIntoFight(trainerName);
            computeFight(pokemonMap.get(Constants.POKEMON_ATTACKER), pokemonMap.get(Constants.POKEMON_DEFENDER));
            nextTurn = !nextTurn;
            if (checkIfWinner()) return;
        }
    }

    private Map<String, Pokemon> involvePokemonIntoFight(String trainerName) {
        Map<String, Pokemon> pokemonMap = new HashMap<>();
        if (nextTurn && getTrainer().getName().equals(trainerName)) {
            pokemonMap.put(Constants.POKEMON_ATTACKER, getTrainer().getTeam().get(0));
            pokemonMap.put(Constants.POKEMON_DEFENDER, getOpponent().getTeam().get(0));
        } else if (!nextTurn && getOpponent().getName().equals(trainerName)){
            pokemonMap.put(Constants.POKEMON_ATTACKER, getOpponent().getTeam().get(0));
            pokemonMap.put(Constants.POKEMON_DEFENDER, getTrainer().getTeam().get(0));
        }else{
            throw new ApplicationException(HttpStatus.BAD_REQUEST, Constants.WRONG_TRAINER);
        }
        return pokemonMap;
    }

    private void computeFight(Pokemon pokemonAttacker, Pokemon pokemonDefender) {
        Integer lvlAttacker = pokemonAttacker.getLevel();
        Integer powerAttack = pokemonAttacker.getPokemonType().getStats().getAttack();
        Integer powerDefense = pokemonDefender.getPokemonType().getStats().getDefense();
        Integer hpDefender = pokemonDefender.getPokemonType().getStats().getHp();

        Integer HPToLose = (((2 * lvlAttacker / 5) + (2 * powerAttack / powerDefense)) + 2);
        pokemonDefender.getPokemonType().getStats().setHp(hpDefender - HPToLose);

        if (pokemonDefender.getPokemonType().getStats().getHp() < 0) {
            if (nextTurn)
                getOpponent().getTeam().remove(pokemonDefender);
            else
                getTrainer().getTeam().remove(pokemonDefender);
        }
    }

    private boolean checkIfWinner() {
        if (trainer.getTeam().isEmpty()) {
            winner = opponent;
            return true;
        } else if (opponent.getTeam().isEmpty()) {
            winner = trainer;
            return true;
        }
        return false;
    }
}
