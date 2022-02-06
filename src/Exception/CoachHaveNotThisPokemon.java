package Exception;

public class CoachHaveNotThisPokemon extends Exception{
    public CoachHaveNotThisPokemon (String coach, String pokemon) {
        super("Coach " + coach + " has not pokemon " + pokemon);
    }
}
