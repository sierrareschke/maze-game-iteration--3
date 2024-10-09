package csci.ooad.polymorphia;

import java.util.Random;

public class Die {
    private static Die singleton;
    private final Random rand = new Random();

    public Die() {
    }

    private int roll(int sides) {
        return randomInt(sides) + 1;
    }

    private int randomInt(int limit) {
        return rand.nextInt(limit);
    }

    public static Die getInstance() {
        if (singleton == null) {
            singleton = new Die();
        }
        return singleton;
    }

    public static int rollSixSided() {
        return rollNSided(6);
    }

    public static int rollNSided(int sides) {
        return getInstance().roll(sides);
    }

    public static int randomLessThan(int limit) {
        return getInstance().randomInt(limit);
    }
}
