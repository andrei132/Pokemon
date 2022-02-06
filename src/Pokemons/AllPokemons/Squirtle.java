package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Squirtle extends Pokemon {

    public Squirtle() {
        super(PokemonType.Squirtle,60,null,3,5,5,new ArrayList<>(2));
        this.getAbilities().add(new Ability(4,false,false,3));
        this.getAbilities().add(new Ability(2,true,false,2));
    }
}
