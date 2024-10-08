package csci.ooad.polymorphia.factories;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CharacterFactoryTest {

    @BeforeEach
    private void setUp(){
        String[][] THREE_X_THREE_ROOM_NAMES = new String[][]{
                {"Northwest", "North", "Northeast"},
                {"West", "Center", "East"},
                {"Southwest", "South", "Southeast"}
        };


    }

    @Test
    public void testCreateCharacter(){

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
}
