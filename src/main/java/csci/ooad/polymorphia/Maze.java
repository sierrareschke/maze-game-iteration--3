package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Character;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.factories.FoodFactory;
import csci.ooad.polymorphia.factories.CharacterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;



public class Maze  {
    private final Random rand = new Random();
    private static final Logger logger = LoggerFactory.getLogger(Maze.class);

    private final List<Room> rooms;
    private final boolean distributeRandomly; // 1 = rand, 0 = simultaneous distribution


    private Maze(MazeBuilder builder) {
        this.rooms = builder.rooms;
        this.distributeRandomly = builder.distributeRandomly;
    }

    public static MazeBuilder newBuilder() {
        return new MazeBuilder();
    }


    public static class MazeBuilder {
        private List<Room> rooms;
        private boolean distributeRandomly;
        private final FoodFactory foodFactory = new FoodFactory();
        private final CharacterFactory characterFactory = new CharacterFactory();



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

        public MazeBuilder createAndAddFoodItems(int numFoods){
            // TODO

            List<Food> foodsToAdd = foodFactory.createListOfFood(numFoods);
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

        /*
        * @param numAdventurers is the total num adventurers to be distributed to Maze rooms (type rand. determined by CharacterFactory)
        * */
        public MazeBuilder createAndAddAdventurers(int numAdventurers) {
            // Ensure the characterFactory is not null
            if (characterFactory == null) {
                throw new IllegalStateException("CharacterFactory must be initialized before creating adventurers.");
            }

            // Call the non-static method using the characterFactory instance
            ArrayList<Character> adventurersToAdd = characterFactory.createAdventurers(numAdventurers);
            List<Object> adventurersAsObjects = new ArrayList<>(adventurersToAdd);

            if (distributeRandomly) {
                // Randomly distribute items
                distributeObjectsRandomly(adventurersAsObjects);
            } else {
                // Sequentially distribute items
                distributeObjectsSequentially(adventurersAsObjects);
            }

            return this;
        }

        public MazeBuilder createAndAddAdventurers(int numKnights, int numCowards, int numGluttons, int numRegular) {
            // Ensure the characterFactory is not null
            if (characterFactory == null) {
                throw new IllegalStateException("CharacterFactory must be initialized before creating adventurers.");
            }

            // Call the non-static method using the characterFactory instance
            ArrayList<Character> adventurersToAdd = characterFactory.createAdventurers(numKnights, numCowards, numGluttons, numRegular);
            List<Object> adventurersAsObjects = new ArrayList<>(adventurersToAdd);

            if (distributeRandomly) {
                // Randomly distribute items
                distributeObjectsRandomly(adventurersAsObjects);
            } else {
                // Sequentially distribute items
                distributeObjectsSequentially(adventurersAsObjects);
            }

            return this;
        }

        // TODO - Arguments???
        public MazeBuilder createAndAddCreatures(int numCreatures, boolean isDemon){
            // TODO

            ArrayList<Character> creaturesToAdd = characterFactory.createCreatures(numCreatures,isDemon);
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

    public List<Room> getRooms() {
        return rooms;
    }
}
