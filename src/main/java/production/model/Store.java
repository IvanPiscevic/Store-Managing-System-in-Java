package production.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a store.
 * Extends NamedEntity Class.
 */
public class Store extends NamedEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7138965692312121835L;
    private String webAddress;
    private Set<Item> items;

    /**
     * Constructor with parameters.
     *
     * @param name String name, which is being passed back to NameEntity constructor.
     * @param webAddress String webAddress of the store.
     * @param items Item object array, which contains all items which store is selling.
     */
    public Store(Long id, String name, String webAddress, Set<Item> items) {
        super(id, name);
        this.webAddress = webAddress;
        this.items = items;
    }

    public String getWebAddress() {
        return webAddress;
    }
    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public Set<Item> getItems() {
        return items;
    }
    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Store store = (Store) o;
        return Objects.equals(webAddress, store.webAddress) && Objects.equals(items, store.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), webAddress, items);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
