package production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Represent Discount Record
 */
public record Discount(BigDecimal discountAmount) implements Serializable {

    /**
     * Discount record constructor which takes two parameters.
     *
     * @param discountAmount BigDecimal value of discount.
     */
    public Discount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return Objects.equals(discountAmount, discount.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount);
    }
}
