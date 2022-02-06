import Game.Game;
import org.xml.sax.SAXException;
import Exception.WrongGameID;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import Exception.WrongArenaEvent;
import Exception.CoachHaveNotThisPokemon;

public class Index {
    public static void main(String[] args) throws InterruptedException, ParserConfigurationException, IOException,
            SAXException, WrongGameID, WrongArenaEvent, CoachHaveNotThisPokemon {

        for(int i = 0; i < 10; i++){
            Game game = Game.getInstance();
            game.initializeGame("./resources/Game" + i+"/Game"+i+".xml");
            game.startGame("./resources/Game" + i+"/TrainingBattle"+i+".xml",
                    "./resources/Game" + i+"/x_finalBattles"+i+".xml",true);
        }
    }
}
