package Pokemons;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum PokemonType {
    Neutrel1("Neutrel1"),
    Neutrel2("Neutrel2"),
    Pikachu("Pikachu"),
    Bulbasaur("Bulbasaur"),
    Charmander("Charmander"),
    Squirtle("Squirtle"),
    Snorlax("Snorlax"),
    Vulpix("Vulpix"),
    Eevee("Eevee"),
    Jigglypuff("Jigglypuff"),
    Meowth("Meowth"),
    Psyduck("Psyduck"),
    PokemonNonexistent("PokemonNonexistent");

    private final String label;

    PokemonType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Contract(pure = true)
    public static PokemonType getEnum(@NotNull String label){
        switch (label){
            case "Neutrel1" -> {return Neutrel1;}
            case "Neutrel2" -> {return Neutrel2;}
            case "Pikachu" -> {return Pikachu;}
            case "Bulbasaur" -> {return Bulbasaur;}
            case "Charmander" -> {return Charmander;}
            case "Squirtle" -> {return Squirtle;}
            case "Snorlax" -> {return Snorlax;}
            case "Vulpix" -> {return Vulpix;}
            case "Eevee" -> {return Eevee;}
            case "Jigglypuff" -> {return Jigglypuff;}
            case "Meowth" -> {return Meowth;}
            case "Psyduck" -> {return Psyduck;}
            default -> {return PokemonNonexistent;}
        }
    }

}
