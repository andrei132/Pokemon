package Exception;

import Game.Game;

public class WrongGameID extends Exception{
    public WrongGameID (String gameID) {
        super("Game ID "+gameID+" is wrong one, right: "+ Game.getId());
    }
}
