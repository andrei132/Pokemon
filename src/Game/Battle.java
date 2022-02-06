package Game;

import Items.Item;
import Pokemons.AttackType;
import Pokemons.Pokemon;
import org.jetbrains.annotations.NotNull;
import Exception.TooManyObjects;
import Exception.WrongAttackType;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Battle implements Runnable {

    private Pokemon pokemon1;
    private ArrayList<Item> itemForPokemon1;
    private Pokemon pokemon2;
    private ArrayList<Item> itemForPokemon2;
    private StringBuilder logg = new StringBuilder();
    private int scorePokemon1;
    private int scorePokemon2;
    private Pokemon pokemonWinner;

    public Battle (Pokemon pokemon1, ArrayList<Item> itemForPokemon1, Pokemon pokemon2,
                   ArrayList<Item> itemForPokemon2) {
        this.pokemon1 = pokemon1;
        this.itemForPokemon1 = itemForPokemon1;
        this.pokemon2 = pokemon2;
        this.itemForPokemon2 = itemForPokemon2;
        this.pokemonWinner = null;
    }

    public Battle () {
    }

    public Pokemon getPokemon1 () {
        return pokemon1;
    }

    public void setPokemon1 (Pokemon pokemon1) {
        this.pokemon1 = pokemon1;
    }

    public ArrayList<Item> getItemForPokemon1 () {
        return itemForPokemon1;
    }

    public void setItemForPokemon1 (ArrayList<Item> itemForPokemon1) {
        this.itemForPokemon1 = itemForPokemon1;
    }

    public Pokemon getPokemon2 () {
        return pokemon2;
    }

    public void setPokemon2 (Pokemon pokemon2) {
        this.pokemon2 = pokemon2;
    }

    public ArrayList<Item> getItemForPokemon2 () {
        return itemForPokemon2;
    }

    public void setItemForPokemon2 (ArrayList<Item> itemForPokemon2) {
        this.itemForPokemon2 = itemForPokemon2;
    }

    public StringBuilder getLogg () {
        return logg;
    }

    public int getScorePokemon1 () {
        return scorePokemon1;
    }

    public void setScorePokemon1 (int scorePokemon1) {
        this.scorePokemon1 = scorePokemon1;
    }

    public int getScorePokemon2 () {
        return scorePokemon2;
    }

    public void setScorePokemon2 (int scorePokemon2) {
        this.scorePokemon2 = scorePokemon2;
    }

    public void setLogg (StringBuilder logg) {
        this.logg = logg;
    }

    public Pokemon getPokemonWinner () {
        return pokemonWinner;
    }

    public void setPokemonWinner (Pokemon pokemonWinner) {
        this.pokemonWinner = pokemonWinner;
    }

    /**
     * Update stats after attack
     * @param finalPokemonReadyToFight Pokemon after attack
     * @param pokemonReadyToFight Pokemon before attack
     * @param pokemon Pokemon in pace mode
     * @return Pokemon with stats after attack
     **/
    private Pokemon getPokemonStatsAfterAttack (@NotNull Pokemon finalPokemonReadyToFight,
                                                Pokemon pokemonReadyToFight, Pokemon pokemon) {
        if (!finalPokemonReadyToFight.isDodge()) pokemonReadyToFight = finalPokemonReadyToFight;
        else logg.append("!! ").append(pokemon.getPokemonType()).append(" has dodged attack").append(" !!\n");
        return pokemonReadyToFight;
    }

    /**
     * Set stun for pokemons
     * @param pokemonReadyToFight1 First pokemon
     * @param pokemonReadyToFight2 Second pokemon
     * @param statsPokemon1BeforeAttack Stats Stun before attack for first pokemon
     * @param statsPokemon2BeforeAttack Stats Stun before for second pokemon
     * @param finalPokemonReadyToFight Pokemon 1 after attack
     * @param finalPokemonReadyToFight1 Pokemon 2 after attack
     **/
    private void setStun (@NotNull Pokemon pokemonReadyToFight1, @NotNull Pokemon pokemonReadyToFight2,
                          boolean statsPokemon1BeforeAttack, boolean statsPokemon2BeforeAttack,
                          @NotNull Pokemon finalPokemonReadyToFight, @NotNull Pokemon finalPokemonReadyToFight1) {
        pokemonReadyToFight1.setStun(finalPokemonReadyToFight.isStun() ^ statsPokemon1BeforeAttack);
        pokemonReadyToFight2.setStun(finalPokemonReadyToFight1.isStun() ^ statsPokemon2BeforeAttack);
    }

    /**
     * Set coolDown for pokemons
     * @param pokemonReadyToFight1 First pokemon
     * @param pokemonReadyToFight2 Second pokemon
     * @param finalPokemonReadyToFight Pokemon 1 after fight
     * @param finalPokemonReadyToFight1 Pokemon 2 after fight
     **/
    private void setCoolDownForPokemons (@NotNull Pokemon pokemonReadyToFight1, @NotNull Pokemon pokemonReadyToFight2,
                                         @NotNull Pokemon finalPokemonReadyToFight,
                                         @NotNull Pokemon finalPokemonReadyToFight1) {
        pokemonReadyToFight1.setCoolDown(finalPokemonReadyToFight.getCoolDown());
        pokemonReadyToFight2.setCoolDown(finalPokemonReadyToFight1.getCoolDown());
    }

    /**
     * Reset dodge
     * @param pokemonReadyToFight1 Pokemon 1
     * @param pokemonReadyToFight2 Pokemon 2
     **/
    private void resetDodge (@NotNull Pokemon pokemonReadyToFight1, @NotNull Pokemon pokemonReadyToFight2) {
        pokemonReadyToFight1.setDodge(false);
        pokemonReadyToFight2.setDodge(false);
    }

    /**
     * Attack each other
     * @param attackTypePokemon1 Attack type for pokemon 1
     * @param attackTypePokemon2 Attack type for pokemon 2
     * @param finalPokemonReadyToFight Pokemon 1
     * @param finalPokemonReadyToFight1 Pokemon 2
     * @param stun1 Must be finalPokemonReadyToFight.isStun()
     * @param stun2 Must be finalPokemonReadyToFight1.isStun()
     **/
    private void attackPokemons (AttackType attackTypePokemon1, AttackType attackTypePokemon2,
                                 Pokemon finalPokemonReadyToFight, Pokemon finalPokemonReadyToFight1,
                                 boolean stun1, boolean stun2) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            try {
                finalPokemonReadyToFight.attackPokemon(finalPokemonReadyToFight1, attackTypePokemon1, stun1);
            } catch (WrongAttackType e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                finalPokemonReadyToFight1.attackPokemon(finalPokemonReadyToFight, attackTypePokemon2, stun2);
            } catch (WrongAttackType e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset stun and coolDown for both pokemons
     **/
    private void resetStunAndCoolDown () {
        pokemon1.setStun(false);
        pokemon2.setStun(false);
        pokemon1.resetCoolDown();
        pokemon2.resetCoolDown();
    }

    /**
     * Increase stats for winner pokemon
     * @param pokemonReadyToFight1 First pokemon
     * @param pokemonReadyToFight2 Second pokemon
     **/
    private void makeUpdateForWinner (@NotNull Pokemon pokemonReadyToFight1, Pokemon pokemonReadyToFight2) {
        if (!pokemonReadyToFight1.isAlive() && !pokemonReadyToFight2.isAlive()) {
            logg.append("\t\t+++ Result: Draw +++");
            return;
        }
        if (pokemonReadyToFight1.isAlive()) {

            this.pokemonWinner = pokemon1;
            logg.append("\t\t+++ Result: Win ").append(pokemon1.getPokemonType()).append(" +++");
            pokemon1.pokemonWinBattle();
            return;
        }
        if (pokemonReadyToFight2.isAlive()) {
            this.pokemonWinner = pokemon2;
            logg.append("\t\t+++ Result: Win ").append(pokemon2.getPokemonType()).append(" +++");
            pokemon2.pokemonWinBattle();
            return;
        }
    }

    /**
     * Show HP to pokemon with damage
     * @param pokemonReadyToFight1 First pokemon
     * @param pokemonReadyToFight2 Second pokemon
     * @param damageReceived Damage that received first pokemon
     * @param damageReceived2 Damage that received second pokemon
     **/
    private void logHelpDamage (@NotNull Pokemon pokemonReadyToFight1, @NotNull Pokemon pokemonReadyToFight2,
                                int damageReceived, int damageReceived2) {
        logg.append("\t+").append(pokemon1.getPokemonType()).append(" HP: ").append(pokemonReadyToFight1.getHP())
                .append("(Damage received: ").append(damageReceived).append(")").append("\n");

        logg.append("\t+").append(pokemon2.getPokemonType()).append(" HP: ").append(pokemonReadyToFight2.getHP())
                .append("(Damage received: ").append(damageReceived2).append(")").append("\n");
    }

    /**
     * Show CoolDown and Stun state, attack type
     * @param pokemonReadyToFight1 First Pokemon
     * @param pokemonReadyToFight2 Second Pokemon
     * @param statsPokemon1BeforeAttack Stun or not on first Pokemon
     * @param statsPokemon2BeforeAttack Stun or not on second Pokemon
     * @param attackTypePokemon1 Attack type for first Pokemon
     * @param attackTypePokemon2 Attack type for second Pokemon
     **/
    private void logHelp (@NotNull Pokemon pokemonReadyToFight1, @NotNull Pokemon pokemonReadyToFight2,
                          boolean statsPokemon1BeforeAttack, boolean statsPokemon2BeforeAttack,
                          AttackType attackTypePokemon1, AttackType attackTypePokemon2) {
        logg.append("* CoolDown ").append(pokemon1.getPokemonType()).append(": Ability1 [")
                .append(pokemonReadyToFight1.getCoolDown().get(0)).append("]").append(" Ability2 [")
                .append(pokemonReadyToFight1.getCoolDown().get(1)).append("]\n");

        logg.append("* CoolDown ").append(pokemon2.getPokemonType()).append(": Ability1 [")
                .append(pokemonReadyToFight2.getCoolDown().get(0)).append("]").append(" Ability2 [")
                .append(pokemonReadyToFight2.getCoolDown().get(1)).append("]\n");

        if (!statsPokemon1BeforeAttack)
            logg.append(pokemon1.getPokemonType()).append(" attack with ").append(attackTypePokemon1).append(" | ");
        else logg.append(pokemon1.getPokemonType()).append(" is stunned | ");
        if (!statsPokemon2BeforeAttack)
            logg.append(pokemon2.getPokemonType()).append(" attack with ").append(attackTypePokemon2).append("\n");
        else logg.append(pokemon2.getPokemonType()).append(" is stunned\n");
    }

    /**
     * Show pokemon 1 and 2 stats
     * @param pokemon1 First pokemon
     * @param pokemon2 Second pokemon
     **/
    private void showStats (@NotNull Pokemon pokemon1, @NotNull Pokemon pokemon2) {
        showStatForPokemon(pokemon1);
        showStatForPokemon(pokemon2);
    }

    /**
     * Show stats
     * @param pokemon The necessary pokemon
     */
    private void showStatForPokemon (@NotNull Pokemon pokemon) {
        logg.append(pokemon.getPokemonType()).append(" HP: ").append(pokemon.getHP())
                .append(" Normal Attack: ").append(pokemon.getNormalAttack())
                .append(" Special Attack: ").append(pokemon.getSpecialAttack())
                .append(" Defence: ").append(pokemon.getDefense())
                .append(" Special Defence: ").append(pokemon.getSpecialAttack())
                .append(" ").append(pokemon.getAbilities().get(0))
                .append(" ").append(pokemon.getAbilities().get(1))
                .append("\n");
    }

    /**
     * Show start point
     **/
    private void showInfoAboutPokemons () {
        logg.append(pokemon1.getPokemonType());
        logg.append(" challenged ").append(pokemon2.getPokemonType()).append("\n");
        logg.append(pokemon1.getPokemonType()).append(" have: ").append(itemForPokemon1).append("\n");
        logg.append(pokemon2.getPokemonType()).append(" have: ").append(itemForPokemon2).append("\n");
    }

    @Override
    public void run () {
        int i = 0;
        showInfoAboutPokemons();
        Pokemon pokemonReadyToFight1 = null;
        try {
            pokemonReadyToFight1 = pokemon1.inGamePokemon(this.itemForPokemon1);
        } catch (TooManyObjects e) {
            e.printStackTrace();
        }
        Pokemon pokemonReadyToFight2 = null;
        try {
            pokemonReadyToFight2 = pokemon2.inGamePokemon(this.itemForPokemon2);
        } catch (TooManyObjects e) {
            e.printStackTrace();
        }
        showStats(pokemonReadyToFight1, pokemonReadyToFight2);

        while (pokemonReadyToFight1.isAlive() && pokemonReadyToFight2.isAlive()) {
            i++;
            logg.append("\n=====================!!! Round ").append(i).append(" !!!=====================\n");

            boolean statsPokemon1BeforeAttack = pokemonReadyToFight1.isStun();
            boolean statsPokemon2BeforeAttack = pokemonReadyToFight2.isStun();

            AttackType attackTypePokemon1 = ArenaEventsUtils.getAttackTypeForPokemon(pokemonReadyToFight1);
            AttackType attackTypePokemon2 = ArenaEventsUtils.getAttackTypeForPokemon(pokemonReadyToFight2);

            logHelp(pokemonReadyToFight1, pokemonReadyToFight2, statsPokemon1BeforeAttack, statsPokemon2BeforeAttack,
                    attackTypePokemon1, attackTypePokemon2);

            Pokemon finalPokemonReadyToFight = pokemonReadyToFight1.clone();
            Pokemon finalPokemonReadyToFight1 = pokemonReadyToFight2.clone();

            attackPokemons(attackTypePokemon1, attackTypePokemon2, finalPokemonReadyToFight, finalPokemonReadyToFight1,
                           statsPokemon1BeforeAttack,statsPokemon2BeforeAttack);

            int damageReceived = pokemonReadyToFight1.getHP();
            int damageReceived2 = pokemonReadyToFight2.getHP();

            pokemonReadyToFight1 = getPokemonStatsAfterAttack(finalPokemonReadyToFight, pokemonReadyToFight1, pokemon1);
            pokemonReadyToFight2 = getPokemonStatsAfterAttack(finalPokemonReadyToFight1, pokemonReadyToFight2, pokemon2);

            damageReceived -= pokemonReadyToFight1.getHP();
            damageReceived2 -= pokemonReadyToFight2.getHP();
            logHelpDamage(pokemonReadyToFight1, pokemonReadyToFight2, damageReceived, damageReceived2);

            resetDodge(pokemonReadyToFight1, pokemonReadyToFight2);
            setCoolDownForPokemons(pokemonReadyToFight1, pokemonReadyToFight2, finalPokemonReadyToFight,
                                   finalPokemonReadyToFight1);

            // Set conform ability
            setStun(pokemonReadyToFight1, pokemonReadyToFight2, statsPokemon1BeforeAttack, statsPokemon2BeforeAttack,
                    finalPokemonReadyToFight, finalPokemonReadyToFight1);
        }
        logg.append("\n");

        resetStunAndCoolDown();
        setScorePokemon1(pokemonReadyToFight1.getScore());
        setScorePokemon2(pokemonReadyToFight2.getScore());
        makeUpdateForWinner(pokemonReadyToFight1, pokemonReadyToFight2);
        logg.append("\n\n");
    }

}
