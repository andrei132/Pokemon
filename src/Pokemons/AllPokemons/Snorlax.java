package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Snorlax extends Pokemon {
    public Snorlax() {
        super(PokemonType.Snorlax,62,3,null,6,4,new ArrayList<>(2));
        this.getAbilities().add(new Ability(4,true, false, 5));
        this.getAbilities().add(new Ability(0,false,true,5));
    }
}
