package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.Creature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


public class Polymorphia {
    private static final Logger logger = LoggerFactory.getLogger(Polymorphia.class);

    Maze maze;
    Integer turnCount = 0;
    final Random rand = new Random();

    public Polymorphia(Maze maze) {
        this.maze = maze;
    }

    public String toString() {
        return "Polymorphia MAZE: turn " + turnCount + "\n" + maze.toString();
    }

    // Game is over when all creatures are killed
    // or all adventurers are killed
    public Boolean isOver() {
        return !hasLivingCreatures() || !hasLivingAdventurers();
    }

    public Boolean hasLivingCreatures() {
        return maze.hasLivingCreatures();
    }

    public Boolean hasLivingAdventurers() {
        return maze.hasLivingAdventurers();
    }

    public void playTurn() {
        if (turnCount == 0) {
            logger.info("Starting play...");
        }
        turnCount += 1;

        // Process all the characters in random order
        List<Character> characters = getLivingCharacters();
        while (!characters.isEmpty()) {
            int index = rand.nextInt(characters.size());
            characters.get(index).doAction();
            characters.remove(index);
        }
    }

    public List<Character> getLivingCharacters() {
        return maze.getLivingCharacters();
    }


    public void play() {
        while (!isOver()) {
            logger.info(this.toString());
            playTurn();
        }
        logger.info("The game ended after {} turns.", turnCount);
        String eventDescription;
        if (hasLivingAdventurers()) {
            eventDescription = "The adventurers won! Left standing are:\n" + getAdventurerNames() + "\n";
        } else if (hasLivingCreatures()) {
            eventDescription = "The creatures won! Left standing are:\n" + getCreatureNames() + "\n";
        } else {
            eventDescription = "No team won! Everyone died!\n";
        }
        logger.info(eventDescription);
    }

    String getAdventurerNames() {
        return String.join("\n ", getLivingCharacters().stream().map(Object::toString).toList());
    }

    String getCreatureNames() {
        return String.join("\n ", getAliveCreatures().stream().map(Object::toString).toList());
    }

    public List<Creature> getAliveCreatures() {
        return maze.getLivingCreatures();
    }

    public Character getWinner() {
        if (!isOver() || !hasLivingCharacters()) {
            // No one has won yet or no one won -- all died
            return null;
        }
        return getLivingCharacters().getFirst();
    }

    private boolean hasLivingCharacters() {
        return !getLivingCharacters().isEmpty();
    }
}