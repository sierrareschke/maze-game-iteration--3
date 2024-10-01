package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Adventurer extends Character {
    private static final Logger log = LoggerFactory.getLogger(Adventurer.class);

    public Adventurer(String name) {
        super(name);
    }

    public Adventurer(String name, Double initialHealth) {
        super(name, initialHealth);
    }

    public void enterRoom(Room room) {
        if (getCurrentLocation() != null) {
            if (getCurrentLocation().equals(room)) {
                return;
            }
            getCurrentLocation().remove(this);
        }
        super.enterRoom(room);
    }

    public void doAction() {
        if (shouldFight()) {
            fight(getCurrentLocation().getRandomCreature());
        } else if (getCurrentLocation().hasFood()) {
            eatFood();
        } else {
            move();
        }
    }

    private void eatFood() {
        Food foodItem = getCurrentLocation().eatFoodItem();
        log.info(getName() + " just ate " + foodItem);
        this.gainHealth(foodItem.getHealthValue());
    }

    Boolean shouldFight() {
        return creatureInRoomWithMe() && iAmHealthiestInRoom();
    }

    private boolean iAmHealthiestInRoom() {
        return this.equals(getCurrentLocation().getHealthiestAdventurer());
    }

    Boolean creatureInRoomWithMe() {
        return getCurrentLocation().hasLivingCreatures();
    }

    @Override
    public Boolean isAdventurer() {
        return true;
    }

}