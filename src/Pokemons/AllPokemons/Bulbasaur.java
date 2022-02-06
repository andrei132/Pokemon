package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Bulbasaur extends Pokemon {

    public Bulbasaur() {
        super(PokemonType.Bulbasaur,42, null, 5, 3, 1,
                new ArrayList<>(2));
        this.getAbilities().add(new Ability(6,false, false, 4));
        this.getAbilities().add(new Ability(5, false, false, 3));
    }

}
