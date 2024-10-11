package csci.ooad.polymorphia.characters;

public class Glutton extends Adventurer {

    static final Double GLUTTON_INITIAL_HEALTH = 3.0;

    public Glutton(String name) {
        super(name, GLUTTON_INITIAL_HEALTH);
    }

    // TODO - does glutton keep eating until no more food or just eats once ?

    // always eats if food is available unless a Demon is in the room, then it must fight
    @Override
    public void doAction() {
        if (shouldFight()) { // fights if Demon in room
            fight(getCurrentLocation().getHealthiestDemon());
        } else if (getCurrentLocation().hasFood()) {
            while(getCurrentLocation().hasFood()) {
                eatFood();
            }
        } else {
            move();
        }
    }

    @Override
    Boolean shouldFight() {
        // will only fight if there is a Demon in the room
        return demonInRoomWithMe();
    }
}
