package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolymorphiaTest {
    public static List<String> ADVENTURER_NAMES = Arrays.asList("Galahad", "Bilbo", "Gandalf");
    public static List<String> CREATURE_NAMES = Arrays.asList("Ogre", "Troll", "Dragon", "Orc", "Shelob");
    public static List<String> FOOD_NAMES = Arrays.asList(
            "Cookie", "Banana", "Steak", "Fries", "Burger",
            "Ice Cream", "Coffee", "Coke", "Pizza", "Pasta");


    @Test
    void testOneRoom() {
        // Baby steps!
        Maze maze = Maze.newBuilder()
                .addRoom(new Room("onlyRoom"))
                .distributeSequentially()
                .createAndAddAdventurers(new Adventurer("Bill"))
                .createAndAddCreatures(new Creature("Ogre"), true)
                .build();

        Polymorphia game = new Polymorphia(maze);

        // Act
        game.play();

        // Assert
        assert game.isOver();
    }

    @Test
    public void test2x2Game() {
        // Arrange
        Maze maze = Maze.newBuilder()
                .createNbyMGrid(2,2)
                .distributeSequentially()
                .createAndAddAdventurers(1)
                .createAndAddCreatures(1, false)
                .build();

        Polymorphia game = new Polymorphia(maze);

        // Act
        game.play();

        // Assert
        assert game.isOver();
    }

    @Test
    public void test3x3Game() {
        // Arrange
        Maze maze = Maze.newBuilder()
                .createNbyMGrid(3,3)
                .distributeSequentially()
                .createAndAddAdventurers(2)
                .createAndAddCreatures(4, true)
                .createAndAddFoodItems(10)
                .build();

        Polymorphia game = new Polymorphia(maze);

        // Act
        game.play();

        // Assert
        assert game.isOver();
    }

    @Test
    void testFairPlay() {
        int adventurerWins = 0;
        int creatureWins = 0;
        int numTies = 0;

        int TOTAL_GAMES = 100;
        for (int i = 0; i < TOTAL_GAMES; i++) {
            Maze maze = Maze.newBuilder()
                    .createNbyMGrid(3,3)
                    .distributeSequentially()
                    .createAndAddAdventurers(2)
                    .createAndAddCreatures(4, true)
                    .createAndAddFoodItems(10)
                    .build();
            Polymorphia game = new Polymorphia(maze);
            game.play();
            if (game.getWinner() == null) {
                numTies++;
            } else if (game.getWinner().isCreature()) {
                creatureWins++;
            } else {
                adventurerWins++;
            }
        }

        System.out.println("Adventurers won " + adventurerWins + " and creatures won " + creatureWins);
        System.out.println("There were " + numTies + " games with no winners");

        double adventureWinRatio = (double) adventurerWins / (double) TOTAL_GAMES;
        System.out.println("Adventures won " + (adventureWinRatio * 100) + "% of the games.");

        // Check to see that adventurers win at least 1% of the games
        assertTrue(adventureWinRatio > 0.01);
    }
}
