package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    Character joe;

    @BeforeEach
    void setUp() {
        joe = new Adventurer("Joe", 5.0);
    }

    @Test
    void testToString() {
        System.out.println(joe);

        assertTrue(joe.toString().contains("Joe"));
    }

    @Test
    void isAlive() {
        assertTrue(joe.isAlive());
    }

    @Test
    void testSorting() {
        double mostHealth = 10.0;
        double mediumHealth = 5.0;
        double leastHealth = 3.0;

        List<Character> characters = new ArrayList<>(Arrays.asList(
                new Adventurer("Bill", mostHealth),
                new Creature("Ogre", mediumHealth),
                new Adventurer("Sheri", leastHealth)));

        Collections.sort(characters);

        System.out.println(characters);
    }

    @Test
    void testLoseHealthAndDeath() {
        joe.loseHealth(3.0);
        assertEquals(2.0, joe.getHealth());

        joe.loseHealth(2.0);
        assertFalse(joe.isAlive());
    }

    @Test
    void testFightingMandatoryLossOfHalfAPoint() {
        Creature ogre = new Creature("Ogre");
        joe.fight(ogre);
        System.out.println("After the fight, Joe's health is: " + joe.getHealth());

        // Joe should have lost 0.5 health and he started with an integer health value
        // of 5.0. After the fight he should have 4.5 health. Or 3.5, or 2.5, etc. depending
        // upon the outcome of the fight. So, we just check to make sure the health is x.5
        assertEquals(0.5, joe.getHealth() % 1);
    }

    @Test
    void testMovingRoomLossOfQuarterPoint() {
        Room currentRoom = new Room("currentRoom");
        Room newRoom = new Room("newRoom");
        currentRoom.addNeighbor(newRoom);
        joe.enterRoom(currentRoom);

        // Since nothing is in the current room with Joe, he should move to the new room
        joe.doAction();

        System.out.println("After moving rooms, Joe's health is: " + joe.getHealth());
        assertEquals(4.75, joe.getHealth());
        assertEquals(newRoom, joe.getCurrentLocation());
    }

    @Test
    void testMovingWithNoNeighbors() {
        Room room = new Room("room");
        Adventurer adventurer = new Adventurer("Adventurer");
        room.add(adventurer);

        // Act -- no error occurs
        adventurer.doAction();
    }

    @Test
    void testEatingFood() {
        Room room = new Room("room");
        Adventurer adventurer = new Adventurer("Adventurer");
        room.add(adventurer);
        Food popcorn = new Food("popcorn");
        room.add(popcorn);

        adventurer.doAction();

        assertEquals(adventurer.getHealth(), Character.DEFAULT_INITIAL_HEALTH + popcorn.getHealthValue() );
        assertFalse(room.hasFood());
    }

    @Test
    void testFighting() {
        Adventurer adventurer = new Adventurer("Adventurer");
        Creature creature = new Creature("Creature");

        double initialHealth = adventurer.getHealth();
        adventurer.fight(creature);

        assertNotEquals(initialHealth, adventurer.getHealth());
    }

    @Test
    void testCreatureDoesNotDoAction() {
        Creature creature = new Creature("Creature");
        creature.doAction();
    }


    // TODO - add tests for Demon, Coward, Glutton, Knight
}