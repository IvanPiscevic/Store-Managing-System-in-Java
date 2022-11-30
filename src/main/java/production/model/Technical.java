package production.model;

/**
 * Represents Technical Interface.
 * Contains one abstract method.
 */
public sealed interface Technical permits Laptop {
    /**
     * Abstract method which acts like a getter for warranty.
     *
     * @return int warranty.
     */
    int getWarranty();
}
