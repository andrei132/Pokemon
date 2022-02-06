package Pokemons;

import Pokemons.AllPokemons.*;
import org.jetbrains.annotations.NotNull;

public class PokemonFactory {

    private static PokemonFactory instance = null;

    private PokemonFactory(){}

    /**
     * Give an PokemonFactory's instance
     * @return PokemonFactory's instance
     **/
    public static PokemonFactory givePokemonFactory(){
        if(instance == null) instance = new PokemonFactory();
        return instance;
    }

    /**
     * Give an pokemon from all possible pokemons
     * @param pokemonType Pokemon type to be give
     * @return Pokemon
     **/
    public Pokemon givePokemon(@NotNull PokemonType pokemonType){
        switch (pokemonType){
            case Neutrel1: return new Neutrel1();
            case Neutrel2: return new Neutrel2();
            case Pikachu: return new Pikachu();
            case Bulbasaur: return new Bulbasaur();
            case Charmander: return new Charmander();
            case Squirtle: return new Squirtle();
            case Snorlax: return new Snorlax();
            case Vulpix: return new Vulpix();
            case Eevee: return new Eevee();
            case Jigglypuff: return new Jigglypuff();
            case Meowth: return new Meowth();
            case Psyduck: return new Psyduck();
            default: {
                return null;
            }
        }
    }

}