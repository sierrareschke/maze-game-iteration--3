package csci.ooad.polymorphia.characters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Creature extends Character {
    private static final Logger logger = LoggerFactory.getLogger(Creature.class);
    static final Double DEFAULT_INITIAL_HEALTH = 3.0;

    public Creature(String name) {
        super(name, DEFAULT_INITIAL_HEALTH);
    }

    public Creature(String name, double health) {
        super(name, health);
    }

    @Override
    public Boolean isCreature() {
        return true;
    }

    @Override
    public void doAction() {
        logger.info("Doing nothing for action for " + getName());
    }
}
