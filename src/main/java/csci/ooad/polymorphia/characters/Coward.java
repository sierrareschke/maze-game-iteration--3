package csci.ooad.polymorphia.characters;

public class Coward extends Adventurer{

    static final Double COWARD_INITIAL_HEALTH = 5.0;

    public Coward(String name) {
        super(name, COWARD_INITIAL_HEALTH);
    }


    // TODO - does coward eat if there is a non-demon creature present (before fleeing)?

    // always runs from a creature, if it can (it can’t run from Demons)
    @Override
    public void doAction() {
        if (shouldFight()) { // fights if demon in room
            fight(getCurrentLocation().getHealthiestDemon());
        } else if (getCurrentLocation().hasLivingCreatures()) { // flees if non-demon creatures in room
            runAway(); // loses 0.5 health when fleeing
        } else if (getCurrentLocation().hasFood()) {
            eatFood();
        } else {
            move(); // loses 0.25 health
        }
    }

    private void runAway() {
        move();
        loseHealth(0.25);  // Loses 0.25 points extra points (0.25+0.25=0.5) when fleeing
    }

    @Override
    Boolean shouldFight() {
        // will only fight if there is a Demon in the room, otherwise runs
        return demonInRoomWithMe();
    }

}
