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


    // TODO - add and check tests for Demon, Coward, Glutton, Knight (haven't ran yet)
    @Test
    void testKnightAlwaysFightsCreatures() {
        Knight knight = new Knight("Sir Lancelot");
        Creature ogre = new Creature("Ogre");
        Room room = new Room("Battlefield");
        room.add(knight);
        room.add(ogre);

        knight.doAction();

        assertFalse(ogre.isAlive());
        assertTrue(knight.isAlive());
    }

    @Test
    void testCowardRunsFromCreature() {
        Coward coward = new Coward("Tim the Coward");
        Creature ogre = new Creature("Ogre");
        Room room = new Room("Forest");
        room.add(coward);
        room.add(ogre);

        coward.doAction();

        assertEquals(4.5, coward.getHealth()); // Coward loses 0.5 health for running away
        assertNotEquals(room, coward.getCurrentLocation()); // Coward should move to a new room
    }

    @Test
    void testCowardCannotRunFromDemon() {
        Coward coward = new Coward("Tim the Coward");
        Demon demon = new Demon("Fierce Demon");
        Room room = new Room("Dungeon");
        room.add(coward);
        room.add(demon);

        coward.doAction();

        assertTrue(demon.isAlive()); // Demon fights the Coward, but does not run
        assertTrue(coward.getHealth() < Character.DEFAULT_INITIAL_HEALTH);
    }

    @Test
    void testGluttonEatsWhenFoodIsAvailable() {
        Glutton glutton = new Glutton("Hungry Hal");
        Food apple = new Food("Apple");
        Room room = new Room("Kitchen");
        room.add(glutton);
        room.add(apple);

        glutton.doAction();

        assertEquals(Character.DEFAULT_INITIAL_HEALTH + apple.getHealthValue(), glutton.getHealth());
        assertFalse(room.hasFood()); // Glutton should have eaten the food
    }

    @Test
    void testGluttonFightsDemonInsteadOfEating() {
        Glutton glutton = new Glutton("Hungry Hal");
        Demon demon = new Demon("Fierce Demon");
        Food apple = new Food("Apple");
        Room room = new Room("Battlefield");
        room.add(glutton);
        room.add(demon);
        room.add(apple);

        glutton.doAction();

        assertTrue(demon.isAlive() && glutton.getHealth() < Character.DEFAULT_INITIAL_HEALTH); // Glutton must fight instead of eat
        assertTrue(room.hasFood()); // Glutton didn’t eat the food
    }

    @Test
    void testDemonFightsAllAdventurers() {
        Demon demon = new Demon("Fierce Demon");
        Adventurer adventurer1 = new Adventurer("Adventurer1", 10.0);
        Adventurer adventurer2 = new Adventurer("Adventurer2", 12.0);
        Room room = new Room("Arena");
        room.add(demon);
        room.add(adventurer1);
        room.add(adventurer2);

        demon.doAction();

        assertFalse(adventurer1.isAlive());
        assertFalse(adventurer2.isAlive());
    }
}