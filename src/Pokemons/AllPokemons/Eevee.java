package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Eevee extends Pokemon {
    public Eevee() {
        super(PokemonType.Eevee,39,null,4,3,3,new ArrayList<>(2));
        this.getAbilities().add(new Ability(5,false,false,3));
        this.getAbilities().add(new Ability(3,true,false,3));
    }
}
