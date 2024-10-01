package csci.ooad.polymorphia;

public class Food {
    public static int DEFAULT_FOOD_HEALTH_VALUE = 1;

    private final String name;
    private final int healthValue;

    public Food(String name) {
        this.name = name;
        this.healthValue = DEFAULT_FOOD_HEALTH_VALUE;
    }

    public Food(String name, int healthValue) {
        this.name = name;
        this.healthValue = healthValue;
    }

    @Override
    public String toString() {
        return name + "(" + healthValue +")";
    }

    public int getHealthValue() {
        return healthValue;
    }

}
