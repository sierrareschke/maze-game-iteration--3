package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Room;

public class Demon extends Creature {
    static final Double DEMON_INITIAL_HEALTH = 15.0;

    public Demon(String name) {
        super(name, DEMON_INITIAL_HEALTH);
    }

    // always fight an adventurer and an adventurer cannot run away from a demon.
    // if there are multiple adventurers in the room, the Demon fights them all.

    @Override
    public void enterRoom(Room room) {
        super.enterRoom(room);
        //fightAllAdventurers();
    }

    private void fightAllAdventurers() {
        for (Adventurer adventurer : getCurrentLocation().getLivingAdventurers()) {
            fight(adventurer);
        }
    }

    @Override
    public void doAction() {
        fightAllAdventurers();
    }
}
