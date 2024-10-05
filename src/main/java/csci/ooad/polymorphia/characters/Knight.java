package csci.ooad.polymorphia.characters;

public class Knight extends Adventurer{

    // initial health score: 8
    static final Double KNIGHT_INITIAL_HEALTH = 8.0;

    public Knight(String name) {
        super(name, KNIGHT_INITIAL_HEALTH);
    }

    // always fights any Creature in the room (doesn't have to be healthiest Adventurer present in room)
    @Override
    Boolean shouldFight() {
        return creatureInRoomWithMe();
    }
}
