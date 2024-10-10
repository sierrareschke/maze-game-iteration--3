package csci.ooad.polymorphia.factories;

// Create concrete classes for Knights, Cowards, Gluttons, regular Adventurers, regular Creatures, and Demons

import csci.ooad.polymorphia.Die;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.characters.*;
import csci.ooad.polymorphia.characters.Character;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CharacterFactory {
    private final String[] creatureTypes = {"Ogre", "Troll", "Goblin", "Vampire", "Ghoul", "Ghost"};

    public ArrayList<Character> createCharacters(int numKnights, int numCowards, int numGluttons, int numCreatures) {
        // Check for null and establish a default
        if (numKnights == -1) numKnights = 2;
        if (numCowards == -1) numCowards = 2;
        if (numGluttons == -1) numGluttons = 2;
        if (numCreatures == -1) numCreatures = 4;

        ArrayList<Character> knights = createKnights(numKnights);
        ArrayList<Character> cowards = createCowards(numCowards);
        ArrayList<Character> gluttons = createGluttons(numGluttons);
        ArrayList<Character> creatures = createCreatures(numCreatures);

        ArrayList<Character> allCharacters = new ArrayList<>();
        allCharacters.addAll(knights);
        allCharacters.addAll(cowards);
        allCharacters.addAll(gluttons);
        allCharacters.addAll(creatures);

        return allCharacters;
    }

    /*
    * createAdventurers(int numKnights, int numCowards, int numGluttons, int numRegular)
    *
    * createAdventurers(int numAdventurers) // return specified num adventurers with rand types
    *
    * createCreatures(int numRegular, boolean isDemon) // returns number of regular creatures AND ONE DEMON?? add a check for demon??
    *
    * */

    public ArrayList<Character> createKnights(int numberOfKnights){
        // Check for null and establish a default
        if (numberOfKnights == -1) numberOfKnights = 2;

        ArrayList<Character> knights = new ArrayList<>();
        for (int i = 0; i < numberOfKnights; i++) {
            String name = "Knight_" + i + 1;
            knights.add(new Knight(name));
        }
        return knights;
    }

    public ArrayList<Character> createGluttons(int numberOfGluttons){
        ArrayList<Character> gluttons = new ArrayList<>();
        for (int i = 0; i < numberOfGluttons; i++) {
            String name = "Glutton_" + i + 1;
            gluttons.add(new Glutton(name));
        }
        return gluttons;
    }

    public ArrayList<Character> createCowards(int numberOfCowards){
        ArrayList<Character> cowards = new ArrayList<>();
        for (int i = 0; i < numberOfCowards; i++) {
            String name = "Cowards_" + i + 1;
            cowards.add(new Coward(name));
        }
        return cowards;
    }

    public Character createDemon(){
        return new Demon("Demon");
    }

    // TODO - make static? AND account for isDemon
    public ArrayList<Character> createCreatures(int numberOfCreatures, boolean isDemon){
        ArrayList<Character> creatures = new ArrayList<>();
        if (isDemon) {
            creatures.add(createDemon());
        }
        // TODO - is demon included in the count of numberOfCreatures? i.e. should i go to numberOfCreatures-1 or numberOfCreatures
        for (int i = 0; i < numberOfCreatures-1; i++) {
            int diceRoll = Die.rollSixSided();
            creatures.add(new Creature(this.creatureTypes[diceRoll]));
        }
        return creatures;
    }

    private String capitalizeFirstCharacter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        // Capitalize the first character and concatenate with the rest of the string
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
