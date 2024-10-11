package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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
            if (demonInRoomWithMe()) {
                fight(getCurrentLocation().getHealthiestDemon());
            }
            else {
                fight(getCurrentLocation().getRandomCreature());
            }
        } else if (getCurrentLocation().hasFood()) {
            eatFood();
        } else {
            move();
        }
    }
    public void eatFood() {
        Food foodItem = getCurrentLocation().eatFoodItem();
        log.info(getName() + " just ate " + foodItem);
        this.gainHealth(foodItem.getHealthValue());
    }

    Boolean shouldFight() {
        return creatureInRoomWithMe() && iAmHealthiestInRoom();
    }

    public boolean iAmHealthiestInRoom() {
        return this.equals(getCurrentLocation().getHealthiestAdventurer());
    }

    Boolean creatureInRoomWithMe() {
        return getCurrentLocation().hasLivingCreatures();
    }

    Boolean demonInRoomWithMe() {
        boolean demonPresent = false;
        List<Creature> creaturesPresent = getCurrentLocation().getLivingCreatures();
        for (Creature creature : creaturesPresent ){
            if (creature instanceof Demon) {
                demonPresent = true;
            }
        }
        return demonPresent;
    }


    @Override
    public Boolean isAdventurer() {
        return true;
    }

}