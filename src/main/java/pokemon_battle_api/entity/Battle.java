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
import java.util.stream.Collectors;

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
            pokemonMap.put(Constants.POKEMON_ATTACKER, getTrainer().getTeam().stream().filter(pokemon -> !pokemon.isKo()).collect(Collectors.toList())
                    .get(0));
            pokemonMap.put(Constants.POKEMON_DEFENDER, getOpponent().getTeam().stream().filter(pokemon -> !pokemon.isKo()).collect(Collectors.toList())
                    .get(0));
        } else if (!nextTurn && getOpponent().getName().equals(trainerName)){
            pokemonMap.put(Constants.POKEMON_ATTACKER, getOpponent().getTeam().stream().filter(pokemon -> !pokemon.isKo()).collect(Collectors.toList())
                    .get(0));
            pokemonMap.put(Constants.POKEMON_DEFENDER, getTrainer().getTeam().stream().filter(pokemon -> !pokemon.isKo()).collect(Collectors.toList())
                    .get(0));
        }else{
            throw new ApplicationException(HttpStatus.BAD_REQUEST, Constants.WRONG_TRAINER);
        }
        return pokemonMap;
    }

    private void computeFight(Pokemon pokemonAttacker, Pokemon pokemonDefender) {
        Integer lvlAttacker = pokemonAttacker.getLevel();
        Integer powerAttack = pokemonAttacker.getAttack();
        Integer powerDefense = pokemonDefender.getDefense();
        Integer hpDefender = pokemonDefender.getHp();

        Integer HPToLose = (((2 * lvlAttacker / 5) + (2 * powerAttack / powerDefense)) + 2);
        pokemonDefender.setHp(hpDefender - HPToLose);

        if (pokemonDefender.getHp() < 0) {
            if (nextTurn)
                getOpponent().getTeam().stream().filter(pokemon -> !pokemon.isKo()).collect(Collectors.toList())
                        .get(0).setKo(true);
            else
                getTrainer().getTeam().stream().filter(pokemon -> !pokemon.isKo()).collect(Collectors.toList())
                        .get(0).setKo(true);
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
