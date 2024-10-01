package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void getRandomNeighbor() {
        Room room = new Room("mainRoom");
        Room neighbor = new Room("neighbor");
        room.addNeighbor(neighbor);

        assertEquals(room.getRandomNeighbor(), neighbor);
    }

    @Test
    void testGetRandomNeighborOnRoomWithNoNeighbors() {
        Room room = new Room("onlyRoom");
        assertNull(room.getRandomNeighbor());
    }

    @Test
    void testToString() {
        Room room = new Room("onlyRoom");
        room.add(new Adventurer("Bill"));
        room.add(new Creature("Ogre"));

        System.out.println(room);

        assertTrue(room.toString().contains("onlyRoom"));
        assertTrue(room.toString().contains("Bill"));
        assertTrue(room.toString().contains("Ogre"));
    }

    @Test
    void testGetHealthiestAdventurer() {
        // Arrange
        double highestHealth = 5;
        double lowestHealth = 3;

        Room room = new Room("onlyRoom");
        Adventurer bilbo = new Adventurer("Bilbo", highestHealth);
        room.add(bilbo);
        room.add(new Adventurer("Frodo", lowestHealth));
        Creature troll = new Creature("Troll", highestHealth);
        room.add(troll);
        room.add(new Creature("Orc", lowestHealth));

        // Act
        Adventurer fittestAdventurer = room.getHealthiestAdventurer();
        Creature fittestCreature = room.getHealthiestCreature();

        // Assert
        assertEquals(bilbo, fittestAdventurer);
        assertEquals(troll, fittestCreature);
    }

    @Test
    void testHealthiestEatsFood() {
        // Arrange
        double highestHealth = 5;
        double lowestHealth = 3;

        Room room = new Room("onlyRoom");
        Adventurer bilbo = new Adventurer("Bilbo", highestHealth);
        room.add(bilbo);
        room.add(new Adventurer("Frodo", lowestHealth));
        room.add(new Food("burger"));

        // Act
        bilbo.doAction();

        // Assert
        assertEquals(bilbo.getHealth(), highestHealth + Food.DEFAULT_FOOD_HEALTH_VALUE);
    }
}