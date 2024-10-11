package csci.ooad.polymorphia.factories;

import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CharacterFactoryTest {

    @BeforeEach
    public void setUp(){
        String[][] THREE_X_THREE_ROOM_NAMES = new String[][]{
                {"Northwest", "North", "Northeast"},
                {"West", "Center", "East"},
                {"Southwest", "South", "Southeast"}
        };
    }

    @Test
    public void testCreateCharacter(){

    }
    
}
