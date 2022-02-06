package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Jigglypuff extends Pokemon {
    public Jigglypuff() {
        super(PokemonType.Jigglypuff,34,4,null,2,3,new ArrayList<>(2));
        this.getAbilities().add(new Ability(4,true,false,4));
        this.getAbilities().add(new Ability(3,true,false,4));
    }
}
