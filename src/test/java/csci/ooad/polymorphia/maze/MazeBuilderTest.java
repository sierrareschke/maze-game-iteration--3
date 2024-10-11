package csci.ooad.polymorphia.maze;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Room;
import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.characters.Demon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MazeBuilderTest {
    private Maze.MazeBuilder builder;

    @BeforeEach
    void setupBuilder() {
        builder = Maze.newBuilder();
    }

    @Test
    void testCreateNbyMGrid() {
        Maze maze = builder.createNbyMGrid(2, 2).build();
        List<Room> rooms = maze.getRooms();
        assertEquals(4, maze.size());
        rooms.forEach(room -> assertFalse(room.getNeighbors().isEmpty()));
    }

    @Test
    void testCreateFullyConnectedRooms() {
        Maze maze = builder.createNFullyConnectedRooms(3).build();
        List<Room> rooms = maze.getRooms();
        rooms.forEach(room -> assertEquals(2, room.getNeighbors().size()));
    }

    @Test
    void testDistributeRandomly() {
        Maze randomMaze = builder.createNbyMGrid(2, 2).distributeRandomly().build();
        assertNotNull(randomMaze);
        assertEquals(4, randomMaze.size());
    }

    @Test
    void testDistributeSequentially() {
        Maze sequentialMaze = builder.createNbyMGrid(2, 2).distributeSequentially().build();
        assertNotNull(sequentialMaze);
        assertEquals(4, sequentialMaze.size());
    }

    @Test
    void testAddRoomAndConnect() {
        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        builder.addRoom(room1.getName())
                .addRoom(room2.getName())
                .build();
        room1.connect(room2);

        assertTrue(room1.getNeighbors().contains(room2));
        assertTrue(room2.getNeighbors().contains(room1));
    }

    @Test
    void testPlaceAdventurerIntoRoom() {
        Room targetRoom = new Room("Target Room");
        Adventurer adventurer = new Adventurer("Brave Adventurer");
        builder.addRoom(targetRoom.getName()).placeObjectIntoRoom(adventurer, targetRoom).build();

        assertTrue(targetRoom.getLivingAdventurers().contains(adventurer));
    }

    @Test
    void testPlaceCreatureIntoRoom() {
        Room targetRoom = new Room("Target Room");
        Creature creature = new Creature("Wild Creature");
        builder.addRoom(targetRoom.getName())
                .placeObjectIntoRoom(creature, targetRoom)
                .build();

        assertTrue(targetRoom.getLivingCreatures().contains(creature));
    }

    @Test
    void testPlaceFoodIntoRoom() {
        Room targetRoom = new Room("Target Room");
        Food food = new Food("Bread");
        builder.addRoom(targetRoom.getName())
                .placeObjectIntoRoom(food, targetRoom)
                .build();

        assertTrue(targetRoom.hasFood());
    }

    @Test
    void testCreateAndAddFoodItems() {
        Maze foodMaze = builder.createNbyMGrid(2, 2).createAndAddFoodItems(3).build();
        assertNotNull(foodMaze);
        List<Room> rooms = foodMaze.getRooms();
        assertTrue(rooms.stream().anyMatch(Room::hasFood));
    }

    @Test
    void testCreateAndAddAdventurers() {
        Maze adventurerMaze = builder.createNbyMGrid(2, 2).createAndAddAdventurers(3).build();
        List<Adventurer> adventurers = adventurerMaze.getLivingAdventurers();
        assertEquals(3, adventurers.size());
    }

    @Test
    void testCreateAndAddCreatures() {
        Maze creatureMaze = builder.createNbyMGrid(2, 2).createAndAddCreatures(2, true).build();
        List<Creature> creatures = creatureMaze.getLivingCreatures();
        assertEquals(2, creatures.size());
    }

    @Test
    void testAddSpecificCharacters() {
        Maze customMaze = builder.createNbyMGrid(2, 2)
                .createAndAddAdventurers(1, 1, 1, 1) // Adds knights, cowards, gluttons, and regulars
                .build();

        List<Adventurer> adventurers = customMaze.getLivingAdventurers();
        assertEquals(4, adventurers.size());
    }

    @Test
    void testAddSpecificCreaturesWithDemons() {
        Maze demonMaze = builder.createNbyMGrid(2, 2).createAndAddCreatures(1, true).build();
        List<Creature> creatures = demonMaze.getLivingCreatures();
        assertEquals(1, creatures.size());
        assertTrue(creatures.get(0) instanceof Demon);
    }
}
