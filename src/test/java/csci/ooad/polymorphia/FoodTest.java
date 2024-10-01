package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FoodTest {

    @Test
    void testFoodCreation() {
        Food steak = new Food("Steak");
        assert steak.getHealthValue() == Food.DEFAULT_FOOD_HEALTH_VALUE;
    }

    @Test
    void testSettingHealthValue() {
        Food steak = new Food("Steak", 2);
        assert steak.getHealthValue() == 2;
    }

    @Test
    void testToString() {
        Food steak = new Food("Steak", 2);
        System.out.println(steak.toString());
        assertTrue(steak.toString().contains("Steak"));
        assertTrue(steak.toString().contains("2"));
    }

}