package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.Creature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(Maze.class);

    private final List<Room> rooms;
    private final boolean distributeRandomly; // 1 = rand, 0 = simultaneous distribution


    public Maze(MazeBuilder builder) {
        this.rooms = builder.rooms;
        this.distributeRandomly = builder.distributeRandomly;
    }




    public static class MazeBuilder {
        private List<Room> rooms;
        private boolean distributeRandomly;

        private MazeBuilder() {}

        public static MazeBuilder newInstance() {
            return new MazeBuilder();
        }

        public MazeBuilder createNbyMGrid(int n, int m) {

            if(n <= 0 || m <= 0) {
                throw new IllegalArgumentException("n and m must be positive");
            }

            // Create a 2D list to store the rooms
            Room[][] roomGrid = new Room[n][m];
            List<Room> newRooms = new ArrayList<>();

            // Create all the rooms
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    String roomName = "Room " + (i * m + j + 1); // Calculate room number
                    Room room = new Room(roomName);
                    roomGrid[i][j] = room;
                    newRooms.add(room);
                }
            }

            // Assign neighbors
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    Room currentRoom = roomGrid[i][j];

                    // Check and assign neighbors (left, right, above, below)
                    if (i > 0) {
                        currentRoom.addNeighbor(roomGrid[i - 1][j]); // above
                    }
                    if (i < n - 1) {
                        currentRoom.addNeighbor(roomGrid[i + 1][j]); // below
                    }
                    if (j > 0) {
                        currentRoom.addNeighbor(roomGrid[i][j - 1]); // left
                    }
                    if (j < m - 1) {
                        currentRoom.addNeighbor(roomGrid[i][j + 1]); // right
                    }
                }
            }

            // Add all the rooms to the Maze
            this.rooms = newRooms;

            return this;
        }


        public MazeBuilder createNFullyConnectedRooms(int n){

            if(n <= 0){
                throw new IllegalArgumentException("n must be greater than 0");
            }

            List<Room> newRooms = new ArrayList<>();

            // Create all the rooms
            for (int i = 0; i < n; i++) {
                String roomName = "Room " + (i + 1);
                Room room = new Room(roomName);
                newRooms.add(room);
            }

            // Assign neighbors
            for(Room room : newRooms){
                for(Room otherRoom : newRooms){
                    if(!otherRoom.equals(room)){
                        room.addNeighbor(otherRoom);
                    }
                }
            }

            return this;
        }

        public MazeBuilder distributeRandomly(){
            this.distributeRandomly = true;
            return this;
        }

        public MazeBuilder distributeSequentially() {
            this.distributeRandomly = false;
            return this;
        }

        public MazeBuilder addRoom(String roomName) {
            Room newRoom = new Room(roomName);

            // If there's already a room in the list, set the last room as its neighbor
            if (!this.rooms.isEmpty()) {
                Room lastRoom = this.rooms.get(this.rooms.size() - 1);
                newRoom.addNeighbor(lastRoom);
                lastRoom.addNeighbor(newRoom);
            }

            // Add the new room to the list of rooms
            this.rooms.add(newRoom);

            return this;
        }


        public MazeBuilder placeObjectIntoRoom(Object object, Room room){
            if(this.rooms.contains(room)){
                if(object instanceof Character){
                    room.add((Character) object);
                    logger.info("placeObjectIntoRoom: Character placed into room");
                } else if (object instanceof Food){
                    room.add((Food) object);
                    logger.info("placeObjectIntoRoom: Food placed into room");
                } else {
                    throw new IllegalArgumentException("object must be a Character or Food");
                }
            }else{
                throw new IllegalArgumentException("Maze must contain room to place object");
            }
            return this;
        }

        private void distributeObjectsRandomly(List<Object> objects){

            // Get random index
            Random random = new Random();
            int numRooms = this.rooms.size();

            // For each object, generate a rand index and add obj to it
            for(Object object : objects){

                int randomIndex = random.nextInt(numRooms);

                if (object instanceof Character) {
                    this.rooms.get(randomIndex).add((Character)object);
                } else if (object instanceof Food) {
                    this.rooms.get(randomIndex).add((Food)object);
                } else {
                    throw new IllegalArgumentException("object must be a Character or Food");
                }
            }

        }

        private void distributeObjectsSequentially(List<Object> objects){

            // Room index
            int roomIndex = 0;

            // For each object, add to room and add one to room index
            for(Object object : objects){

                if (object instanceof Character) {
                    this.rooms.get(roomIndex).add((Character)object);
                } else if (object instanceof Food) {
                    this.rooms.get(roomIndex).add((Food)object);
                } else {
                    throw new IllegalArgumentException("object must be a Character or Food");
                }

                roomIndex = (roomIndex + 1) % this.rooms.size();

            }
        }

        public MazeBuilder createAndAddFoodItems(List<String> foodNames){
            // TODO

            List<Food> foodsToAdd = new ArrayList<>();
            List<Object> foodsAsObjects = new ArrayList<>(foodsToAdd);

            if(distributeRandomly){

                // Randomly distribute items
                distributeObjectsRandomly(foodsAsObjects);

            } else {
                // Sequentially distribute items
                distributeObjectsSequentially(foodsAsObjects);
            }

            return this;
        }

        // TODO - Arguments???
        public MazeBuilder createAndAddAdventurers(List<String> adventurerNames){
            // TODO

            List<Food> adventurersToAdd = new ArrayList<>();
            List<Object> adventurersAsObjects = new ArrayList<>(adventurersToAdd);

            if(distributeRandomly){
                // Randomly distribute items
                distributeObjectsRandomly(adventurersAsObjects);
            } else {
                // Sequentially distribute items
                distributeObjectsSequentially(adventurersAsObjects);
            }

            return this;
        }

        // TODO - Arguments???
        public MazeBuilder createAndAddCreatures(List<String> creatureNames){
            // TODO

            List<Food> creaturesToAdd = new ArrayList<>();
            List<Object> creaturesAsObjects = new ArrayList<>(creaturesToAdd);

            if(distributeRandomly){

                // Randomly distribute items
                distributeObjectsRandomly(creaturesAsObjects);

            } else {
                // Sequentially distribute items
                distributeObjectsSequentially(creaturesAsObjects);
            }

            return this;
        }

        // TODO - Random methods for food, adventurers, creatures

        public Maze build(){
            return new Maze(this);
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
