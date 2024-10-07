package csci.ooad.polymorphia.factories;

// Create concrete classes for Knights, Cowards, Gluttons, regular Adventurers, regular Creatures, and Demons

import csci.ooad.polymorphia.Die;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.characters.*;
import csci.ooad.polymorphia.characters.Character;

public class CharacterFactory {
    private final String[] creatureTypes = {"Ogre", "Troll", "Goblin", "Vampire", "Ghoul", "Ghost"};
    public Character createCharacter(String characterType, Maze maze) {
        int numberOfAdventures = maze.getLivingAdventurers().size();
        String adventurerName = capitalizeFirstCharacter(characterType) + numberOfAdventures;

        switch (characterType.toLowerCase()) {
            case "knight":
                return new Knight(adventurerName);
            case "demon":
                return new Demon("Demon");
            case "glutton":
                return new Glutton(adventurerName);
            case "coward":
                return new Coward(adventurerName);
            case "creature":
                int diceRoll = Die.rollSixSided();
                return new Creature(this.creatureTypes[diceRoll]);
            default:
                throw new IllegalArgumentException("Unknown character type: " + characterType);
        }
    }

    private String capitalizeFirstCharacter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        // Capitalize the first character and concatenate with the rest of the string
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
