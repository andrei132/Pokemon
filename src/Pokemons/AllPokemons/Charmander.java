package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Charmander extends Pokemon {

    public Charmander() {
        super(PokemonType.Charmander,50,4,null,3,2,
              new ArrayList<>(2));
        this.getAbilities().add(new Ability(4,true, false,4));
        this.getAbilities().add(new Ability(7,false,false,6));
    }
}
