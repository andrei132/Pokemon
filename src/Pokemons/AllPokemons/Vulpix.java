package Pokemons.AllPokemons;

import Pokemons.Ability;
import Pokemons.Pokemon;
import Pokemons.PokemonType;

import java.util.ArrayList;

public class Vulpix extends Pokemon {
    public Vulpix() {
        super(PokemonType.Vulpix,36,5,null,2,4,new ArrayList<>(2));
        this.getAbilities().add(new Ability(8,true,false,6));
        this.getAbilities().add(new Ability(2,false,true,7));
    }
}
