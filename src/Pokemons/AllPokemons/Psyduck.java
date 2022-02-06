package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Psyduck extends Pokemon {
    public Psyduck() {
        super(PokemonType.Psyduck,43,3,null,3,3,new ArrayList<>(2));
        this.getAbilities().add(new Ability(2,false,false,4));
        this.getAbilities().add(new Ability(2,true,false,5));
    }
}
