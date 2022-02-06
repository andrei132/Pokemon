package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Meowth extends Pokemon {
    public Meowth() {
        super(PokemonType.Meowth,41,3,null,4,2,new ArrayList<>(2));
        this.getAbilities().add(new Ability(5,false,true,4));
        this.getAbilities().add(new Ability(1,false,true,3));
    }
}
