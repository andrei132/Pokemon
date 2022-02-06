package Exception;

public class PokemonNotFound extends Exception{
    public PokemonNotFound (String Pokemon) {
        super("Pokemon: "+Pokemon+" was nos found");
    }
}
