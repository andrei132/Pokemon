package Game;

import Pokemons.AttackType;
import Pokemons.Pokemon;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ArenaEventsUtils {

    /**
     * Get a random event
     * @return Random event
     **/
    public static EventType getRandomEvent(){
        return EventType.values()[new Random().nextInt(EventType.values().length)];
    }

    /**
     * Get a random attack type for pokemon
     * @param pokemon Pokemon
     * @return Attack type for pokemon 1
     **/
    public static AttackType getAttackTypeForPokemon(@NotNull Pokemon pokemon){
        if(pokemon.isStun()) return AttackType.NORMAL_ATTACK;
        AttackType attackType = AttackType.values()[new Random().nextInt(AttackType.values().length)];

        if(pokemon.canMakeThatAttack(attackType)) return attackType;
        return getAttackTypeForPokemon(pokemon);
    }
    
}
