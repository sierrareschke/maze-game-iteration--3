package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MazeTest {


    // NEW FROM HW 4


    @Test
    void testSequentialDistribution() {
        Maze maze = Maze.newBuilder()
                .createNFullyConnectedRooms(4)
                .distributeSequentially()
                .createAndAddFoodItems(4)
                .build();

        // Since we added four food items into a four-room maze, with sequential distribution, each room should have some food.
        for (Room room : maze.getRooms()) {
            assertTrue(room.hasFood());
        }
    }

    @Test
    void testRandomDistribution() {
        Maze maze = Maze.newBuilder()
                .createNFullyConnectedRooms(10)
                .distributeRandomly()
                .createAndAddFoodItems(10)
                .build();

        // Since we added 8 food items into a ten-room maze, with random distribution,
        // it's likely (not assured) that one room will not have any food in it.
        assertTrue(maze.getRooms().stream().anyMatch(room -> !room.hasFood()));
    }

    // DONE WITH NEW TESTS






    private static final Logger logger = LoggerFactory.getLogger(MazeTest.class);

    public static String[][] TWO_X_TWO_ROOM_NAMES = new String[][]{
            {"Northwest", "Northeast"},
            {"Southwest", "Southeast"}
    };

    public static String[][] THREE_X_THREE_ROOM_NAMES = new String[][]{
            {"Northwest", "North", "Northeast"},
            {"West", "Center", "East"},
            {"Southwest", "South", "Southeast"}
    };

    @Test
    void testCreationOf2x2Grid() {
        Maze maze = createGridMaze(2, 2, TWO_X_TWO_ROOM_NAMES);
        assertEquals(4, maze.size());
        logger.info(maze.toString());
    }

    @Test
    void testCreationOf3x3Grid() {
        Maze maze = createGridMaze(3, 3, THREE_X_THREE_ROOM_NAMES);
        assertEquals(9, maze.size());
        logger.info(maze.toString());
    }

    public static Maze create2x2GridMaze() {
        return createGridMaze(2, 2, TWO_X_TWO_ROOM_NAMES);
    }

    public static Maze create3x3GridMaze() {
        return createGridMaze(3, 3, THREE_X_THREE_ROOM_NAMES);
    }

    private static Maze createGridMaze(int rows, int columns, String[][] roomNames) {
        Room[][] roomGrid = new Room[rows][columns];
        List<Room> rooms = new ArrayList<>();
        // Notice -- don't use i and j. Use row and column -- they are better
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Room newRoom = new Room(roomNames[row][column]);
                roomGrid[row][column] = newRoom;
                rooms.add(newRoom);
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                Room currentRoom = roomGrid[row][column];
                if (row > 0) {
                    currentRoom.connect(roomGrid[row - 1][column]);
                }
                if (column > 0) {
                    currentRoom.connect(roomGrid[row][column - 1]);
                }
            }
        }

        return new Maze(rooms);
    }

    private Maze createGridMaze(int rows, int columns) {
        String[][] roomNames = new String[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                roomNames[row][column] = "Room " + row + "," + column;
            }
        }
        return createGridMaze(rows, columns, roomNames);
    }

    @Test
    void testHasLivingAdventurers() {
        int rows = 2;
        int columns = 2;
        Maze maze = createGridMaze(rows, columns);

        maze.addToRandomRoom(new Adventurer("Bill"));
        assertTrue(maze.hasLivingAdventurers());

        maze.addToRandomRoom(new Adventurer("Sheri"));
        assertEquals(maze.getLivingAdventurers().size(), 2);
    }

    @Test
    void testHasLivingCreatures() {
        Maze maze = createGridMaze(3, 3);
        maze.addToRandomRoom(new Creature("Ogre"));
        assertTrue(maze.hasLivingCreatures());

        maze.addToRandomRoom(new Creature("Dragon"));
        assertEquals(maze.getLivingCreatures().size(), 2);
    }

    @Test
    void testToString() {
        Maze maze = createGridMaze(2, 2, TWO_X_TWO_ROOM_NAMES);
        maze.addToRandomRoom(new Adventurer("Bill"));
        maze.addToRandomRoom(new Creature("Ogre"));

        String mazeString = maze.toString();
        logger.info(mazeString);

        assertTrue(mazeString.contains("Northwest"));
        assertTrue(mazeString.contains("Bill"));
        assertTrue(mazeString.contains("Ogre"));
    }
}