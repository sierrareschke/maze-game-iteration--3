package csci.ooad.polymorphia.factories;

import csci.ooad.polymorphia.Food;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class FoodFactoryTest {

    @Test
    void testCreateListOfFood() {
        FoodFactory factory = new FoodFactory();
        ArrayList<Food> foodList = factory.createListOfFood(5);

        // Verify that the list contains 5 items
        assertEquals(5, foodList.size(), "The list should contain 5 food items");

        // Verify that all items in the list are instances of Food
        foodList.forEach(food -> assertNotNull(food, "Food object should not be null"));
    }

    @Test
    void testCreateSingleFoodItem() {
        FoodFactory factory = new FoodFactory();
        Food singleFood = factory.createSingleFoodItem("Pizza");

        // Verify that the default health value is applied
        assertEquals(Food.DEFAULT_FOOD_HEALTH_VALUE, singleFood.getHealthValue(), "Default health value should be applied");
    }

    @Test
    void testCreateListOfFoodRandomness() {
        FoodFactory factory = new FoodFactory();
        ArrayList<Food> foodList1 = factory.createListOfFood(5);
        ArrayList<Food> foodList2 = factory.createListOfFood(5);

        // Check that two lists are not identical (since food is randomly chosen)
        assertNotEquals(foodList1, foodList2, "Two different food lists should not be identical due to randomness");
    }
}
