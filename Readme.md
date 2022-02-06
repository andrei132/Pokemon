# Proiect Sesiune POO
### Girnet Andrei 321 CB
## Descriere
---
Acest proiect implementeaza o clona simplificata a jocului cu pokemoni, 
unde exista 2 mari evenimente:

+ Antrenamente si pregatiri
    * Lupta cu un pokemon tip Neutrel1 
    * Lupta cu un pokemon tip Neutrel2
    * Lupta cu un pokemon de la alt antrenor

+ Lupta intre antrenori
    * Lupta pokemon[i] de la antrenor1 cu pokemon[i] de la antrenor2
    * Lupta celui mai bun pokemon de la antrenorul1 cu cel mai bun de la 
    antrenorul2
        - Mentiune: Cel mai bun pokemon de la antrenor se decide prin cel mai
        mare scor acumulat pe parcursul luptelor intre toti pokemonii

Ideea principala e sa iti ridici caracteristicile pokemonilor la antrenamente
ca apoi in lupta contra alt antrenor sa ai un pokemon mai bun

## Functionalitate
---
Clasa abstracta Pokemon, este clasa unui pokemon in 3 stari posibile, pokemon pasnic, pokemon in lupta, si pokemon in timpul unui atac, pentru a obtine un pokemon este necesara folosirea de pokemonFactory
```java
class Program{
    
    PokemonFactory pokemonFactory = PokemonFactory.givePokemonFactory();
    Pokemon pokemon = pokemonFactory.givePokemon(pokemonType);

} 
```

Clasa abstarcta Item, este clasa unui item, pentru a obtine un obiect tip Item, folosim ItemFactory
```java
class Program{
    
    ItemFactory itemFactory = ItemFactory.giveItemFactory();
    Pokemon pokemon = pokemonFactory.giveItem(itemType);

} 
```
Clasa Coach, e clasa unui antrenor, pentru a crea un antrenor folosim constructorul

```java
class Program{

    Coach coach = new Coach(nume,varsta);

}
```

Clasa Battle, e clasa care implementeaza Runnable, si poate fi executata de un fir de executie pentru a simula o batalie de orice tip, nu creati o clasa fara motiv, pentru simulare, folositi clasa Game

```java
class Program{

    Battle battle = new Battle(pokemon1, itemForPokemon1, pokemon2, itemForPokemon2)

}
```

Clasa Game e clasa prinipala a jocului, de aici se apeleaza bataliile si toate setarile, informatiile, ce antrenori participa, si ce pokemoni cu obiecte au, exista o singura instanta de Game

```java
class Progrma{

    Game game = Game.getInstance();

}
```

## Utilizare
---
Pentru a rula un joc, este necesar sa incarcati in ./resources 3 fisiere:

+ fisier tip xml, de format din exemplu, unde veti indica id-ul jocului, cine sunt antrenorii, ce pokemoni detin si ce obiecte au antrenorii

+ fisier tip xml, de format din exemplu, unde veti specifica numarul de batalii pentru antrenament si crestere a caracteristicilor pokemonilor dumneavoastra, tot aici veti indica ce pokemon pleaca la batalia de antrenament i, si cu ce obiecte ajutatoare

+ fisier tip xml, de format din exemplu, unde veti indica cine cu cine se va lupta din pokemonii antrenorului1 cu pokemonii antrenorului2 pentru lupta finala

```java
class Progrma{
    public static void main(String[] args){
        Game game = Game.getInstance();
        game.initializeGame("./resources/exempluFisierAntrenori.xml");
        game.startGame("./resources/exempluLupteAntrenament.xml","./resources/exempluFisierLupteFinalePentruCastig.xml",false);
    }
}
```

## Cum ruleaza in spate
---
Cand se creaza o instanta Game, atunci se initializeaza un ArrayList de antrenori
Apoi se vor importa antrenori cu pokemonii si obiectele care le are antrenorul
Fiecare batalie ruleaza pe un fir de executie nou, iar batalia, cand are loc 
atacul se executa inca 2 fire de executie, ca si cum loviturile au loc concomitent
Jocul nu poate fi infuentat din afara, nu exista metode care ar putea interesa un utilizator
Iesirea jocului se creaza automat, la cerere, ultimul camp, daca e setat pe true, in 
./resources/gamesOut/game[Game.ID].out

## Structura Proiectului
---
* Coach
    + Coach - Clasa care defineste un antrenor

* Exception
    + CoachHaveNotThisPokemon - Clasa exceptie care arata ca acest coach nu are acest pokemon
    + PokemonNotFound - Acest Pokemon nu exista in sistem
    + TooManyObjects - Un pokemon a cerut sa detina in joc mai mult de 3 obiecte
    + WrongArenaEvent - Acest evenioment nu este conuscut de arena
    + WrongAttackType - Acest pokemon nu poate indeplini acest tip de atac
    + WrongGameID - Problema cu ID-ul jocului

* Game
    + ArenaEventsUtils - Este folosit pentru a defini ce alegeri ar face arena
    + Battle - Clasa care este rulata si reprezinta o batalie
    + EventType - Enum pentru a reprezentat un eveniment al arenei
    + Game - Toat jocul

* Items
    + Item - Clasa care reprezinta un obiect
    + ItemFactory - clasa care se ocupa de crearea obiectelor necesare
    + ItemType - Enum care reprezinta toate tipurile obiectelor
    + AllItems - Packet cu toate obiectele din joc
        - ...
        - ...
        - ...

* Pokemons
    + Abillity - Clasa care reprezinta o abilitate a unui pokemon
    + AttackType - Enum care reprezinta tipul atacului
    + Pokemon - Clasa care reprezinta un pokemon
    + PokemonFactory - Clasa care se ocupa de crearea pokemonilor de tipul necesar
    + PokemonType - Enum care reprezinta tipul pokemonului
    + AllPokemons - Packet care tine toti pokemoni jocului
        - ...
        - ...
        - ...

* XMLRW
    + XMLParser - Parser pentru un fisier de tip XML

## Designe Paterns folosite
---
+ Prototype
    * Folosesc clone in loc sa instantiez un obiect nou construit
    * Il folosesc pentru a trece dintr-o stare a obiectului Pokemon in alta

+ State
    * Pokemon are 3 stari:
        - Pokemon pasnic
        - Pokemon in batalie
        - Pokemon in timp ce face un atac

+ Factory
    * Nu initializez aparte pokemon cu ajutorul constructorilor, ci folosesc 
    Factory, spunandui ca am nevoie de PekemonType

+ Singletone
    * PokemonFactory
    * ItemFactory
    * Game