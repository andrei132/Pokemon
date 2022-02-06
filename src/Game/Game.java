package Game;

import Coach.Coach;
import Pokemons.AllPokemons.Neutrel1;
import Pokemons.AllPokemons.Neutrel2;
import Pokemons.Pokemon;
import XMLRW.XMLParser;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;
import Exception.WrongGameID;
import Exception.WrongArenaEvent;
import Exception.CoachHaveNotThisPokemon;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game{

    private static Game instance;
    private static String id = null;
    private final ArrayList<Coach> coaches;
    private BufferedWriter bufferedWriter;

    private Game(){
        this.coaches = new ArrayList<>();
    }

    /**
     * Get an instance of Game
     * @return Instance
     **/
    public static Game getInstance(){
        if(instance == null) instance = new Game();
        return instance;
    }

    public static String getId() {
        return id;
    }

    /**
     * Set ID
     * @param id Id to set
     * @throws WrongGameID You can't change ID if you assign an ID to game
     **/
    public static void setId(String id) throws WrongGameID {
        if(Game.id == null) Game.id = id; else throw new WrongGameID(id);
    }

    public ArrayList<Coach> getCoaches() {
        return coaches;
    }

    public void addCoach(Coach coach){
        this.coaches.add(coach);
    }

    /**
     * Initialize a game with settings, coaches and pokemons from file
     * @param path Path to file
     * @throws ParserConfigurationException Exception for XML
     * @throws IOException Exception for XML
     * @throws SAXException Exception for XML
     * @throws WrongGameID If game id is different from file
     **/
    public void initializeGame(String path) throws ParserConfigurationException, IOException, SAXException, WrongGameID {
        XMLParser xmlParser = new XMLParser();
        xmlParser.uploadGame(path,this);
    }

    /**
     * Check if an coach exist in game by name
     * @param coachName Coach name
     * @return True is exist
     **/
    public boolean existCoach(String coachName){
        return this.coaches.contains(new Coach(coachName,-1));
    }

    /**
     * Get coach by name
     * @param coachName Coach name
     * @return First coach with name
     **/
    public Coach getCoachByName(String coachName){
        int index = this.getCoaches().indexOf(new Coach(coachName, -1));
        if(index == -1) return null;
        return this.getCoaches().get(index);
    }

    /**
     * Upload a battle to game
     * @param path Path to file
     * @param battleID Battle ID
     * @return Battle to be executed
     * @throws IOException XML Exception
     * @throws ParserConfigurationException Exception for XML
     * @throws SAXException Exception for XML
     * @throws WrongGameID Game id from file is different from game
     * @throws CoachHaveNotThisPokemon Coach haven't that pokemon
     **/
    private Battle uploadBattleSettings(String path, int battleID) throws IOException, ParserConfigurationException,
            SAXException, WrongGameID, CoachHaveNotThisPokemon {
        XMLParser xmlParser = new XMLParser();
        return xmlParser.loadBattleSettings(path,this,battleID);
    }

    /**
     * Run 2 battle concomitant
     * @param executorService Executor service that will be used
     * @param battle1 Battle nr 1
     * @param battle2 Battle nr 2
     * @throws InterruptedException Exception for executorService.awaitTermination
     * @throws IOException Exception for bufferedWriter.write
     **/
    private void executeBattle(@NotNull ExecutorService executorService, Battle battle1, Battle battle2)
            throws InterruptedException, IOException {
        executorService.execute(battle1);
        executorService.execute(battle2);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        if(this.bufferedWriter == null){
            System.out.println(battle1.getLogg());
            System.out.println();
            System.out.println(battle2.getLogg());
            return;
        }

        bufferedWriter.write(battle1.getLogg().toString());
        bufferedWriter.write(battle2.getLogg().toString());
    }

    /**
     * Fight for incrementing pokemon fields
     * @param pathToTrainingPokemon Path to battle file
     * @throws IOException Exception for bufferedWriter.write
     * @throws ParserConfigurationException Exception for XML
     * @throws SAXException Exception for XML
     * @throws InterruptedException Exception for executorService.awaitTermination
     * @throws WrongArenaEvent Arena have not that event
     * @throws WrongGameID Game id from file is different from game
     * @throws CoachHaveNotThisPokemon Coach haven't that pokemon
     **/
    private void trainingPokemon(String pathToTrainingPokemon) throws IOException, ParserConfigurationException,
            SAXException, InterruptedException, WrongArenaEvent, WrongGameID, CoachHaveNotThisPokemon {
        int i = 0;
        Battle battle = this.uploadBattleSettings(pathToTrainingPokemon,i++);
        ExecutorService executorService;

        while (battle != null){

            EventType eventType = ArenaEventsUtils.getRandomEvent();
            switch (eventType){
                case FIGHT_NEUTREL1 -> {
                    executorService = Executors.newFixedThreadPool(2);
                    Battle battle1VSN1 = new Battle(battle.getPokemon1(),battle.getItemForPokemon1(),
                            new Neutrel1(),null);
                    Battle battle2VSN1 = new Battle(battle.getPokemon2(),battle.getItemForPokemon2(),
                            new Neutrel1(),null);
                    executeBattle(executorService, battle1VSN1, battle2VSN1);
                }
                case FIGHT_NEUTREL2 -> {
                    executorService = Executors.newFixedThreadPool(2);
                    Battle battle1VSN2 = new Battle(battle.getPokemon1(),battle.getItemForPokemon1(),
                            new Neutrel2(),null);
                    Battle battle2VSN2 = new Battle(battle.getPokemon2(),battle.getItemForPokemon2(),
                            new Neutrel2(),null);
                    executeBattle(executorService, battle1VSN2, battle2VSN2);
                }
                case FIGHT_EACH_OTHER -> {
                    executorService = Executors.newFixedThreadPool(1);
                    executorService.execute(battle);
                    executorService.shutdown();
                    executorService.awaitTermination(1, TimeUnit.SECONDS);
                    if(this.bufferedWriter == null) System.out.println(battle.getLogg());
                    else bufferedWriter.write(battle.getLogg().toString());
                }
                default -> throw new WrongArenaEvent("Error, please notify Teach Support!");
            }
            battle = this.uploadBattleSettings(pathToTrainingPokemon,i++);
        }
    }

    /**
     * Return coach of that pokemon, not pokemon type
     * @param pokemon Pokemon that owns coach
     * @return Coach who owns pokemon
     **/
    public Coach getCoachForPokemon(Pokemon pokemon){
        for (Coach coach : coaches) {
            if(coach.containsPokemonObj(pokemon)) return coach;
        }

        return null;
    }

    /**
     * Last battle that will decide who is winner of that adventure
     * @param battle Battle that will be executed
     * @throws IOException Exception for bufferedWriter.write
     **/
    private void battleForWinner(@NotNull Battle battle) throws IOException {
        battle.run();
        String winner;

        if(battle.getPokemonWinner() != null ) winner = "Winner of all adventure is: "
                + this.getCoachForPokemon(battle.getPokemonWinner()).getName()
                + " with pokemon: " + battle.getPokemonWinner().getPokemonType();
        else winner = "===== Draw ====";

        if(this.bufferedWriter == null) {
            System.out.println(battle.getLogg());
            System.out.println(winner);
        return;
        }

        this.bufferedWriter.write(battle.getLogg().toString());
        this.bufferedWriter.write(winner);
    }

    /**
     * Last 3 battle to find most powerfully pokemon
     * @param pathToFinalBattlesSettings Path to file that describe last 3 battles
     * @throws IOException Exception for bufferedWriter.write
     * @throws ParserConfigurationException Exception for XML
     * @throws SAXException Exception for XML
     * @throws InterruptedException Exception for executor
     * @throws WrongGameID Game id from file is different from game
     * @throws CoachHaveNotThisPokemon Coach haven't that pokemon
     **/
    private void finalBattlesForWinner(String pathToFinalBattlesSettings) throws IOException,
            ParserConfigurationException, SAXException, InterruptedException, WrongGameID, CoachHaveNotThisPokemon {

        ArrayList<Battle> battles = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 3; i++){
            battles.add(this.uploadBattleSettings(pathToFinalBattlesSettings,i));
            executorService.execute(battles.get(i));
        }
        executorService.shutdown();
        executorService.awaitTermination(1,TimeUnit.SECONDS);
        for (Battle battle : battles) {
            if(this.bufferedWriter == null)System.out.println(battle.getLogg());
            else this.bufferedWriter.write(battle.getLogg().toString());
        }

        ArrayList<Battle> battles1 = (ArrayList<Battle>) battles.clone();

        battles.sort((Battle a, Battle b)->{
            int res = b.getScorePokemon1() - a.getScorePokemon1();
            if(res == 0)
                res = b.getPokemon1().getPokemonType().getClass().getSimpleName()
                        .compareTo(a.getPokemon1().getClass().getSimpleName());
            return res;
        });
        battles1.sort((Battle a, Battle b)->{
            int res = b.getScorePokemon2() - a.getScorePokemon2();
            if(res == 0)
                res = a.getPokemon2().getPokemonType().getClass().getSimpleName()
                        .compareTo(b.getPokemon2().getClass().getSimpleName());
            return res;
        });

        Battle battleForWinner = new Battle(battles.get(0).getPokemon1(),battles.get(0).getItemForPokemon1(),
                battles1.get(0).getPokemon2(),battles1.get(0).getItemForPokemon2());
        this.battleForWinner(battleForWinner);

    }

    /**
     * Run an adventure
     * @param pathToTrainingPokemons File to whats pokemons will be trained
     * @param pathToFinalSetting Final battle settings
     * @param writeToFile True if write on file
     * @throws IOException Exception for bufferedWriter.write
     * @throws ParserConfigurationException Exception for XML
     * @throws SAXException Exception for XML
     * @throws InterruptedException Exception for executor
     * @throws WrongArenaEvent Arena have not that event
     * @throws WrongGameID Game id from file is different from game
     * @throws CoachHaveNotThisPokemon Coach haven't that pokemon
     */
    public void startGame(String pathToTrainingPokemons, String pathToFinalSetting, boolean writeToFile)
            throws IOException, ParserConfigurationException, SAXException, InterruptedException, WrongArenaEvent,
            WrongGameID, CoachHaveNotThisPokemon {

        if(writeToFile) this.bufferedWriter = new BufferedWriter(
                new FileWriter("./resources/gamesOut/game"+Game.getId()+".out"));

        this.trainingPokemon(pathToTrainingPokemons);
        this.finalBattlesForWinner(pathToFinalSetting);
        if(writeToFile) this.bufferedWriter.close();

        //Game was finished
        Game.instance = null;
        System.out.println("Game with id " + Game.id + " ended.");
        Game.id = null;
        System.gc();
        System.out.println();
    }

}
