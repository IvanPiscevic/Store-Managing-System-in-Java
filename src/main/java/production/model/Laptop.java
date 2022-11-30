package production.model;

import java.math.BigDecimal;

/**
 * Represents a Laptop.
 * Extends Item Class.
 * Implements Technical Interface.
 */
public final class Laptop extends Item implements Technical {

    private int warranty;

    /**
     * Constructor with parameters.
     *
     * @param name String name.
     * @param category Category category name.
     * @param width BigDecimal width value.
     * @param height BigDecimal height value.
     * @param length BigDecimal length value.
     * @param productionCost BigDecimal Produciton Cost value.
     * @param sellingPrice BigDecimal Selling Price value.
     * @param discount Discount Record.
     * @param warranty Int warranty value (in months).
     */
    public Laptop(Long id, String name, Category category, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice, Discount discount, int warranty) {
        super(id, name, category, width, height, length, productionCost, sellingPrice, discount);
        this.warranty = warranty;
    }

    /**
     * Overrides getter method from technical interface.
     *
     * @return Integer value of warranty.
     */
    @Override
    public int getWarranty() {
        return warranty;
    }
    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }
}
