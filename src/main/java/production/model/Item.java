package production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents Item.
 * Extends NameEntity Class.
 */
public class Item extends NamedEntity implements Serializable {

    // Item variables
    private Category category;
    private BigDecimal width, height, length, productionCost, sellingPrice;
    private BigDecimal volume;
    private Discount discount;

    /**
     * Constructor with parameters.
     * Has possibility to shorten Item names if they are longer than 10 characters.
     *
     * @param name String name of Item, which is being passed to NamedEntity class constructor.
     * @param category Caregory object, which contains category information.
     * @param width BigDecimal width value.
     * @param height BigDecimal height value.
     * @param length BigDecimal length value.
     * @param productionCost BigDecimal Production Cost value.
     * @param sellingPrice BigDecimal Selling Price value.
     * @param discount Discount Record.
     */
    public Item (Long id, String name, Category category,
                BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Discount discount) {
        super(id, name);
        /*if(name.length() > 10) {
            String newName; // Removed "= new String()"
            newName = name.substring(0, 10) + "...";
            this.setName(newName);
        }*/
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.volume = this.width.multiply(this.length.multiply(this.height));
    }

    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getWidth() {
        return width;
    }
    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }
    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }
    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Discount getDiscount() {
        return discount;
    }
    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public BigDecimal getVolume() {
        return volume;
    }
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return Objects.equals(category, item.category) && Objects.equals(width, item.width) && Objects.equals(height,
                item.height) && Objects.equals(length, item.length) && Objects.equals(productionCost, item.productionCost)
                && Objects.equals(sellingPrice, item.sellingPrice) && Objects.equals(discount, item.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category, width, height, length, productionCost, sellingPrice, discount);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
