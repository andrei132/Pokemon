package XMLRW;

import Coach.Coach;
import Game.Game;
import Game.Battle;
import Items.Item;
import Items.ItemFactory;
import Items.ItemType;
import Pokemons.PokemonFactory;
import Pokemons.PokemonType;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import Exception.WrongGameID;
import Exception.CoachHaveNotThisPokemon;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class XMLParser {

    /**
     * Read an file and prepare it to be parsed
     * @param path Path to file
     * @return Document ready to be parsed
     * @throws IOException Exception for XMLReader
     * @throws SAXException Exception for XMLReader
     * @throws ParserConfigurationException Exception for XMLReader
     **/
    private @NotNull Document readXML(String path) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(new File(path));
        doc.getDocumentElement().normalize();

        return doc;
    }

    /**
     * Read all info about game, coaches, coach's pokemons, coach's item
     * @param path Path to file
     * @param game Game that will be uploaded
     * @throws ParserConfigurationException Exception for XMLReader
     * @throws IOException Exception for XMLReader
     * @throws SAXException Exception for XMLReader
     * @throws WrongGameID If Game ID from file is different from the application
     **/
    public void uploadGame(String path, Game game) throws ParserConfigurationException, IOException, SAXException,
            WrongGameID {
        Document doc = this.readXML(path);
        Game.setId(doc.getDocumentElement().getAttribute("ID"));
        System.out.println("Loading game with ID: " + Game.getId());

        NodeList list = doc.getElementsByTagName("Coach");

        for(int i = 0, len = list.getLength(); i < len; i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                String name = element.getElementsByTagName("Name").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("Age").item(0).getTextContent());

                Coach coach = new Coach(name,age);
                NodeList pokemons = ((Element)element.getElementsByTagName("Pokemons").item(0))
                        .getElementsByTagName("Pokemon");

                for (int j = 0, lenPok = pokemons.getLength(); j < lenPok; j++ ){
                    Node pokemon = pokemons.item(j);
                    if(pokemon.getNodeType() == Node.ELEMENT_NODE){
                        Element pokemonE = (Element) pokemon;
                        PokemonType pokemonType = PokemonType.getEnum(pokemonE.getTextContent());
                        if(pokemonType == PokemonType.PokemonNonexistent) continue;
                        coach.addPokemon(PokemonFactory.givePokemonFactory().givePokemon(pokemonType));
                    }
                }

                NodeList items = ((Element)element.getElementsByTagName("Items").item(0))
                        .getElementsByTagName("Item");

                for(int j = 0, lenItem = items.getLength(); j < lenItem; j++){
                    Node item = items.item(j);
                    if(item.getNodeType() == Node.ELEMENT_NODE){
                        Element itemE = (Element) item;
                        ItemType itemType = ItemType.toEnum(itemE.getTextContent());
                        if(itemType == ItemType.ItemNonExistent) continue;
                        coach.addItem(ItemFactory.giveItemFactory().giveItem(itemType));
                    }
                }
                game.addCoach(coach);
            }
        }

        System.out.println("Game was uploaded SUCCESSFULLY");
    }

    /**
     * Load settings for all battle
     * @param path Path to file
     * @param game Game that is used
     * @param battleID Battle ID in file
     * @return Battle that was uploaded
     * @throws IOException Exception for XMLReader
     * @throws ParserConfigurationException Exception for XMLReader
     * @throws SAXException Exception for XMLReader
     * @throws WrongGameID If Game ID from file is different from the application
     * @throws CoachHaveNotThisPokemon If coach has not this pokemon
     **/
    public Battle loadBattleSettings(String path, Game game, int battleID) throws IOException,
            ParserConfigurationException, SAXException, WrongGameID, CoachHaveNotThisPokemon {

        Document doc = readXML(path);
        Battle battleWithThatSettings = null;

        if(!Game.getId().equals(doc.getDocumentElement().getAttribute("id")))
            throw new WrongGameID(doc.getDocumentElement().getAttribute("id"));

        NodeList list = doc.getElementsByTagName("Battle");
        for(int i = 0, len = list.getLength(); i < len; i++){
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){

                Element battle = (Element) node;
                if(Integer.parseInt(battle.getAttribute("nr")) != battleID) continue;
                battleWithThatSettings = new Battle();
                NodeList coaches = battle.getElementsByTagName("Coach");
                int lenC = coaches.getLength();

                for(int j = 0; j < lenC; j++){
                    Node coach = coaches.item(j);
                    if (extractPokemonsForBattle(game, battleWithThatSettings, coach)) return null;
                }
            }
        }
        return battleWithThatSettings;
    }

    /**
     * Extract all pokemon from file for coach
     * @param game Game that is used
     * @param battleWithThatSettings Battle that is uploading
     * @param coach Coach that is owner of pokemon
     * @return True if something went wrong
     * @throws CoachHaveNotThisPokemon If coach has not this pokemon
     **/
    private boolean extractPokemonsForBattle (Game game, Battle battleWithThatSettings, @NotNull Node coach)
            throws CoachHaveNotThisPokemon {
        if(coach.getNodeType() == Node.ELEMENT_NODE){
            Element coachE = (Element) coach;

            Coach coachInBattle1 = game.getCoachByName(coachE.getElementsByTagName("Name").
                                    item(0).getTextContent());
            String pokemonType = coachE.getElementsByTagName("PokemonToFight").
                    item(0).getTextContent();

            if(!coachInBattle1.checkIfHavePokemonType(PokemonType.getEnum(pokemonType))){
                throw new CoachHaveNotThisPokemon(coachInBattle1.getName(),pokemonType);
            }

            NodeList items = ((Element)coachE.getElementsByTagName("ItemsGive").item(0)).
                            getElementsByTagName("Item");

            int lenI = items.getLength();
            if(lenI > 3) return true;
            Item[] itemsToApply = new Item[lenI];
            for(int k = 0; k < lenI; k++){
                Node item = items.item(k);
                if (setOnKPositionItemIfCoachHaveIt(coachInBattle1, itemsToApply, k, item)) return true;
            }

            ArrayList<Item> itemArrayList = new ArrayList<>(Arrays.asList(itemsToApply));
            return awardPokemonInBattle(battleWithThatSettings, coachInBattle1, pokemonType, itemArrayList);
        }
        return false;
    }

    /**
     * Set an item on position k if coach have that item
     * @param coachInBattle Coach that have that item
     * @param itemsToApply Item array to get in battle
     * @param k Position in array to get in battle
     * @param item Item to add in item array
     * @return True if something went wrong
     **/
    private boolean setOnKPositionItemIfCoachHaveIt (Coach coachInBattle, Item[] itemsToApply, int k,
                                                     @NotNull Node item) {
        if(item.getNodeType() == Node.ELEMENT_NODE){
            String itemType = item.getTextContent();
            if (!coachInBattle.checkIfHaveItem(ItemType.toEnum(itemType))) return true;
            itemsToApply[k] = ItemFactory.giveItemFactory().giveItem(ItemType.toEnum(itemType));
        }
        return false;
    }

    /**
     * Load pokemon into battle from coach
     * @param battleWithThatSettings Battle that will be updated
     * @param coachInBattle Coach that has pokemon type
     * @param pokemonType Pokemon type from coach
     * @param itemArrayList Items that will take with him pokemon in battle
     * @return True if something went wrong
     */
    private boolean awardPokemonInBattle (@NotNull Battle battleWithThatSettings, Coach coachInBattle,
                                          String pokemonType, ArrayList<Item> itemArrayList) {
        if(battleWithThatSettings.getPokemon1() == null) {
            battleWithThatSettings.setPokemon1(coachInBattle.getPokemonByType(PokemonType.getEnum(pokemonType)));
            battleWithThatSettings.setItemForPokemon1(itemArrayList);
        } else if(battleWithThatSettings.getItemForPokemon2() == null) {
            battleWithThatSettings.setPokemon2(coachInBattle.getPokemonByType(PokemonType.getEnum(pokemonType)));
            battleWithThatSettings.setItemForPokemon2(itemArrayList);
        } else return true;

        return false;
    }

}
