package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.Creature;

import java.util.*;


/*
* ACTION ITEMS
*   CREATE BUILDER & METHODS BEFORE FACTORY
*          * .createNByMGrid (int n,int m) - constructs a "grid-layout" maze of N rows and M columns
*          * .createNFullyConnectedRooms(int n) - consstructs a maze with n rooms where all rooms are fully connected
*          * .distributeSequentially() - set distribution method to sequential (boolean / 0 or 1)
*          * distributeRandomly() - set distribution method to random
*          * addRoomWithName(string roomName)
*          * placeObjectIntoRoom(Object object,Room room) - place object into specified room
*
*   REGULAR METHODS
*       * addToRoom method (object,Room)
*
*   CREATE BUILDER & METHODS AFTER FACTORY
*           * Implement Factory Pattern inside of builder
 *          * createAndAddFoodItems(strings)
 *          * createAndAddAdventurers(...) - dont know arguments rn
 *          * createAndAddCreatures(...) - dont know arguments rn
 *          * createAndAddRandFoodItems(int n)
 *          * createAndAddRandAdventurers(int n)
 *          * createAndAddRandCreatures(int n)
 *          * ACCOUNT FOR SEQ DIST & RAND DIST IN EACH OF METHODS
* */


/*
* SEQUENTIAL DISTRIBUTION NOTES
* One way to implement the sequential distribution is to keep an index to your room list (in the Builder class) and each time through the
* loop of distributing any object, just call nextRoom() and it would return the next room in the sequence.

So, if you had say 4 rooms, and you distributed three adventurers first, then three creatures, then five food items, you’d get:

Room 1: adventurer1, creature2, food4
Room 2: adventurer2, food1, food5
Room 3: adventurer3, food2
Room 4: creature1, food3
*
But you could start the distribution of each type with Room 1. That's your choice. The point is that no room will get two
*  of anything until all rooms get one of it. Which is different from a random distribution.
* */


/*
* BUILDER NOTES
* Finally, the Builder class needs to implement the build method, which returns the fully-constructed Maze object:

Maze build() {
    ...
}
* */


/*
* FACTORY PATTERN NOTES
* Adding the Factory Pattern (15 points)

The Builder class should use factories to create adventurers, creatures, and food (see below) make sure that it creates a valid Maze.
* Meaning that if you have more than one room in your maze, each room should be connected to at least one other room – no rooms that have no way to
*  access them from outside the room.

The Maze.Builder can use the new keyword to create Rooms, the Maze, and common objects like ArrayList, Random, HashMap, etc.

Use the Simple Factory pattern and create these factories:

CharacterFactory (creates five concrete classes)
FoodFactory (creates one concrete class)
Each of these factories should have methods that create each concrete type. Feel free to add other helper methods to these factories like:

myCharacterFactory.createKnightsWithCoolNames(5);
myFoodFactory.createFoodItems(10);
myFoodFactory.createFoodItems("Steak", "Popcorn", "Fries");
You can create methods that take a variable number of arguments like this, where items becomes an array of Strings inside the method:

static List<Food> createFoodItems(String... items) {
    return Arrays.stream(items)
            .map(item -> new Food(item))
            .toList();
}
The FoodFactory doesn't really gain us much at this point because we don’t have any subclasses of Food, but we’re doing it here for consistency.
*  Yes, there are exceptions to everything. Having consistency might override our usual policy of “No Single-Use Abstractions”.

Remember that these factories should not expose the subclasses of Adventurer, Knight, Glutton, Coward, Creature, Demon, and Food (there are none of Food).

Pass these factories into your Maze.Builder class at construction time, or it's okay for Maze.Builder to create the factories itself.
*
* */




public class Maze  {
    private final Random rand = new Random();

    private final List<Room> rooms;

    private Maze(List<Room> rooms) {
        this.rooms = rooms;
    }

    public static class Builder {
        public static Builder getNewBuilder() {
            return new Builder();
        }
    }

    public int size() {
        return rooms.size();
    }

    public String toString() {
        return String.join("\n\n", rooms.stream().map(Object::toString).toList());
    }

    public Boolean hasLivingCreatures() {
        return rooms.stream().anyMatch(Room::hasLivingCreatures);
    }

    public Boolean hasLivingAdventurers() {
        return rooms.stream().anyMatch(Room::hasLivingAdventurers);
    }

    private Room getRandomRoom() {
        return rooms.get(rand.nextInt(rooms.size()));
    }

    public List<Adventurer> getLivingAdventurers() {
        List<Adventurer> adventurers = new ArrayList<>();
        for (Room room : rooms) {
            adventurers.addAll(room.getLivingAdventurers());
        }
        return adventurers;
    }

    public List<Creature> getLivingCreatures() {
        List<Creature> creatures = new ArrayList<>();
        for (Room room : rooms) {
            creatures.addAll(room.getLivingCreatures());
        }
        return creatures;
    }

    public List<Character> getLivingCharacters() {
        List<Character> characters = new ArrayList<>();
        for (Room room : rooms) {
            characters.addAll(room.getLivingCharacters());
        }
        return characters;
    }

    public void addToRandomRoom(Character character) {
        getRandomRoom().add(character);
    }

    public void addToRandomRoom(Food foodItem) {
        getRandomRoom().add(foodItem);
    }
}
