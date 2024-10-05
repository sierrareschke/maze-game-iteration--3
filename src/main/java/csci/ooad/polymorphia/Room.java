package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;
import csci.ooad.polymorphia.characters.Demon;
import csci.ooad.polymorphia.characters.Character;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Room {
    private final String name;
    private final List<Room> neighbors = new ArrayList<>();
    private final List<Character> characters = new ArrayList<>();
    private final List<Food> foodItems = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Adventurer> getLivingAdventurers() {
        return characters.stream()
                .filter(Character::isAdventurer)
                .filter(Character::isAlive)
                .map(Adventurer.class::cast)
                .sorted().toList();
    }

    public List<Creature> getLivingCreatures() {
        return characters.stream()
                .filter(Character::isCreature)
                .filter(Character::isAlive)
                .map(Creature.class::cast)
                .sorted()
                .toList();
    }

    public List<String> getContents() {
        List<String> contents = new ArrayList<>(characters.stream()
                .map(Object::toString)
                .toList());
        contents.addAll(this.foodItems.stream()
                .map(Object::toString)
                .toList());
        return contents;
    }

    public void addNeighbor(Room neighbor) {
        // Make sure we are never a neighbor of ourselves
        assert this != neighbor;
        this.neighbors.add(neighbor);
    }

    public Room connect(Room neighbor) {
        this.addNeighbor(neighbor);
        neighbor.addNeighbor(this);
        return this;
    }

    public String toString() {
        String representation = "\t" + name + ":\n\t\t";
        representation += String.join("\n\t\t", getContents());
        return representation;
    }

    public void add(Character character) {
        characters.add(character);
        character.enterRoom(this);
    }

    public Boolean hasLivingCreatures() {
        return characters.stream()
                .filter(Character::isCreature)
                .filter(Character::isAlive)
                .anyMatch(Character::isAlive);
    }

    public Boolean hasLivingAdventurers() {
        return characters.stream()
                .filter(Character::isAdventurer)
                .filter(Character::isAlive)
                .anyMatch(Character::isAlive);
    }

    public void remove(Character character) {
        characters.remove(character);
    }

    public Creature getRandomCreature() {
        List<Creature> creatures = getLivingCreatures();
        return creatures.get(Die.randomLessThan(creatures.size()));
    }

    public Room getRandomNeighbor() {
        if (neighbors.isEmpty()) {
            return null;
        }
        return neighbors.stream().toList().get(Die.randomLessThan(neighbors.size()));
    }

    public void enter(Character character) {
        add(character);
    }

    public List<Character> getLivingCharacters() {
        return characters.stream()
                .filter(Character::isAlive)
                .toList();
    }

    public void add(Food foodItem) {
        foodItems.add(foodItem);
    }

    public Adventurer getHealthiestAdventurer() {
        return getLivingAdventurers().stream().max(Comparator.naturalOrder()).get();
    }

    public Creature getHealthiestCreature() {
        return getLivingCreatures().stream().max(Comparator.naturalOrder()).get();
    }

    public Demon getHealthiestDemon() {
        return getLivingCreatures().stream()
                .filter(creature -> creature instanceof Demon)  // Only consider Demons
                .map(creature -> (Demon) creature)  // Cast to Demon
                .max(Comparator.naturalOrder())     // Find the healthiest Demon
                .orElse(null);  // Return null if no Demons are found
    }


    public boolean hasFood() {
        return !foodItems.isEmpty();
    }

    public Food eatFoodItem() {
        if (foodItems.isEmpty()) {
            return null;
        }
        return foodItems.removeFirst();
    }
}
