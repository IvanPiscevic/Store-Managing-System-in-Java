package production.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a Bean.
 * Extends Item Class.
 * Implements interface Edible.
 */
public class Bean extends Item implements Edible {

    private static final int CALORIES_PER_KG = 3;
    BigDecimal weight; // in KG

    /**
     * Constructor which creates the Bean object.
     *
     * @param name Item name.
     * @param category Item category. Must be equal to 'Food' or 'Hrana'.
     * @param width Item width.
     * @param height Item height.
     * @param length Item length.
     * @param productionCost Item Production Cost.
     * @param sellingPrice Item Selling Price
     * @param weight Bean weight.
     * @param discount Discount object amount.
     */
    public Bean(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, BigDecimal weight, Discount discount) {
        super(id, name, category, width, height, length, productionCost, sellingPrice, discount);
        this.weight = weight;
    }

    public BigDecimal getWeight() {
        return weight;
    }
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     * Overrides calculateCalories method from interface Edible
     *
     * @return Calculated number of calories.
     */
    @Override
    public int calculateCalories() {
        int intWeight = weight.intValue();
        return intWeight * CALORIES_PER_KG;
    }

    /**
     * Overrides calculatePrice method from interface Edible
     *
     * @return Calculated price per kilogram.
     */
    @Override
    public int calculatePrice() { // Selling price per KG
        int intSellingPrice = getSellingPrice().intValue();
        int intWeight = weight.intValue();
        return intSellingPrice * intWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Bean bean = (Bean) o;
        return Objects.equals(weight, bean.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }
}
