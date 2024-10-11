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
                new Adventurer("Scrambler", leastHealth)));

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
        Adventurer adventurer = new Adventurer("Minion");
        room.add(adventurer);
        Food popcorn = new Food("popcorn");
        room.add(popcorn);

        adventurer.doAction();

        assertEquals(adventurer.getHealth(), Character.DEFAULT_INITIAL_HEALTH + popcorn.getHealthValue() );
        assertFalse(room.hasFood());
    }

    @Test
    void testFighting() {
        Adventurer adventurer = new Adventurer("Minion");
        Creature creature = new Creature("Satan");

        double initialHealth = adventurer.getHealth();
        adventurer.fight(creature);

        assertNotEquals(initialHealth, adventurer.getHealth());
    }

    @Test
    void testCreatureDoesNotDoAction() {
        Creature creature = new Creature("Minion");
        creature.doAction();
    }

    @Test
    void testHealthiestInRoom() {
        Room testRoom;
        Adventurer healthiestAdventurer;
        Adventurer weakerAdventurer1;
        Adventurer weakerAdventurer2;

        // Initialize a room
        testRoom = new Room("Test Room");

        // Create adventurers with different health levels
        healthiestAdventurer = new Adventurer("Healthiest", 10.0);  // Highest health
        weakerAdventurer1 = new Adventurer("Weaker1", 5.0);  // Lower health
        weakerAdventurer2 = new Adventurer("Weaker2", 7.0);  // Lower health

        // Place all adventurers in the room
        testRoom.add(healthiestAdventurer);
        testRoom.add(weakerAdventurer1);
        testRoom.add(weakerAdventurer2);

        // Test the healthiest adventurer
        assertTrue(healthiestAdventurer.iAmHealthiestInRoom(),
                "Healthiest adventurer should be recognized as the healthiest.");
        // Test the weaker adventurers
        assertFalse(weakerAdventurer1.iAmHealthiestInRoom(),
                "Weaker adventurer should not be recognized as the healthiest.");
        assertFalse(weakerAdventurer2.iAmHealthiestInRoom(),
                "Weaker adventurer should not be recognized as the healthiest.");
    }


    // TODO - add and check tests for Demon, Coward, Glutton, Knight
    @Test
    void testKnightAlwaysFightsCreatures() {
        Knight knight = new Knight("Knight Minion");
        Adventurer testAdventurer = new Adventurer("Adventurer Minion");

        Creature ogre = new Creature("Ogre");
        Room room = new Room("Battlefield");
        room.add(knight);
        room.add(ogre);

        knight.loseHealth(4.0);

        // check (updated) health values are correct
        assertEquals(4.0, knight.getHealth());
        assertEquals(5.0, testAdventurer.getHealth());
        assertEquals(3.0, ogre.getHealth());

        knight.doAction(); // knight should fight ogre, even if not healthiest in room

        // both characters should lose health from fighting regardless of outcome
        assertTrue(ogre.getHealth() < 3.0);
        assertTrue(knight.getHealth() < 4.0);
    }

    @Test
    void testCowardRunsFromCreature() {
        Coward coward = new Coward("Tim the Coward");
        Creature ogre = new Creature("Ogre");
        Room room1 = new Room("Forest");
        Room room2 = new Room("Cliff");
        room1.addNeighbor(room2);

        room1.add(coward);
        room1.add(ogre);

        // check (updated) health values are correct
        assertEquals(5.0, coward.getHealth());
        assertEquals(3.0, ogre.getHealth());

        coward.doAction(); // coward should run

        assertEquals(4.5, coward.getHealth()); // Coward loses 0.5 health for running away
        assertNotEquals(room1, coward.getCurrentLocation()); // Coward should move to a new room
    }

    @Test
    void testCowardFightsDemon() {
        Coward coward = new Coward("Bill the Coward");
        Creature ogre = new Creature("Ogre");
        Demon demon = new Demon("Satan");
        Room room1 = new Room("Flatirons");
        Room room2 = new Room("Trail");
        room1.addNeighbor(room2);

        room1.add(coward);
        room1.add(ogre);
        room1.add(demon);

        // check (updated) health values are correct
        assertEquals(5.0, coward.getHealth());
        assertEquals(3.0, ogre.getHealth());

        coward.doAction(); // Coward should fight Demon

        assertTrue(coward.getHealth() < 5.0); // Coward loses health from fighting
        assertEquals(room1, coward.getCurrentLocation()); // Coward should not move to a new room
    }


    @Test
    void testCowardCannotRunFromDemon() {
        Coward coward = new Coward("Tim the Coward");
        Demon demon = new Demon("Satan");
        Room room = new Room("Dungeon");
        room.add(coward);
        room.add(demon);

        coward.doAction();

        assertTrue(demon.isAlive()); // Demon fights the Coward, but does not run
        assertTrue(coward.getHealth() < Character.DEFAULT_INITIAL_HEALTH);
    }

    @Test
    void testGluttonEatsWhenOneFoodIsAvailable() {
        Glutton glutton = new Glutton("Glutton Minion");
        Food apple = new Food("Apple");
        Room room = new Room("Kitchen");
        room.add(glutton);
        room.add(apple);

        double initialGluttonHealth = glutton.getHealth();

        // check initial health values are correct
        assertEquals(3.0, initialGluttonHealth);
        assertEquals(1.0, apple.getHealthValue());

        glutton.doAction(); // glutton should eat one food item

        // Glutton should eat single food item present
        assertEquals(initialGluttonHealth + apple.getHealthValue(), glutton.getHealth());
        assertFalse(room.hasFood()); // Glutton should have eaten the food
    }

    @Test
    void testGluttonEatsWhenMultipleFoodIsAvailable() {
        Glutton glutton = new Glutton("TestGlutton");
        Food apple = new Food("Apple");
        Food cake = new Food("Cake");

        Room room = new Room("Kitchen");
        room.add(glutton);
        room.add(apple);
        room.add(cake);

        double initialGluttonHealth = glutton.getHealth();

        // check initial health values are correct
        assertEquals(3.0, initialGluttonHealth);
        assertEquals(1.0, apple.getHealthValue());
        assertEquals(1.0, cake.getHealthValue());

        glutton.doAction(); // glutton should eat all food items

        // Glutton should eat all food items present
        assertEquals(initialGluttonHealth + apple.getHealthValue() + cake.getHealthValue(), glutton.getHealth());
        assertFalse(room.hasFood()); // Glutton should have eaten all food
    }

    @Test
    void testGluttonFightsDemonInsteadOfEating() {
        Glutton glutton = new Glutton("Hungry Minion");
        Demon demon = new Demon("Satan");
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
        Demon demon = new Demon("Satan");
        Adventurer adventurer1 = new Adventurer("Minion Extraordinaire", 10.0);
        Adventurer adventurer2 = new Adventurer("Imperial Grand Minion", 12.0);
        Room room = new Room("Satan's Lair");

        room.add(demon);
        room.add(adventurer1);
        room.add(adventurer2);

        // check initial health values are correct
        assertEquals(10.0, adventurer1.getHealth());
        assertEquals(12.0, adventurer2.getHealth());
        assertEquals(15.0, demon.getHealth());

        demon.doAction();

        assertTrue(adventurer1.getHealth() < 10.0);
        assertTrue(adventurer2.getHealth() < 12.0);
    }
}