package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.Demon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {
    private Maze maze;

    @BeforeEach
    void setupMaze() {
        maze = Maze.newBuilder()
                .createNbyMGrid(2, 2)
                .distributeSequentially()
                .createAndAddFoodItems(2)
                .createAndAddAdventurers(2)
                .createAndAddCreatures(1, true)
                .build();
    }

    @Test
    void testMazeSize() {
        assertEquals(4, maze.size());
    }

    @Test
    void testGetRooms() {
        List<Room> rooms = maze.getRooms();
        assertNotNull(rooms);
        assertEquals(4, rooms.size());
    }

    @Test
    void testHasLivingCreatures() {
        assertTrue(maze.hasLivingCreatures());
    }

    @Test
    void testHasLivingAdventurers() {
        assertTrue(maze.hasLivingAdventurers());
    }

    @Test
    void testGetLivingAdventurers() {
        List<Adventurer> adventurers = maze.getLivingAdventurers();
        assertNotNull(adventurers);
        assertFalse(adventurers.isEmpty());
    }

    @Test
    void testGetLivingCreatures() {
        List<Creature> creatures = maze.getLivingCreatures();
        assertNotNull(creatures);
        assertFalse(creatures.isEmpty());
    }

    @Test
    void testGetLivingCharacters() {
        List<Character> characters = maze.getLivingCharacters();
        assertNotNull(characters);
        assertFalse(characters.isEmpty());
    }

    @Test
    void testAddToRandomRoomCharacter() {
        Character character = new Adventurer("Test Adventurer");
        maze.addToRandomRoom(character);
        assertTrue(maze.getLivingCharacters().contains(character));
    }

    @Test
    void testAddToRandomRoomFood() {
        Food food = new Food("Test Food");
        maze.addToRandomRoom(food);
        List<Room> rooms = maze.getRooms();
        assertTrue(rooms.stream().anyMatch(room -> room.hasFood()));
    }

    @Test
    void testToString() {
        String mazeDescription = maze.toString();
        assertNotNull(mazeDescription);
        assertFalse(mazeDescription.isEmpty());
    }
}
