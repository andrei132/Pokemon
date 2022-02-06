package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Pikachu extends Pokemon {

    public Pikachu() {
        super(PokemonType.Pikachu,35, null, 4, 2, 3, new ArrayList<>(2));
        this.getAbilities().add(new Ability(6, false, false, 4));
        this.getAbilities().add(new Ability(4, true, true, 5));
    }

}

