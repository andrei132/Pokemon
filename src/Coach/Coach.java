package Coach;

import Items.Item;
import Items.ItemFactory;
import Items.ItemType;
import Pokemons.Pokemon;
import Pokemons.PokemonFactory;
import Pokemons.PokemonType;

import java.util.ArrayList;
import java.util.Objects;

public class Coach {

    String name;
    Integer age;
    ArrayList<Pokemon> pokemons;
    ArrayList<Item> items;

    public Coach(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.pokemons = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ArrayList<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(ArrayList<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addPokemon(Pokemon pokemon){
        if(!this.pokemons.contains(pokemon))
            this.pokemons.add(pokemon);
    }

    public void addItem(Item item){
        this.items.add(item);
    }

    /**
     * Check if this coach has that pokemon type in his pokemons
     * @param pokemonType Pokemon's type to be search
     * @return True if pokemon exist
     **/
    public boolean checkIfHavePokemonType (PokemonType pokemonType) {
        return this.pokemons.contains(PokemonFactory.givePokemonFactory().givePokemon(pokemonType));
    }

    /**
     * Check if coach has that item type in his items
     * @param itemType Item's type to be search
     * @return True if item exist
     **/
    public boolean checkIfHaveItem(ItemType itemType) {
        return this.items.contains(ItemFactory.giveItemFactory().giveItem(itemType));
    }

    /**
     * Get pokemon from coach's pokemons by field type
     * @param pokemonType Pokemon's type to be search
     * @return Pokemon that have that type from coach's pokemon
     **/
    public Pokemon getPokemonByType (PokemonType pokemonType) {
        int index = this.pokemons.indexOf(PokemonFactory.givePokemonFactory().givePokemon(pokemonType));
        if(index == -1) return null;
        return this.pokemons.get(index);
    }

    /**
     * Check if right this object is in coach's pokemons
     * @param pokemon Object to be check
     * @return True if pokemon is own by this coach
     **/
    public boolean containsPokemonObj(Pokemon pokemon) {
        for (Pokemon pokemon1 : pokemons) {
            if (pokemon1.hashCode() == pokemon.hashCode()) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(name, coach.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
