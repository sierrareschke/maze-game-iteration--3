package csci.ooad.polymorphia;

import java.util.Random;

public class Die {
    private static Die singleton;
    private final Random rand = new Random();

    private Die() {
    }

    private Integer roll(Integer sides) {
        return randomInt(sides) + 1;
    }

    private Integer randomInt(Integer limit) {
        return rand.nextInt(limit);
    }

    public static Die getInstance() {
        if (singleton == null) {
            singleton = new Die();
        }
        return singleton;
    }

    public static Integer rollSixSided() {
        return rollNSided(6);
    }

    public static Integer rollNSided(Integer sides) {
        return getInstance().roll(sides);
    }

    public static Integer randomLessThan(Integer limit) {
        return getInstance().randomInt(limit);
    }
}
