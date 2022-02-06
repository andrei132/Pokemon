package Pokemons;

import Items.Item;
import org.jetbrains.annotations.NotNull;
import Exception.TooManyObjects;
import Exception.WrongAttackType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public abstract class Pokemon implements Cloneable{

    private final Lock lock = new ReentrantLock();
    private PokemonType pokemonType;
    private Integer HP;
    private Integer normalAttack;
    private Integer specialAttack;
    private Integer defense;
    private Integer specialDefense;
    private final ArrayList<Ability> abilities;
    private ArrayList<Integer> coolDown;
    private boolean stun;
    private boolean dodge;

    public Pokemon(PokemonType pokemonType, Integer HP, Integer normalAttack, Integer specialAttack, Integer defense,
                   Integer specialDefense, ArrayList<Ability> abilities) {
        this.pokemonType = pokemonType;
        this.HP = HP;
        this.normalAttack = normalAttack;
        this.specialAttack = specialAttack;
        this.defense = defense;
        this.specialDefense = specialDefense;
        this.abilities = abilities;
        this.coolDown = new ArrayList<>(Arrays.asList(0,0));
        this.stun = false;
        this.dodge = false;
    }

    public Integer getHP() {
        return HP;
    }

    public Integer getNormalAttack() {
        return normalAttack;
    }

    public Integer getSpecialAttack() {
        return specialAttack;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getSpecialDefense() {
        return specialDefense;
    }

    public void setHP(Integer HP) {
        this.HP = HP;
    }

    public void setCoolDown(ArrayList<Integer> coolDown) {
        this.coolDown = coolDown;
    }

    public boolean isStun() {
        return stun;
    }

    public void setStun(boolean stun) {
        this.stun = stun;
    }

    public boolean isDodge() {
        return dodge;
    }

    public void setDodge(boolean dodge) {
        this.dodge = dodge;
    }

    public ArrayList<Integer> getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int abilityID) {
        this.coolDown.set(abilityID - 1,this.abilities.get(abilityID).getCoolDown());
    }

    public PokemonType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(PokemonType pokemonType) {
        this.pokemonType = pokemonType;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }

    @Override
    public Pokemon clone() {
        Pokemon returnClone = null;
        try {
            returnClone = (Pokemon) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return returnClone;
    }

    /**
     * Check if HP is bigger than 0
     * @return True if HP is bigger than 0
     **/
    public boolean isAlive(){
        return this.HP > 0;
    }

    /**
     * Increment all possible stats
     **/
    public void pokemonWinBattle(){
        this.HP++;
        if(this.normalAttack != null) this.normalAttack++;
        if(this.specialAttack != null) this.specialAttack++;
        this.defense++;
        this.specialDefense++;
    }

    /**
     * Apply all items to pokemon, condition to be less than 4 items
     * @param items Items to be applied
     * @return New Pokemon with items applied
     * @throws TooManyObjects If items.length() > 3
     **/
    public Pokemon inGamePokemon(ArrayList<Item> items) throws TooManyObjects {

        Pokemon pokemonInGame = this.clone();
        if (items == null || items.size() == 0) return pokemonInGame;

        if(items.size() > 3) throw new TooManyObjects();
        // Remove duplicates
        items = (ArrayList<Item>) items.stream().distinct().collect(Collectors.toList());
        for (Item item : items) {
            pokemonInGame.HP += item.getHP();
            if(pokemonInGame.normalAttack != null) pokemonInGame.normalAttack += item.getAttack();
            if(pokemonInGame.specialAttack != null) pokemonInGame.specialAttack += item.getSpecialAttack();
            pokemonInGame.defense += item.getDefence();
            pokemonInGame.specialDefense += item.getSpecialDefence();
        }

        return pokemonInGame;
    }

    /**
     * Check if pokemon can make that attack type
     * @param attackType Attack type to be checked
     * @return True if pokemon can make that attack
     **/
    public boolean canMakeThatAttack(AttackType attackType){
        if(this.normalAttack != null && attackType == AttackType.NORMAL_ATTACK) return true;
        if(this.specialAttack != null && attackType == AttackType.SPECIAL_ATTACK) return true;
        if(this.abilities.get(0) != null && attackType == AttackType.ABILITY1 && this.coolDown.get(0) == 0) return true;
        return this.abilities.get(1) != null && attackType == AttackType.ABILITY2 && this.coolDown.get(1) == 0;
    }

    /**
     * Attack a pokemon with attack type, set stun, coolDown for ability, and dodge
     * @param pokemon Pokemon that will be attacked
     * @param attackType Pokemon's attack type
     * @param isStun Must be this.isStun()
     * @throws WrongAttackType If attackType is wrong format or can't find that attack
     **/
    public void attackPokemon(@NotNull Pokemon pokemon, @NotNull AttackType attackType, boolean isStun)
            throws WrongAttackType {
        if(!isStun)
            switch (attackType) {
                case NORMAL_ATTACK -> {
                    lock.lock();
                    if (this.normalAttack != null) pokemon.HP -= (this.normalAttack
                            - (pokemon.defense > this.normalAttack ? this.normalAttack
                                                                    : pokemon.defense));
                    lock.unlock();
                }

                case SPECIAL_ATTACK -> {
                    lock.lock();
                    if (this.specialAttack != null) pokemon.HP -= (this.specialAttack
                            - (pokemon.specialDefense > this.specialAttack ? this.specialAttack
                                                                            : pokemon.specialDefense));
                    lock.unlock();
                }

                case ABILITY1, ABILITY2 -> {
                    lock.lock();
                    int abilityID = 0;
                    if (attackType == AttackType.ABILITY2) abilityID = 1;

                    if (this.abilities.get(abilityID) != null && this.coolDown.get(abilityID) <= 0)
                        attackWithAbility(pokemon, abilityID);

                    lock.unlock();
                }

                default -> { throw new WrongAttackType(); }
            }

        if (this.coolDown.get(0) > 0) this.coolDown.set(0,this.coolDown.get(0) - 1);
        if (this.coolDown.get(1) > 0) this.coolDown.set(1,this.coolDown.get(1) - 1);
    }

    /**
     * Attack a pokemon with ability, set coolDown, stun and dodge
     * @param pokemon Pokemon that will be attacked
     * @param abilityID Ability1 = 0, Ability2 = 1
     **/
    private void attackWithAbility (@NotNull Pokemon pokemon, int abilityID) {
        pokemon.HP -= this.abilities.get(abilityID).getDamage();
        this.dodge = this.abilities.get(abilityID).getDodge();

        if (!pokemon.stun) pokemon.stun = this.abilities.get(abilityID).getStun();
        else if (this.abilities.get(abilityID).getStun()) pokemon.stun = !pokemon.stun;

        this.coolDown.set(abilityID, this.abilities.get(abilityID).getCoolDown() + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return pokemonType == pokemon.pokemonType;
    }

    /**
     * Get score for pokemon by formula: score = HPAfterBattle + normalAttack/specialAttack + defence + specialDefence
     * @return Score that this pokemon accumulate
     **/
    public int getScore(){
        int res = 0;
        if(this.HP > 0) res+=this.HP;
        if(this.normalAttack != null) res+=this.normalAttack;
        if(this.specialAttack != null) res+=this.specialAttack;
        res+=this.defense;
        res+=this.specialDefense;
        return res;
    }

    /**
     * Reset coolDown
     **/
    public void resetCoolDown(){
        this.coolDown.set(0,0);
        this.coolDown.set(1,0);
    }

}
