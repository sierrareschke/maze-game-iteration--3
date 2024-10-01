package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Die;
import csci.ooad.polymorphia.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Character implements Comparable<Character> {
    private static final Logger logger = LoggerFactory.getLogger(Character.class);

    static final Double DEFAULT_INITIAL_HEALTH = 5.0;
    static final Double HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME = 0.5;
    static final Double HEALTH_LOST_IN_MOVING_ROOMS = 0.25;

    protected String name;
    private Double health;

    private Room currentLocation;

    public Room getCurrentLocation() {
        return currentLocation;
    }

    public Character(String name) {
        this(name, DEFAULT_INITIAL_HEALTH);
    }

    public Character(String name, Double initialHealth) {
        this.name = name;
        this.health = initialHealth;
    }

    @Override
    public int compareTo(Character otherCharacter) {
        return getHealth().compareTo(otherCharacter.getHealth());
    }

    public void enterRoom(Room room) {
        this.currentLocation = room;
    }

    public String toString() {
        return getName() + "(health: " + getHealth() + ")";
    }

    public void loseHealth(Double healthPoints) {
        if (health <= 0) {
            return;     // already dead, probably called for mandatory health loss for having a fight
        }

        health -= healthPoints;

        if (health <= 0) {
            logger.info("{} just died!", name);
        }
    }

    public Double getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public Boolean isAlive() {
        return getHealth() > 0;
    }

    public void loseFightDamage(double fightDamage) {
        loseHealth(fightDamage);
    }

    public Boolean isAdventurer() {
        return false;
    }

    public Boolean isCreature() {
        return false;
    }

    public void fight(Character opponent) {
        Integer adventurerRoll = Die.rollSixSided();
        Integer creatureRoll = Die.rollSixSided();
        logger.info(getName() + " is fighting " + opponent);

        logger.info(getName() + " rolled " + adventurerRoll);
        logger.info(opponent + " rolled " + creatureRoll);

        if (adventurerRoll > creatureRoll) {
            opponent.loseFightDamage(adventurerRoll - creatureRoll);
        } else if (creatureRoll > adventurerRoll) {
            loseFightDamage(creatureRoll - adventurerRoll);
        }

        loseHealth(Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME);
        opponent.loseHealth(Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME);
    }

    public abstract void doAction();

    protected void move() {
        Room nextLocation = getCurrentLocation().getRandomNeighbor();
        if (nextLocation != null) {
            logger.info(getName() + " moved from " + getCurrentLocation().getName() + " to " + nextLocation.getName());
            nextLocation.enter(this);
            loseHealth(HEALTH_LOST_IN_MOVING_ROOMS);
        } else {
            logger.warn(getCurrentLocation().getName() + " has no neighbors!");
        }
    }

    protected void gainHealth(double healthValue) {
        this.health += healthValue;
        logger.info(getName() + " gained health: " + healthValue);
    }
}
