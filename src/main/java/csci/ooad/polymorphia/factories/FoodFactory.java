package csci.ooad.polymorphia.factories;

// Creates one concrete class

import csci.ooad.polymorphia.Die;
import csci.ooad.polymorphia.Food;

import java.util.ArrayList;

public class FoodFactory {
    static String[] foods = {"Hamburger", "Salad", "French Fries", "Apple", "Pancake", "Vanilla Oatmilk Latte", "Sandwich"};
    static int diceSides = 6;

    public ArrayList<Food> createListOfFood(int numberOfItems){
        ArrayList<Food> listOfFoods = new ArrayList<Food>();
        for (int i = 0; i < numberOfItems; i++){
            int diceRoll = Die.rollNSided(diceSides);
            listOfFoods.add(new Food(foods[diceRoll]));
        }
        return listOfFoods;
    }

    public Food createSingleFoodItem(String name){
        return new Food(name);
    }
}
