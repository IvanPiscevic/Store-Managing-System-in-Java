package production.model;

/**
 * Represents interface Edible
 * Has two abstract methods.
 * First one for calculating number of calories.
 * Second one for calculating price of Edible items.
 */
public interface Edible {

    int calculateCalories();
    int calculatePrice();
}
